package com.pinyougou.seckill.service;

import com.pyg_pojo.entity.PageResult;
import com.pyg_pojo.pojo.TbSeckillGoods;

import java.util.List;

public interface SeckillGoodsService {

    /**
     * 添加秒杀商品信息
     *
     * @param tbSeckillGoods
     */
    public abstract void add(TbSeckillGoods tbSeckillGoods,String ys);

    /**
     * 修改秒杀商品信息
     *
     * @param tbSeckillGoods
     */
    public abstract void update(TbSeckillGoods tbSeckillGoods);

    /**
     * 删除秒杀商品信息
     *
     * @param ids
     */
    public abstract void delete(Long[] ids);

    /**
     * 根据秒杀商品id查询信息
     *
     * @param id
     * @return
     */
    public abstract TbSeckillGoods findById(Long id);

    /**
     * 根据秒杀商品id从redis中查询
     *
     * @param id
     * @return
     */
    public abstract TbSeckillGoods findOne(Long id);

    /**
     * 从redis中获取全部秒杀 商品信息
     *
     * @return
     */
    public abstract List<TbSeckillGoods> finAll();

    /**
     * 根据当前商家id查询秒杀商品
     *
     * @param sellerId
     * @return
     */
    public abstract PageResult finBySellerId(String sellerId, Integer pageNum, Integer pageSize);

    /**
     * 修改秒杀商品审核状态
     *
     * @param ids
     */
    public abstract void updateStatus(Long[] ids, String status);

    /**
     * 查询秒杀商品信息
     *
     * @return
     */
    public abstract List<TbSeckillGoods> findAll();
    /**
     * 运营商查询秒杀商品信息
     *
     * @return
     */
    public abstract PageResult findManagerAll(Integer pageNum,Integer pageSize);
}
