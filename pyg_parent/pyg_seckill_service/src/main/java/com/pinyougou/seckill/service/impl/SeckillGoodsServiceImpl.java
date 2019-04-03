package com.pinyougou.seckill.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.pinyougou.seckill.service.SeckillGoodsService;
import com.pyg_dao.mapper.TbSeckillGoodsMapper;
import com.pyg_pojo.entity.PageResult;
import com.pyg_pojo.pojo.TbBrand;
import com.pyg_pojo.pojo.TbSeckillGoods;
import com.pyg_pojo.pojo.TbSeckillGoodsExample;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

@Service
@Transactional
public class SeckillGoodsServiceImpl implements SeckillGoodsService {

    @Autowired
    private TbSeckillGoodsMapper tbSeckillGoodsMapper;

    @Autowired
    private RedisTemplate redisTemplate;

    /**
     * 修改秒杀商品审核状态
     * 商品状态为2的时候才可以提交审核，修改为0
     * 商品状态为0的时候才可以通过审核修改状态为1
     * 商品状态为0 的时候才可以未通过审核修改状态为3
     * 商品状态为3的时候可以提交审核
     *
     * @param ids
     */
    @Override
    public void updateStatus(Long[] ids, String status) {
        for (Long id : ids) {
            TbSeckillGoods tbSeckillGoods = tbSeckillGoodsMapper.selectByPrimaryKey(id);
            if ("2".equals(tbSeckillGoods.getStatus()) && "0".equals(status)) {
                //提交审核
                tbSeckillGoods.setStatus(status);
            } else if ("0".equals(tbSeckillGoods.getStatus()) && "1".equals(status)) {
                //审核通过
                tbSeckillGoods.setStatus(status);
            } else if ("0".equals(tbSeckillGoods.getStatus()) && "3".equals(status)) {
                //审核未通过
                tbSeckillGoods.setStatus(status);
            } else if ("3".equals(tbSeckillGoods.getStatus()) && "0".equals(status)) {
                //重新提交审核
                tbSeckillGoods.setStatus(status);
            }
            tbSeckillGoodsMapper.updateByPrimaryKey(tbSeckillGoods);
        }
    }

    /**
     * 查询全部秒杀商品
     *
     * @return
     */
    @Override
    public List<TbSeckillGoods> findAll() {
        return tbSeckillGoodsMapper.selectByExample(null);
    }

    /**
     * 运营商查询未审核秒杀商品
     *
     * @return
     */
    @Override
    public PageResult findManagerAll(Integer pageNum, Integer pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        TbSeckillGoodsExample example = new TbSeckillGoodsExample();
        example.createCriteria().andStatusEqualTo("0");
        Page<TbSeckillGoods> page = (Page<TbSeckillGoods>) tbSeckillGoodsMapper.selectByExample(example);
        return new PageResult(page.getTotal(), page.getResult());
    }

    /**
     * 添加秒杀商品信息
     *
     * @param tbSeckillGoods
     */
    @Override
    public void add(TbSeckillGoods tbSeckillGoods, String ys) {
        tbSeckillGoods.setCreateTime(new Date());//添加时间
        if (ys.equals("y")) {
            //运营商提交的秒杀商品状态默认为1
            tbSeckillGoods.setStatus("1");//审核通过
        } else {
            //商家提交的秒杀商品状态默认为2
            tbSeckillGoods.setStatus("2");//未提交审核
        }
        tbSeckillGoodsMapper.insert(tbSeckillGoods);
    }

    /**
     * 修改秒杀商品信息
     *
     * @param tbSeckillGoods
     */
    @Override
    public void update(TbSeckillGoods tbSeckillGoods) {
        tbSeckillGoods.setCreateTime(new Date());//添加时间
        tbSeckillGoodsMapper.updateByPrimaryKey(tbSeckillGoods);
    }

    /**
     * 删除秒杀商品信息
     *
     * @param ids
     */
    @Override
    public void delete(Long[] ids) {
        for (Long id : ids) {
            tbSeckillGoodsMapper.deleteByPrimaryKey(id);
        }
    }

    /**
     * 根据秒杀商品id查询
     *
     * @param id
     * @return
     */
    @Override
    public TbSeckillGoods findById(Long id) {
        return tbSeckillGoodsMapper.selectByPrimaryKey(id);
    }

    /**
     * 根据秒杀商品id从redis中查询
     *
     * @param id
     * @return
     */
    @Override
    public TbSeckillGoods findOne(Long id) {
        return (TbSeckillGoods) redisTemplate.boundHashOps("seckillGoods").get(id);
    }

    /**
     * 从redis中获取全部秒杀 商品信息
     *
     * @return
     */
    @Override
    public List<TbSeckillGoods> finAll() {
        //获取全部商品
        return redisTemplate.boundHashOps("seckillGoods").values();
    }

    /**
     * 根据当前商家id查询秒杀商品
     *
     * @param sellerId
     * @return
     */
    @Override
    public PageResult finBySellerId(String sellerId, Integer pageNum, Integer pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        TbSeckillGoodsExample example = new TbSeckillGoodsExample();
        example.createCriteria().andSellerIdEqualTo(sellerId);
        Page<TbSeckillGoods> page = (Page<TbSeckillGoods>) tbSeckillGoodsMapper.selectByExample(example);
        return new PageResult(page.getTotal(), page.getResult());
    }


}
