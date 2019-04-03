package com.itheima;

import com.github.pagehelper.Page;
import com.pyg_dao.mapper.TbBrandMapper;
import com.pyg_dao.mapper.TbItemCatMapper;
import com.pyg_dao.mapper.TbOrderMapper;
import com.pyg_dao.mapper.TbSeckillOrderMapper;
import com.pyg_pojo.pojo.*;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class Test1 {

    @Test
    public void test01(){
        long currentTime = System.currentTimeMillis() + 5 * 60 * 1000;
        Date date = new Date(currentTime);
        System.out.println(date);
    }
   /* @Test
    public void saveSeckillOrder() {
        ApplicationContext applicationContext = new ClassPathXmlApplicationContext("spring/applicationContext-dao.xml");
        TbSeckillOrderMapper tssm = applicationContext.getBean(TbSeckillOrderMapper.class);
        TbSeckillOrder tbSeckillOrder = new TbSeckillOrder();
        tbSeckillOrder.setCreateTime(new Date());
        tbSeckillOrder.setId("123123123456");
        tbSeckillOrder.setMoney(new BigDecimal(213));
        tbSeckillOrder.setSellerId("lxyw");
        tbSeckillOrder.setStatus("0");
        tbSeckillOrder.setSeckillId(123L);
        tbSeckillOrder.setUserId("lxyw");

        tbSeckillOrder.setExpire(date);//订单失效时间
        tssm.insert(tbSeckillOrder);
    }*/

    @Test
    public void save() {
        long currentTime = System.currentTimeMillis() + 30 * 60 * 1000;
        Date date = new Date(currentTime);
        ApplicationContext applicationContext = new ClassPathXmlApplicationContext("spring/applicationContext-dao.xml");
        TbOrderMapper tssm = applicationContext.getBean(TbOrderMapper.class);
        TbOrder order = new TbOrder();
        order.setOrderId(1234L);//订单id
        order.setPaymentType("1");//支付类型
        order.setStatus("1");//支付状态'状态：1、未付款，2、已付款，3、未发货，4、已发货，5、交易成功，6、交易关闭,7、待评价',
        order.setCreateTime(new Date());//创建时间
        order.setUpdateTime(new Date());//修改时间
        order.setUserId("123");//用户id
        order.setReceiverMobile("123");//联系人手机号
        order.setReceiver("1234");//联系人
        order.setSourceType("2");//订单来源：1:app端，2：pc端，3：M端，4：微信端，5：手机qq端',
        order.setSellerId("213");//商家id
        order.setReceiverAreaName("123");//地址
        order.setExpire(date);//订单失效时间
        tssm.insert(order);
        //2019-04-02 10:17:33
    }
}
