package com.pinyougou.seckill.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.pinyougou.seckill.service.SeckillOrderService;
import com.pinyougou.utils.IdWorker;
import com.pyg_dao.mapper.TbSeckillOrderMapper;
import com.pyg_pojo.entity.PageResult;
import com.pyg_pojo.pojo.TbSeckillGoods;
import com.pyg_pojo.pojo.TbSeckillOrder;
import com.pyg_pojo.pojo.TbSeckillOrderExample;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

@Service
@Transactional
public class SeckillOrderServiceImpl implements SeckillOrderService {


    @Autowired
    private TbSeckillOrderMapper tbSeckillOrderMapper;

    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private IdWorker idWorker;

    @Autowired
    private CreateOrder createOrder;

    @Autowired
    private ThreadPoolTaskExecutor threadPoolTaskExecutor;

    @Autowired
    private IdWorker idWorker1;

    /**
     * 提交订单
     *
     * @param id
     */
    @Override
    public void saveSeckillOrder(String id, String userId) {
        //8、从redis,根据用户id查询并判断此用户是否存在
        Boolean member = redisTemplate.boundSetOps("seckill_log_" + id).isMember(userId);
        if (member) {
            throw new RuntimeException("您已经购买过此商品！！不能重复够买");
        }
        //9、解决超卖问题
        Object obj = redisTemplate.boundListOps("seckill_goods_queue_" + id).rightPop();
        if (obj == null) {
            throw new RuntimeException("秒杀活动结束");
        }
        //1、先根据商品id从redis中查询数据
        TbSeckillGoods seckillGoods = (TbSeckillGoods) redisTemplate.boundHashOps("seckillGoods").get(id);
        //2、判断从redis中查询到的数据是否为空或商品数量是否等于0
        if (seckillGoods == null || seckillGoods.getStockCount() == 0) {
            throw new RuntimeException("秒杀活动结束");
        }
        //3、给redis中的商品数量-1并归还
        seckillGoods.setStockCount(seckillGoods.getStockCount() - 1);
        redisTemplate.boundHashOps("seckillGoods").put(id, seckillGoods);
        //6、代表商品抢购成功（一个商品一个set类型添加到redis,）
        redisTemplate.boundSetOps("seckill_log_" + id).add(userId);
        //7、商品数量为0从redis中删除商品
        if (seckillGoods.getStockCount() == 0) {
            redisTemplate.boundHashOps("seckillGoods").delete(id);
        }
        //4、向秒杀订单表中添加数据
        TbSeckillOrder tbSeckillOrder = new TbSeckillOrder();
        tbSeckillOrder.setCreateTime(new Date());
        tbSeckillOrder.setId(String.valueOf(idWorker.nextId()));
        tbSeckillOrder.setMoney(seckillGoods.getCostPrice());
        tbSeckillOrder.setSellerId(seckillGoods.getSellerId());
        tbSeckillOrder.setStatus("0");
        tbSeckillOrder.setSeckillId(seckillGoods.getId());
        tbSeckillOrder.setUserId(userId);
        long currentTime = System.currentTimeMillis() + 5 * 60 * 1000;
        Date date = new Date(currentTime);
        tbSeckillOrder.setExpire(date);//订单失效时间
        //添加订单成功跳转到购物车页面进行结算
        //5、把秒杀订单表中的数据放入到的redis中
        redisTemplate.boundListOps("seckill_order").leftPush(tbSeckillOrder);
        threadPoolTaskExecutor.execute(createOrder);//在子线程中操作数据库
    }

    /**
     * 查询当前商家的秒杀订单信息+分页
     *
     * @param name
     * @param page
     * @param rows
     * @return
     */
    @Override
    public PageResult findPage(String name, int page, int rows) {
        PageHelper.startPage(page, rows);
        TbSeckillOrderExample example = new TbSeckillOrderExample();
        example.createCriteria().andSellerIdEqualTo(name);
        Page<TbSeckillOrder> pages = (Page<TbSeckillOrder>) tbSeckillOrderMapper.selectByExample(example);
        return new PageResult(pages.getTotal(), pages.getResult());
    }

    /**
     * 删除订单信息
     *
     * @param ids
     */
    @Override
    public void delete(String[] ids) {
        for (String id : ids) {
            tbSeckillOrderMapper.deleteByPrimaryKey(id);
        }
    }

    /**
     * 根据秒杀订单商品id查询
     *
     * @param id
     * @return
     */
    @Override
    public TbSeckillOrder findById(String id) {
        return tbSeckillOrderMapper.selectByPrimaryKey(id);
    }

    /**
     * 修改订单商品信息
     *
     * @param seckillOrder
     */
    @Override
    public void update(TbSeckillOrder seckillOrder) {
        tbSeckillOrderMapper.updateByPrimaryKey(seckillOrder);
    }

    /**
     * 添加秒杀订单
     *
     * @param seckillOrder
     */
    @Override
    public void add(TbSeckillOrder seckillOrder) {
        long nextId = idWorker1.nextId();
        seckillOrder.setId(String.valueOf(nextId));
        if (seckillOrder.getStatus()==null){
            seckillOrder.setStatus("0");
        }
        seckillOrder.setCreateTime(new Date());
        tbSeckillOrderMapper.insert(seckillOrder);
    }


    /**
     * 查询全部
     */
    @Override
    public List<TbSeckillOrder> findAll() {
        return tbSeckillOrderMapper.selectByExample(null);
    }

    /**
     * 按分页查询
     */
    @Override
    public PageResult findPage(int pageNum, int pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        Page<TbSeckillOrder> page = (Page<TbSeckillOrder>) tbSeckillOrderMapper.selectByExample(null);
        return new PageResult(page.getTotal(), page.getResult());
    }

    /**
     * 根据ID获取实体
     *
     * @param id
     * @return
     */
    @Override
    public TbSeckillOrder findOne(String id) {
        return tbSeckillOrderMapper.selectByPrimaryKey(id);
    }

    /**
     * 分页查询全部商家订单
     * @param seckillOrder
     * @param pageNum 当前页 码
     * @param pageSize 每页记录数
     * @return
     */
    @Override
    public PageResult findPage(TbSeckillOrder seckillOrder, int pageNum, int pageSize) {
        PageHelper.startPage(pageNum, pageSize);

        TbSeckillOrderExample example = new TbSeckillOrderExample();
        TbSeckillOrderExample.Criteria criteria = example.createCriteria();

        if (seckillOrder != null) {
            if (seckillOrder.getUserId() != null && seckillOrder.getUserId().length() > 0) {
                criteria.andUserIdLike("%" + seckillOrder.getUserId() + "%");
            }
            if (seckillOrder.getSellerId() != null && seckillOrder.getSellerId().length() > 0) {
                criteria.andSellerIdLike("%" + seckillOrder.getSellerId() + "%");
            }
            if (seckillOrder.getStatus() != null && seckillOrder.getStatus().length() > 0) {
                criteria.andStatusLike("%" + seckillOrder.getStatus() + "%");
            }
            if (seckillOrder.getReceiverAddress() != null && seckillOrder.getReceiverAddress().length() > 0) {
                criteria.andReceiverAddressLike("%" + seckillOrder.getReceiverAddress() + "%");
            }
            if (seckillOrder.getReceiverMobile() != null && seckillOrder.getReceiverMobile().length() > 0) {
                criteria.andReceiverMobileLike("%" + seckillOrder.getReceiverMobile() + "%");
            }
            if (seckillOrder.getReceiver() != null && seckillOrder.getReceiver().length() > 0) {
                criteria.andReceiverLike("%" + seckillOrder.getReceiver() + "%");
            }
            if (seckillOrder.getTransactionId() != null && seckillOrder.getTransactionId().length() > 0) {
                criteria.andTransactionIdLike("%" + seckillOrder.getTransactionId() + "%");
            }

        }

        Page<TbSeckillOrder> page = (Page<TbSeckillOrder>) tbSeckillOrderMapper.selectByExample(example);
        return new PageResult(page.getTotal(), page.getResult());
    }
}
