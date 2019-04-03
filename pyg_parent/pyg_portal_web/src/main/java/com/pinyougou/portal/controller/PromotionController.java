package com.pinyougou.portal.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.pinyougou.content.service.PromotionService;
import com.pyg_pojo.pojo.TbPromotion;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/promotionController")
public class PromotionController {

    @Reference
    private PromotionService promotionService;

    /**
     * 根据状态查询
     *
     * @return
     */
    @RequestMapping("/findStatus")
    public List<TbPromotion> findStatus() {

        return promotionService.findStatus();
    }
}
