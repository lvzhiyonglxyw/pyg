package com.pinyougou.cart.controlle;

import com.alibaba.dubbo.config.annotation.Reference;
import com.pinyougou.cart.service.CartService;
import com.pyg_pojo.entity.Cart;
import com.pyg_pojo.pojo.TbOrderItem;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.UnsupportedEncodingException;
import java.util.List;

@RestController
@RequestMapping("orderItemController")
public class OrderItemController {

    @Reference
    private CartService cartService;

    /**
     * 从redis中获取数据
     *
     * @param sellerId
     * @param itemId
     * @return
     */
    @RequestMapping("/findBySellerIdAndItemId")
    public List<Cart> findBySellerIdAndItemId(String[] sellerId, String[] itemId) {
        String name = SecurityContextHolder.getContext().getAuthentication().getName();
        for (String seller : sellerId) {
            try {
                seller = new String(seller.getBytes("iso-8859-1"), "utf-8");
                return cartService.findBySellerIdAndItemId(name, seller, itemId);
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }
        return null;
    }
}
