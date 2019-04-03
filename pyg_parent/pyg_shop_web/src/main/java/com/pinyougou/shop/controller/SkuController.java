package com.pinyougou.shop.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.pyg.pyg_sellergoods_interface.SkuService;
import com.pyg_pojo.pojo.TbItem;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/sku")
public class SkuController {

    @Reference
    private SkuService skuService;


    @RequestMapping("/findGoodsSku")
    public List<TbItem> findGoodsSku(){
        //获取商家id
        String sellId = SecurityContextHolder.getContext().getAuthentication().getName();
        //System.out.println("哈哈哈哈哈"+sellId);
        //查询该id下所有的商品
        List<TbItem> tbItemsList= skuService.findGoodsSku(sellId);
        //返回前端数据
        return tbItemsList;
    }

}