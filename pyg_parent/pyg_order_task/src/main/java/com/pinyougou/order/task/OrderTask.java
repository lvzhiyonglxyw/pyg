package com.pinyougou.order.task;

import com.pyg_dao.mapper.TbOrderMapper;
import com.pyg_dao.mapper.TbSeckillOrderMapper;
import com.pyg_pojo.pojo.TbOrder;
import com.pyg_pojo.pojo.TbOrderExample;
import com.pyg_pojo.pojo.TbSeckillOrder;
import com.pyg_pojo.pojo.TbSeckillOrderExample;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;

@Component
public class OrderTask {

    @Autowired
    private TbOrderMapper tbOrderMapper;

    @Autowired
    private TbSeckillOrderMapper tbSeckillOrderMapper;

    //一分钟执行一次普通订单
    @Scheduled(cron = "0/1 * * * * ?")
    public void CronTask() {
        TbOrderExample example = new TbOrderExample();
        //创建时间小于等于当前时间，30分钟小于等于创建时间
        example.createCriteria()
                //失效时间大于等于当前时间
                .andExpireLessThanOrEqualTo(new Date())
                //支付状态为1
                .andStatusEqualTo("1")
                //支付类型为1payment_type
                .andPaymentTypeEqualTo("1");
        List<TbOrder> tbOrders = tbOrderMapper.selectByExample(example);
        for (TbOrder tbOrder : tbOrders) {
            tbOrder.setStatus("6");
            tbOrder.setCloseTime(new Date());
            tbOrderMapper.updateByPrimaryKey(tbOrder);
        }
    }

    //一分钟执行一次秒杀订单
    @Scheduled(cron = "0/1 * * * * ?")
    public void SeckillCronTask() {
        long currentTime = System.currentTimeMillis() + 30 * 60 * 1000;
        Date date = new Date(currentTime);
        TbSeckillOrderExample example = new TbSeckillOrderExample();
        //创建时间小于等于当前时间，
        example.createCriteria()
                //失效时间大于等于当前时间
                .andExpireLessThanOrEqualTo(new Date())
                //支付状态为1
                .andStatusEqualTo("0");
        List<TbSeckillOrder> tbSeckillOrders = tbSeckillOrderMapper.selectByExample(example);
        for (TbSeckillOrder tbSeckillOrder : tbSeckillOrders) {
            tbSeckillOrder.setStatus("6");
            tbSeckillOrder.setCloseTime(new Date());
            tbSeckillOrderMapper.updateByPrimaryKey(tbSeckillOrder);
        }
    }

}
