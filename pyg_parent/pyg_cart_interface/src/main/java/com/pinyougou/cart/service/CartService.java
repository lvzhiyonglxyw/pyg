package com.pinyougou.cart.service;

import com.pyg_pojo.entity.Cart;
import com.pyg_pojo.pojo.TbOrderItem;

import java.util.List;

public interface CartService {

    /**
     * 把未登录的购物车从redis中删除掉
     *
     * @param key
     */
    public abstract void removeRedisCart(String key);

    /**
     * 合并未登录的购物和已登录的购物车
     *
     * @param cartList1 未登录
     * @param cartList2 已登录
     * @return
     */
    public abstract List<Cart> mergeCartList(List<Cart> cartList1, List<Cart> cartList2);

    /**
     * 把购物车集合存入到redis中
     *
     * @param key
     * @param cartList
     */
    public abstract void addRedisCart(String key, List<Cart> cartList);

    /**
     * 根据sellId从redis中获取购物车再根据itemid获取数据
     *
     * @param sellerId
     * @param itemId
     * @return
     */
    public abstract List<Cart> findBySellerIdAndItemId(String name,String sellerId, String[] itemId);

    /**
     * 从redis中获取购物车
     *
     * @param key
     * @return
     */
    public abstract List<Cart> findRedisCart(String key);

    /**
     * 向购物车中添加商品
     *
     * @param cartList 购物车集合
     * @param num      购物车商品数量
     * @param itemId   商品id
     * @return
     */
    public abstract List<Cart> addItemCartList(List<Cart> cartList, Integer num, Long itemId);
}
