package com.pinyougou.cart.controlle;

import com.alibaba.dubbo.config.annotation.Reference;
import com.pinyougou.pay.service.WeiXinPayService;
import com.pyg_pojo.entity.Result;
import com.pyg_pojo.pojo.TbPayLog;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/payController")
public class PayController {

    @Reference
    private WeiXinPayService weiXinPayService;

    //显示用户信息
    @RequestMapping("/showName")
    public Map showName() {
        Map<String, String> map = new HashMap<>();
        String name = SecurityContextHolder.getContext().getAuthentication().getName();
        map.put("name", name);
        return map;
    }

    /**
     * 查询订单状态
     *
     * @param out_trade_no
     * @return
     */
    @RequestMapping("/queryPayStatus")
    public Result queryPayStatus(String out_trade_no) {
        int time = 0;
        while (true) {
            Map map = weiXinPayService.queryPayStatus(out_trade_no);
            if (map == null) {
                System.out.println("支付失败");
                return new Result(false, "支付失败");
            }
            if ("SUCCESS".equals(map.get("trade_state"))) {
                //付款成功
                System.out.println("支付成功");
                weiXinPayService.updateOrderStatus(out_trade_no,map.get("transaction_id").toString());
                return new Result(true, "付款成功");
            }
            if (time > 6) {
                System.out.println("支付超时");
                return new Result(false, "timeout");
            }
            time++;
            try {
                //设置调用时间
                Thread.sleep(10000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 生成支付二维码
     *
     * @return
     */
    @RequestMapping("/createNative")
    public Map createNative() {
        //String uuid = UUID.randomUUID().toString().replaceAll("-", "");
        //获取当前登录的用户名
        String name = SecurityContextHolder.getContext().getAuthentication().getName();
        //根据用户名获取redis中的数据
        TbPayLog tbPayLog = weiXinPayService.findByUserId(name);
        //判断redis中是否有数据
        if (tbPayLog != null) {
            System.out.print("总金额：" + tbPayLog.getTotalFee());
            //根据订单号生成二维码
            return weiXinPayService.createNative(tbPayLog.getOutTradeNo(), "1");
        }
        return null;
    }
}
