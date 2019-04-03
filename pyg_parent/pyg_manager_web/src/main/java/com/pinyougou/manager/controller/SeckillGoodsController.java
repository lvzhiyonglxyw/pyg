package com.pinyougou.manager.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.pinyougou.seckill.service.SeckillGoodsService;
import com.pyg_pojo.entity.PageResult;
import com.pyg_pojo.entity.Result;
import com.pyg_pojo.pojo.TbSeckillGoods;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/seckillGoodsController")
@SuppressWarnings("all")
public class SeckillGoodsController {

    @Reference
    private SeckillGoodsService seckillGoodsService;


    /*
     * 修改秒杀商品审核状态
     *
     * @param tbSeckillGoods
     * @return
     */
    @RequestMapping("/updateStatus")
    public Result updateStatus(Long[] ids, String status) {
        try {
            seckillGoodsService.updateStatus(ids, status);
            return new Result(true, "修改成功");
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, "修改失败");
        }
    }

    /**
     * 根据秒杀商品id查询商品信息
     *
     * @param id
     * @return
     */
    @RequestMapping("/findOne")
    public TbSeckillGoods findOne(Long id) {
        return seckillGoodsService.findById(id);
    }

    /**
     * 删除秒杀商品信息
     *
     * @param tbSeckillGoods
     * @return
     */
    @RequestMapping("/delete")
    public Result delete(Long[] ids) {
        try {
            seckillGoodsService.delete(ids);
            return new Result(true, "删除成功");
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, "删除失败");
        }
    }

    /**
     * 修改秒杀商品信息
     *
     * @param tbSeckillGoods
     * @return
     */
    @RequestMapping("/update")
    public Result update(@RequestBody TbSeckillGoods tbSeckillGoods) {
        try {
            String name = SecurityContextHolder.getContext().getAuthentication().getName();
            tbSeckillGoods.setSellerId(name);
            seckillGoodsService.update(tbSeckillGoods);
            return new Result(true, "修改成功");
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, "修改失败");
        }
    }

    /**
     * 添加秒杀商品信息
     *
     * @param tbSeckillGoods
     * @return
     */
    @RequestMapping("/add")
    public Result add(@RequestBody TbSeckillGoods tbSeckillGoods) {
        try {
            seckillGoodsService.add(tbSeckillGoods,"y");
            return new Result(true, "添加成功");
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, "添加失败");
        }
    }

    /**
     * 根据当前商家id查询秒杀商品
     *
     * @return
     */
    @RequestMapping("/finBySellerId")
    public PageResult finBySellerId(Integer pageNum, Integer pageSize) {
        String name = SecurityContextHolder.getContext().getAuthentication().getName();
        return seckillGoodsService.finBySellerId(name, pageNum, pageSize);
    }

    /**
     * 查询全部秒杀商品
     *
     * @return
     */
    @RequestMapping("/findAll")
    public List<TbSeckillGoods> findAll() {

        return seckillGoodsService.findAll();
    }

    /**
     * 运营商查询未审核秒杀商品
     *
     * @return
     */
    @RequestMapping("/findManagerAll")
    public PageResult findManagerAll(Integer pageNum, Integer pageSize) {

        return seckillGoodsService.findManagerAll(pageNum, pageSize);
    }

}
