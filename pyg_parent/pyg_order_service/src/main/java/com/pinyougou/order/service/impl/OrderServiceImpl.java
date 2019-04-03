package com.pinyougou.order.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.pinyougou.order.service.OrderService;
import com.pinyougou.utils.IdWorker;
import com.pyg_dao.mapper.TbGoodsMapper;
import com.pyg_dao.mapper.TbOrderItemMapper;
import com.pyg_dao.mapper.TbOrderMapper;
import com.pyg_dao.mapper.TbPayLogMapper;
import com.pyg_pojo.entity.Cart;
import com.pyg_pojo.entity.Order;
import com.pyg_pojo.pojo.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.*;

@Service(timeout = 10000)
@Transactional
public class OrderServiceImpl implements OrderService {

    @Autowired
    private TbOrderMapper tbOrderMapper;

    @Autowired
    private TbOrderItemMapper tbOrderItemMapper;

    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private TbPayLogMapper tbPayLogMapper;

    //雪花算法
    @Autowired
    private IdWorker idWorker;

    @Autowired
    private IdWorker idWorker1;

    @Autowired
    private IdWorker idWorker2;

    @Autowired
    private TbGoodsMapper tbGoodsMapper;



    @Override
    public List<TbOrder> findAll(String sellerId) {
        return null;
    }

    @Override
    public void addCartOrder(TbOrder tbOrder) {
        //购物车变订单
        List<Cart> cartList = (List<Cart>) redisTemplate.boundHashOps("CartList").get(tbOrder.getUserId());

        double totalPrice = 0.0;//购物车总金额
        //订单编号集合
        List<Long> orderIds = new ArrayList();
        for (Cart cart : cartList) {
            double money = 0.0;//实付金额
            TbOrder order = new TbOrder();
            //雪花算法（时间戳，计算机名，序列号）
            long nextId = idWorker.nextId();
            //一个购物车一个订单
            orderIds.add(nextId);
            order.setOrderId(nextId);//订单id
            order.setPaymentType(tbOrder.getPaymentType());//支付类型
            order.setStatus("1");//支付状态'状态：1、未付款，2、已付款，3、未发货，4、已发货，5、交易成功，6、交易关闭,7、待评价',
            order.setCreateTime(new Date());//创建时间
            order.setUpdateTime(new Date());//修改时间
            order.setUserId(tbOrder.getUserId());//用户id
            order.setReceiverMobile(tbOrder.getReceiverMobile());//联系人手机号
            order.setReceiver(tbOrder.getReceiver());//联系人
            order.setSourceType("2");//订单来源：1:app端，2：pc端，3：M端，4：微信端，5：手机qq端',
            order.setSellerId(cart.getSellerId());//商家id
            order.setReceiverAreaName(tbOrder.getReceiverAreaName());//地址
            long currentTime = System.currentTimeMillis() + 30 * 60 * 1000;
            Date date = new Date(currentTime);
            order.setExpire(date);//订单失效时间
            List<TbOrderItem> tbOrderItems = cart.getTbOrderItems();
            for (TbOrderItem tbOrderItem : tbOrderItems) {
                money += tbOrderItem.getTotalFee().doubleValue();
                tbOrderItem.setId(idWorker1.nextId());
                tbOrderItem.setOrderId(nextId);
                tbOrderItemMapper.insert(tbOrderItem);
            }
            order.setPayment(new BigDecimal(money));//实付金额
            //添加订单
            tbOrderMapper.insert(order);
            totalPrice += money;
        }
        //移除redis中的数据
        redisTemplate.boundHashOps("CartList").delete(tbOrder.getUserId());

        //生成支付单
        TbPayLog tbPayLog = new TbPayLog();
        //支付订单号采用雪花算法
        tbPayLog.setOutTradeNo(String.valueOf(idWorker2.nextId()));
        //创建时间
        tbPayLog.setCreateTime(new Date());
        //用户id
        tbPayLog.setUserId(tbOrder.getUserId());
        //支付状态0未支付，1，已支付
        tbPayLog.setTradeState("0");
        //订单编号的集合
        tbPayLog.setOrderList(orderIds.toString().replace("[", "").replace("]", "").replace(" ", ""));
        //支付类型
        tbPayLog.setPayType(tbOrder.getPaymentType());
        //总金额
        tbPayLog.setTotalFee((long) (totalPrice * 100));
        //添加
        tbPayLogMapper.insert(tbPayLog);
        //把数据存入到redis中
        redisTemplate.boundHashOps("payLog").put(tbOrder.getUserId(), tbPayLog);
    }

    @Override
    public List<Order> findAll(Order order1) {
        List<TbOrder> orders = new ArrayList<>();
        if (order1 != null) {
            //根据sellerId查询所有的订单
            TbOrderExample example = new TbOrderExample();
            TbOrderExample.Criteria criteria = example.createCriteria();
            if (order1.getSellerId() != null && order1.getSellerId().length() > 0) {
                criteria.andSellerIdEqualTo(order1.getSellerId());
            }
            if (order1.getStatus() != null && order1.getStatus().length() > 0) {
                criteria.andStatusEqualTo(order1.getStatus());
            }
            List<TbOrder> tbOrders1 = tbOrderMapper.selectByExample(example);
            //日期查询条件
            if (order1.getCreateTime() != null && tbOrders1.size() > 0) {
                Calendar instance = Calendar.getInstance();
                instance.setTime(order1.getCreateTime());
                for (TbOrder tbOrder : tbOrders1) {
                    Calendar instance1 = Calendar.getInstance();
                    instance1.setTime(tbOrder.getCreateTime());
                    if (instance.get(Calendar.YEAR) == instance1.get(Calendar.YEAR) && instance.get(Calendar.MONTH) == instance1.get(Calendar.MONTH) && instance.get(Calendar.DAY_OF_MONTH) == instance1.get(Calendar.DAY_OF_MONTH)) {
                        orders.add(tbOrder);
                    }
                }
            }else{
                    orders= tbOrders1;
            }
//            //新建一个封装对象的list集合
            List<Order> orderList = new ArrayList<Order>();
            //循环订单集合
            for (TbOrder tbOrder : orders) {
                //根据订单id得到对应的订单详情集合
                TbOrderItemExample example1 = new TbOrderItemExample();
                example1.createCriteria().andOrderIdEqualTo(tbOrder.getOrderId());
                List<TbOrderItem> tbOrderItems = tbOrderItemMapper.selectByExample(example1);
                //遍历订单详情集合
                for (TbOrderItem tbOrderItem : tbOrderItems) {
                    //得到商品id并得到商品
                    Long goodsId = tbOrderItem.getGoodsId();
                    TbGoods tbGoods = tbGoodsMapper.selectByPrimaryKey(goodsId);
                    //新建一个封装对象，并封装数据
                    Order order = new Order();
                    order.setGoodsName(tbGoods.getGoodsName());
                    order.setPrice(tbOrderItem.getPrice().doubleValue());
                    order.setNum(tbOrderItem.getNum());
                    order.setTotalFee(tbOrderItem.getTotalFee().doubleValue());
                    order.setStatus(tbOrder.getStatus());
                    order.setCreateTime(tbOrder.getCreateTime());
                    order.setSourceType(tbOrder.getSourceType());
                    //添加到集合当中
                    orderList.add(order);
                }
            }
            return orderList;
        }
        return null;
    }
}
