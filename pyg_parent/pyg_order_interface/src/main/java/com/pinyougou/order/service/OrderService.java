package com.pinyougou.order.service;

import com.pyg_pojo.entity.Order;
import com.pyg_pojo.pojo.TbOrder;

import java.util.List;

public interface OrderService {
    //條件分頁查詢
    public List<Order> findAll(Order order);

    /**
     * 按商家查询
     *
     * @param sellerId
     * @return
     */
    public abstract List<TbOrder> findAll(String sellerId);

    /**
     * 购物车转订单
     */
    public abstract void addCartOrder(TbOrder tbOrder);
}
