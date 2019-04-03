package com.pinyougou.seckill.service;

import com.pyg_pojo.entity.PageResult;
import com.pyg_pojo.pojo.TbSeckillOrder;

import java.util.List;

public interface SeckillOrderService {
    /**
     * 返回全部列表
     * @return
     */
    public List<TbSeckillOrder> findAll();


    /**
     * 返回分页列表
     * @return
     */
    public PageResult findPage(int pageNum, int pageSize);

    /**
     * 根据ID获取实体
     * @param id
     * @return
     */
    public TbSeckillOrder findOne(String id);

    /**
     * 分页
     * @param pageNum 当前页 码
     * @param pageSize 每页记录数
     * @return
     */
    public PageResult findPage(TbSeckillOrder seckillOrder, int pageNum, int pageSize);
    /**
     * 添加秒杀订单
     *
     * @param id
     */
    public abstract void saveSeckillOrder(String id,String userId);

    /**
     * 查询当前商家的秒杀订单信息+分页
     * @param name
     * @param page
     * @param rows
     * @return
     */
    public abstract PageResult findPage(String name, int page, int rows);

    /**
     * 删除订单信息
     * @param ids
     */
    public abstract void delete(String[] ids);

    /**
     * 根据秒杀订单商品id查询
     * @param id
     * @return
     */
    public abstract TbSeckillOrder findById(String id);

    /**
     * 修改订单商品信息
     * @param seckillOrder
     */
    public abstract void update(TbSeckillOrder seckillOrder);

    /**
     * 添加秒杀订单
     * @param seckillOrder
     */
    public abstract void add(TbSeckillOrder seckillOrder);
}
