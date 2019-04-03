package com.pinyougou.seckill.task;

import com.pyg_dao.mapper.TbSeckillGoodsMapper;
import com.pyg_dao.mapper.TbSeckillOrderMapper;
import com.pyg_pojo.pojo.TbSeckillGoods;
import com.pyg_pojo.pojo.TbSeckillGoodsExample;
import com.pyg_pojo.pojo.TbSeckillOrder;
import com.pyg_pojo.pojo.TbSeckillOrderExample;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.scheduling.annotation.Schedules;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;

@Component
public class SeckillTask {

    @Autowired
    private TbSeckillGoodsMapper tbSeckillGoodsMapper;

    @Autowired
    private RedisTemplate redisTemplate;

    @Scheduled(cron = "0/5 * * * * ?")
    public void CronTask() {
        //查询开始时间<=当前时间<=结束日期,审核状态为1,商品数量大于等于1
        TbSeckillGoodsExample example = new TbSeckillGoodsExample();
        example.createCriteria()
                //开始时间小于等于当前时间
                .andStartTimeLessThanOrEqualTo(new Date())
                //结束时间大于等于当前时间
                .andEndTimeGreaterThanOrEqualTo(new Date())
                //商品审核状态为1
                .andStatusEqualTo("1")
                .andStockCountGreaterThanOrEqualTo(1);
        List<TbSeckillGoods> tbSeckillGoods = tbSeckillGoodsMapper.selectByExample(example);

        for (TbSeckillGoods tbSeckillGood : tbSeckillGoods) {
            //根据秒杀商品id存入到redis
            redisTemplate.boundHashOps("seckillGoods").put(tbSeckillGood.getId(), tbSeckillGood);

            //解决超卖问题
            //把一个个商品根据数量一个一个的push到redis中
            for (int i = 0; i < tbSeckillGood.getStockCount(); i++) {
                redisTemplate.boundListOps("seckill_goods_queue_" + tbSeckillGood.getId()).leftPush(tbSeckillGood.getId());
            }
        }
        System.out.println("存入redis商品个数：" + tbSeckillGoods.size());
    }
}
