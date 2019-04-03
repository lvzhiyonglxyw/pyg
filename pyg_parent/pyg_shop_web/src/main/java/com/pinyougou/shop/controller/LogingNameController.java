package com.pinyougou.shop.controller;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/logingNameController")
public class LogingNameController {


    @RequestMapping("/showName")
    public String showName() {
        String name = SecurityContextHolder.getContext().getAuthentication().getName();
        return name;
    }

}
