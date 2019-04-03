package com.pinyougou.manager.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.pinyougou.content.service.PromotionService;
import com.pyg_pojo.entity.PageResult;
import com.pyg_pojo.entity.Result;
import com.pyg_pojo.pojo.TbPromotion;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/promotionController")
public class PromotionController {

    @Reference
    private PromotionService promotionService;

    /**
     * 添加活动信息
     *
     * @param tbPromotion
     * @return
     */
    @RequestMapping("/add")
    public Result add(@RequestBody TbPromotion tbPromotion) {
        try {
            promotionService.add(tbPromotion);
            return new Result(true, "添加成功");
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, "添加失败");
        }
    }

    /**
     * 修改
     *
     * @param promotion
     * @return
     */
    @RequestMapping("/update")
    public Result update(@RequestBody TbPromotion promotion) {
        try {
            promotionService.update(promotion);
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
    @RequestMapping("/findOne")
    public TbPromotion findOne(String id) {
        return promotionService.findOne(id);
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
            promotionService.delete(ids);
            return new Result(true, "删除成功");
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, "删除失败");
        }
    }

    /**
     * 查询+分页
     *
     * @param promotion
     * @param pageNum
     * @param pageSize
     * @return
     */
    @RequestMapping("/search")
    public PageResult search(@RequestBody TbPromotion promotion, int pageNum, int pageSize) {
        return promotionService.findPage(pageNum, pageSize, promotion);
    }

}
