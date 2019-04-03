package com.pinyougou.cart.controlle;

import com.alibaba.dubbo.config.annotation.Reference;
import com.pinyougou.order.service.OrderService;
import com.pyg_pojo.entity.Result;
import com.pyg_pojo.pojo.TbOrder;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/orderController")
public class OrderController {
    @Reference
    private OrderService orderService;

    /**
     * 添加购物车
     * @param tbOrder
     * @return
     */
    @RequestMapping("/addCartOrder")
    public Result addCartOrder(@RequestBody TbOrder tbOrder) {
        try {
            String name = SecurityContextHolder.getContext().getAuthentication().getName();
            tbOrder.setUserId(name);
            orderService.addCartOrder(tbOrder);
            return new Result(true, "添加成功");
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, "添加失败");
        }
    }
}
