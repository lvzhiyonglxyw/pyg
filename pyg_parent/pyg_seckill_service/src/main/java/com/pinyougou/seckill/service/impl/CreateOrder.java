package com.pinyougou.seckill.service.impl;

import com.pyg_dao.mapper.TbSeckillGoodsMapper;
import com.pyg_dao.mapper.TbSeckillOrderMapper;
import com.pyg_pojo.pojo.TbSeckillGoods;
import com.pyg_pojo.pojo.TbSeckillOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

@Component
public class CreateOrder implements Runnable {

    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private TbSeckillOrderMapper tbSeckillOrderMapper;

    @Autowired
    private TbSeckillGoodsMapper tbSeckillGoodsMapper;

    @Override
    public void run() {
        //1、获取redis中的数据
        TbSeckillOrder tbSeckillOrder = (TbSeckillOrder) redisTemplate.boundListOps("seckill_order").rightPop();
        //2、向数据库中添加数据
        tbSeckillOrderMapper.insert(tbSeckillOrder);
        //3、修改秒杀商品表中的商品数量-1
        //3.1查询秒杀表
        TbSeckillGoods tbSeckillGoods = tbSeckillGoodsMapper.selectByPrimaryKey(tbSeckillOrder.getSeckillId());
        tbSeckillGoods.setStockCount(tbSeckillGoods.getStockCount() - 1);
        //修改数据到数据库
        tbSeckillGoodsMapper.updateByPrimaryKey(tbSeckillGoods);
    }
}
