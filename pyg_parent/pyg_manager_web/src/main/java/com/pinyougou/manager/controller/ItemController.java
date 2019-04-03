package com.pinyougou.manager.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.pyg.pyg_sellergoods_interface.ItemService;
import com.pyg_pojo.pojo.TbItem;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
@SuppressWarnings("all")
@RestController
@RequestMapping("/itemController")
public class ItemController {

    @Reference
    private ItemService itemService;

    @RequestMapping("/findByGoodsId")
    public List<TbItem> findByGoodsId(Long id) {
        return itemService.findByGoodsId(id);
    }
}
