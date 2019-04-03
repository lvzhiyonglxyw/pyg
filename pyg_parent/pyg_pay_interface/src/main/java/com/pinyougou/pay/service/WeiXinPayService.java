package com.pinyougou.pay.service;

import com.pyg_pojo.pojo.TbPayLog;

import java.util.Map;

public interface WeiXinPayService {

    /**
     * 根据订单号修改支付状态
     *
     * @param out_trade_no
     * @param transaction_id
     */
    public abstract void updateOrderStatus(String out_trade_no, String transaction_id);

    /**
     * 根据用户ID获取redis中的信息
     *
     * @param key
     * @return
     */
    public abstract TbPayLog findByUserId(String key);

    /**
     * 查询订单支付状态
     *
     * @param out_trade_no 商品订单号
     * @return
     */
    public abstract Map queryPayStatus(String out_trade_no);

    /**
     * 生成支付二维码
     *
     * @param out_trade_no 商品订单号
     * @param total_fee    商品金额
     * @return
     */
    public abstract Map createNative(String out_trade_no, String total_fee);
}
