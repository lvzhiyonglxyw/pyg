package com.pinyougou.pay.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.github.wxpay.sdk.WXPayUtil;
import com.pinyougou.pay.service.WeiXinPayService;
import com.pinyougou.utils.HttpClient;
import com.pyg_dao.mapper.TbOrderMapper;
import com.pyg_dao.mapper.TbPayLogMapper;
import com.pyg_pojo.pojo.TbOrder;
import com.pyg_pojo.pojo.TbPayLog;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service
@Transactional
public class WeiXinPayServiceImpl implements WeiXinPayService {

    @Value("${appid}")
    private String appid;//公众账号ID

    @Value("${partner}")
    private String mch_id;//商户ID

    @Value("${notifyurl}")
    private String notify_url;//通知地址

    @Value("${partnerkey}")
    private String partnerkey;//签名

    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private TbPayLogMapper tbPayLogMapper;

    @Autowired
    private TbOrderMapper tbOrderMapper;

    /**
     * 根据订单号查询
     *
     * @param out_trade_no
     * @param transaction_id
     */
    @Override
    public void updateOrderStatus(String out_trade_no, String transaction_id) {
        TbPayLog tbPayLog = tbPayLogMapper.selectByPrimaryKey(out_trade_no);
        //支付时间
        tbPayLog.setPayTime(new Date());
        //交易号码
        tbPayLog.setTransactionId(transaction_id);
        //0：未支付  1：已支付
        tbPayLog.setTradeState("1");
        tbPayLogMapper.updateByPrimaryKey(tbPayLog);
        String[] split = tbPayLog.getOrderList().split(",");
        for (String order : split) {
            TbOrder tbOrder = tbOrderMapper.selectByPrimaryKey(Long.valueOf(order));
            //支付状态状态：1、未付款，2、已付款，3、未发货，4、已发货，5、交易成功，6、交易关闭,7、待评价',
            tbOrder.setStatus("2");
            //更新订单时间
            tbOrder.setUpdateTime(new Date());
            //付款时间
            tbOrder.setPaymentTime(new Date());
            tbOrderMapper.updateByPrimaryKey(tbOrder);
        }
        //清空redis
        redisTemplate.boundHashOps("payLog").delete(tbPayLog.getUserId());
    }

    /**
     * 根据用户id查询从redis中获取信息
     *
     * @param key
     * @return
     */
    @Override
    public TbPayLog findByUserId(String key) {
        return (TbPayLog) redisTemplate.boundHashOps("payLog").get(key);
    }

    /**
     * 查询订单状态
     *
     * @param out_trade_no 商品订单号
     * @return
     */
    @Override
    public Map queryPayStatus(String out_trade_no) {
        try {
            //准备发送请求
            HttpClient httpClient = new HttpClient("https://api.mch.weixin.qq.com/pay/orderquery");
            //设置参数
            Map requestMap = new HashMap();
            requestMap.put("appid", appid);//公众账号ID
            requestMap.put("mch_id", mch_id);//商户ID
            requestMap.put("out_trade_no", out_trade_no);//商品订单号
            requestMap.put("nonce_str", WXPayUtil.generateUUID());//随机字符串
            //把map转换成xml的string
            String signedXml = WXPayUtil.generateSignedXml(requestMap, partnerkey);
            System.out.println("发送的参数==" + signedXml);
            //准备发送数据，参数是xml转换后的字符串
            httpClient.setXmlParam(signedXml);
            //发送请求
            httpClient.post();
            //接收参数
            String content = httpClient.getContent();
            System.out.println("接收微信返回值==" + content);
            //将string类型的xml转换成map类型
            Map<String, String> responseMap = WXPayUtil.xmlToMap(content);
            return responseMap;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

    }

    /**
     * 生成支付二维码
     *
     * @param out_trade_no 商品订单号
     * @param total_fee    商品金额
     * @return
     */
    @Override
    public Map createNative(String out_trade_no, String total_fee) {
        try {
            //准备发送请求
            HttpClient httpClient = new HttpClient("https://api.mch.weixin.qq.com/pay/unifiedorder");
            //设置参数
            Map requestMap = new HashMap();
            requestMap.put("appid", appid);//公众账号ID
            requestMap.put("mch_id", mch_id);//商户ID
            requestMap.put("nonce_str", WXPayUtil.generateUUID());//随机字符串32位
            // requestMap.put("sign", sign);//签名
            requestMap.put("body", "商城中的商品");//商品描述
            requestMap.put("out_trade_no", out_trade_no);//商户订单号
            requestMap.put("total_fee", total_fee);//标价金额
            requestMap.put("spbill_create_ip", "127.0.0.1");//终端
            requestMap.put("notify_url", notify_url);//通知地址
            requestMap.put("trade_type", "NATIVE");//交易类型
            //使用微信工具类中的generateSignedXML将map转换成xml的Stiring
            String signedXml = WXPayUtil.generateSignedXml(requestMap, partnerkey);
            System.out.println("发送请求内容==" + signedXml);
            //准备发送参数，需要参数的内容是xml后的string数据
            httpClient.setXmlParam(signedXml);
            //需要设置请求的协议
            httpClient.setHttps(true);
            //发送请求
            httpClient.post();
            //接收参数
            String content = httpClient.getContent();
            System.out.println("微信返回的==" + content);
            //将string类型的xml转换成map类型
            Map<String, String> map = WXPayUtil.xmlToMap(content);

            //再次封装一个新的map
            Map responseMap = new HashMap();
            responseMap.put("out_trade_no", out_trade_no);//支付订单号
            responseMap.put("total_fee", total_fee);//总金额
            responseMap.put("code_url", map.get("code_url"));//二维码链接

            return responseMap;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

    }
}
