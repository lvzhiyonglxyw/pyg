package com.pyg.pyg_seckill_web;

import com.alibaba.dubbo.config.annotation.Reference;
import com.pinyougou.seckill.service.SeckillGoodsService;
import com.pinyougou.seckill.service.SeckillOrderService;
import com.pyg_pojo.entity.Result;
import com.pyg_pojo.pojo.TbSeckillGoods;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/seckillGoodsController")
public class SeckillGoodsController {

    @Reference
    private SeckillGoodsService seckillGoodsService;
    @Reference
    private SeckillOrderService seckillOrderService;

    /**
     * 获取当前用户信息
     *
     * @return
     */
    @RequestMapping("/showName")
    public Map showName() {
        String name = SecurityContextHolder.getContext().getAuthentication().getName();
        Map<String, String> map = new HashMap<>();
        map.put("name", name);
        return map;
    }

    /**
     * 添加秒杀订单
     *
     * @param id
     * @return
     */
    @RequestMapping("/saveSeckillOrder")
    public Result saveSeckillOrder(String id) {
        try {
            String userId = SecurityContextHolder.getContext().getAuthentication().getName();
            if ("anonymousUser".equals(userId)) {
                return new Result(false, "请先登录在下单！！");
            }
            seckillOrderService.saveSeckillOrder(id, userId);
            return new Result(true, "抢单成功");
        } catch (RuntimeException e) {
            e.printStackTrace();
            return new Result(false, e.getMessage());
        } catch (Exception e) {
            return new Result(false, "抢单失败");
        }
    }

    /**
     * 根据秒杀商品id查询
     *
     * @param id
     * @return
     */
    @RequestMapping("/findOne")
    public TbSeckillGoods findOne(Long id) {

        return seckillGoodsService.findOne(id);
    }

    /**
     * 从redis中获取全部秒杀 商品信息
     *
     * @return
     */
    @RequestMapping("/findSeckillList")
    public List<TbSeckillGoods> findSeckillList() {

        return seckillGoodsService.finAll();

    }
}
