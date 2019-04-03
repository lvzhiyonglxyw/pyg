package com.pinyougou.shop.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.github.pagehelper.PageInfo;
import com.pinyougou.order.service.OrderService;
import com.pyg_pojo.entity.Order;
import com.pyg_pojo.entity.PageResult;
import com.pyg_pojo.pojo.TbOrder;
import org.springframework.security.core.context.SecurityContextHolder;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/orderController")
public class OrderController {
    @Reference
    private OrderService orderService;
    //分页条件查询
    @RequestMapping("/findAll")
    public List<Order> findAll(@RequestBody Order order) {
        String sellerId = SecurityContextHolder.getContext().getAuthentication().getName();
        order.setSellerId(sellerId);
        List<Order> list = orderService.findAll(order);
        return list;
    }

}
