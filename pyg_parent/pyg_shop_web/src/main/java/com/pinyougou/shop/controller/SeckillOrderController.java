package com.pinyougou.shop.controller;

import java.util.List;

import com.pinyougou.seckill.service.SeckillOrderService;
import com.pyg_pojo.entity.PageResult;
import com.pyg_pojo.entity.Result;
import com.pyg_pojo.pojo.TbSeckillOrder;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.alibaba.dubbo.config.annotation.Reference;

/**
 * controller
 *
 * @author Administrator
 */
@RestController
@RequestMapping("/seckillOrder")
public class SeckillOrderController {

    @Reference
    private SeckillOrderService seckillOrderService;

    /**
     * 增加
     *
     * @param seckillOrder
     * @return
     */
    @RequestMapping("/add")
    public Result add(@RequestBody TbSeckillOrder seckillOrder) {
        try {
            String name = SecurityContextHolder.getContext().getAuthentication().getName();
            seckillOrder.setSellerId(name);
            seckillOrderService.add(seckillOrder);
            return new Result(true, "增加成功");
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, "增加失败");
        }
    }

    /**
     * 修改
     *
     * @param seckillOrder
     * @return
     */
    @RequestMapping("/update")
    public Result update(@RequestBody TbSeckillOrder seckillOrder) {
        try {
            String name = SecurityContextHolder.getContext().getAuthentication().getName();
            seckillOrder.setSellerId(name);
            seckillOrderService.update(seckillOrder);
            return new Result(true, "修改成功");
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, "修改失败");
        }
    }

    /**
     * 获取实体
     *
     * @param id
     * @return
     */
    @RequestMapping("/findById")
    public TbSeckillOrder findById(String id) {
        return seckillOrderService.findById(id);
    }

    /**
     * 批量删除
     *
     * @param ids
     * @return
     */
    @RequestMapping("/delete")
    public Result delete(String[] ids) {
        try {
            seckillOrderService.delete(ids);
            return new Result(true, "删除成功");
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, "删除失败");
        }
    }

    /**
     * 查询+分页
     *
     * @param pageNum
     * @param pageSize
     * @return
     */
    @RequestMapping("/search")
    public PageResult search(int pageNum, int pageSize) {
        String name = SecurityContextHolder.getContext().getAuthentication().getName();
        return seckillOrderService.findPage(name, pageNum, pageSize);
    }

}
