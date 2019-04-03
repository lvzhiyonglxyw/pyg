package com.pinyougou.cart.controlle;

import com.alibaba.dubbo.config.annotation.Reference;
import com.pyg.pyg_user_service.service.AddressService;
import com.pyg_pojo.pojo.TbAddress;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/addressController")
public class AddressController {
    @Reference
    private AddressService addressService;

    //根据用户名查询收货地址
    @RequestMapping("/findByUserId")
    public List<TbAddress> findByUserId() {
        String name = SecurityContextHolder.getContext().getAuthentication().getName();
        return addressService.findByUserId(name);
    }
}
