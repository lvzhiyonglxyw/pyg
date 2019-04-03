package com.pinyougou.cart.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.pinyougou.cart.service.CartService;
import com.pyg_dao.mapper.TbItemMapper;
import com.pyg_pojo.entity.Cart;
import com.pyg_pojo.pojo.TbItem;
import com.pyg_pojo.pojo.TbOrderItem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
public class CartServiceImpl implements CartService {

    @Autowired
    private TbItemMapper tbItemMapper;

    @Autowired
    private RedisTemplate redisTemplate;

    /**
     * 把未登录的购物车从redis中删除掉
     *
     * @param key
     */
    @Override
    public void removeRedisCart(String key) {
        redisTemplate.boundHashOps("CartList").delete(key);
    }

    /**
     * 合并未登录的购物车和已合并的购物车
     *
     * @param cartList1 未登录
     * @param cartList2 已登录
     * @return
     */
    @Override
    public List<Cart> mergeCartList(List<Cart> cartList1, List<Cart> cartList2) {
        for (Cart cart : cartList1) {
            List<TbOrderItem> tbOrderItems = cart.getTbOrderItems();
            for (TbOrderItem tbOrderItem : tbOrderItems) {
                //把未登录的购物车合并到已登录的购物车中
                cartList2 = addItemCartList(cartList2, tbOrderItem.getNum(), tbOrderItem.getItemId());
            }
        }
        return cartList2;
    }

    /**
     * 把购物车集合添加到redis中
     *
     * @param key
     * @param cartList
     */
    @Override
    public void addRedisCart(String key, List<Cart> cartList) {
        redisTemplate.boundHashOps("CartList").put(key, cartList);
    }

    /**
     * 从redis中获取数据
     *
     * @param sellerId
     * @param itemId
     * @return
     */
    @Override
    public List<Cart> findBySellerIdAndItemId(String name, String sellerId, String[] itemId) {
        List<Cart> cartList = (List<Cart>) redisTemplate.boundHashOps("CartList").get(name);
        if (cartList != null) {
            for (Cart cart : cartList) {
                if (sellerId.equals(cart.getSellerId())){
                    System.out.println("sellerId相等");
                    List<TbOrderItem> tbOrderItems = cart.getTbOrderItems();
                    for (TbOrderItem tbOrderItem : tbOrderItems) {
                        System.out.println("itemId:"+tbOrderItem.getItemId());
                        for (String item : itemId) {
                            System.out.println("ittem:"+item);
                            if (item.equals(tbOrderItem.getItemId())){
                                System.out.println("相等");
                                return cartList;
                            }else{
                                System.out.println("1相等");
                            }
                        }
                    }
                }
            }
        }
        return cartList;
    }

    /**
     * 从redis中获取购物车
     *
     * @param key
     * @return
     */
    @Override
    public List<Cart> findRedisCart(String key) {
        List<Cart> cartList = (List<Cart>) redisTemplate.boundHashOps("CartList").get(key);
        if (cartList == null) {
            cartList = new ArrayList<>();
        }
        return cartList;
    }

    @Override
    public List<Cart> addItemCartList(List<Cart> cartList, Integer num, Long itemId) {
        //判断购物车中是否已存在该商家的商品信息
        TbItem tbItem = tbItemMapper.selectByPrimaryKey(itemId);
        //购物车中是否已存在该商家
        Cart cart = searchCartListBySellerId(cartList, tbItem.getSellerId());
        //判断商家是否已存在
        if (cart == null) {
            //不存在,向购物车集合中添加购物车
            TbOrderItem tbOrderItem = setValue(tbItem, num);
            List<TbOrderItem> orderItemList = new ArrayList<>();
            orderItemList.add(tbOrderItem);
            //向购物车中添加信息
            cart = new Cart();
            cart.setSellerId(tbItem.getSellerId());
            cart.setSellerName(tbItem.getSeller());
            cart.setTbOrderItems(orderItemList);
            cartList.add(cart);
        } else {
            //已存在,再根据商品id判断该商品是否已存在购物车中
            TbOrderItem tbOrderItem = searchOrderItemByItemId(cart, itemId);
            if (tbOrderItem != null) {
                //如果已存在   修改购物车中商品数量和总金额,判断商品明细数量是否小于1，小于1，从购物车中移除掉，再判断购物车集合中的购物车数量是否小于1，，从购物车集合中移除掉
                //修改商品数量
                tbOrderItem.setNum(tbOrderItem.getNum() + num);
                //修改商品总金额
                tbOrderItem.setTotalFee(new BigDecimal(tbOrderItem.getPrice().doubleValue() * tbOrderItem.getNum()));
                //判断商品明细数量是否小于1，小于1，从购物车中移除掉
                if (tbOrderItem.getNum() < 1) {
                    //移除商品数量小于1的购物车
                    cart.getTbOrderItems().remove(tbOrderItem);
                }
                //判断购物车集合中的购物车数量是否小于1，，从购物车集合中移除掉
                if (cart.getTbOrderItems().size() < 1) {
                    //移除购物车集合中购物车个数小于1的购物车
                    cartList.remove(cart);
                }
            } else {
                //如果不存在，向购物车中添加商品
                TbOrderItem tbOrderItem1 = setValue(tbItem, num);
                cart.getTbOrderItems().add(tbOrderItem1);
            }
        }
        return cartList;
    }

    /**
     * 根据当前商品id判断购物车中是否存在该商品
     *
     * @param cart   购物车
     * @param itemId 商品id
     * @return 商品明细
     */
    public TbOrderItem searchOrderItemByItemId(Cart cart, Long itemId) {
        List<TbOrderItem> tbOrderItems = cart.getTbOrderItems();
        for (TbOrderItem tbOrderItem : tbOrderItems) {
            if (tbOrderItem.getItemId().equals(itemId)) {
                return tbOrderItem;
            }
        }
        return null;
    }

    /**
     * 准备购物车信息
     *
     * @param tbItem spu表
     * @param num    商品数量
     * @return TbOrderItem
     */
    public TbOrderItem setValue(TbItem tbItem, Integer num) {
        TbOrderItem tbOrderItem = new TbOrderItem();
        tbOrderItem.setGoodsId(tbItem.getGoodsId());
        tbOrderItem.setItemId(tbItem.getId());
        tbOrderItem.setNum(num);
        tbOrderItem.setPicPath(tbItem.getImage());
        tbOrderItem.setPrice(tbItem.getPrice());
        tbOrderItem.setSellerId(tbItem.getSellerId());
        tbOrderItem.setTitle(tbItem.getTitle());
        tbOrderItem.setTotalFee(new BigDecimal(tbItem.getPrice().doubleValue() * num));
        return tbOrderItem;
    }

    /**
     * 根据sellerId查找购物车中是否已存在该商家
     *
     * @param cartList 购物车集合中的商家id
     * @param sellerId item对象中的商家id
     * @return 购物车
     */
    private Cart searchCartListBySellerId(List<Cart> cartList, String sellerId) {
        for (Cart cart : cartList) {
            if (sellerId.equals(cart.getSellerId())) {
                return cart;
            }
        }
        return null;
    }
}
