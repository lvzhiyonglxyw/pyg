package com.pyg.pyg_user_web;

import com.alibaba.dubbo.config.annotation.Reference;
import com.pinyougou.utils.PhoneFormatCheckUtils;
import com.pyg.pyg_user_service.service.TbUserService;
import com.pyg_pojo.entity.Result;
import com.pyg_pojo.pojo.TbUser;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

import static com.pinyougou.utils.PhoneFormatCheckUtils.isPhoneLegal;

@RestController
@RequestMapping("/tbUserController")
public class TbUserController {

    @Reference
    private TbUserService tbUserService;

    //获取登录用户名
    @RequestMapping("/showName")
    public Map showName() {
        String name = SecurityContextHolder.getContext().getAuthentication().getName();
        Map<String, String> map = new HashMap<>();
        map.put("name", name);
        return map;
    }

    //发送短信
    @CrossOrigin(origins="*",allowCredentials="true")
    @RequestMapping("/sendSms")
    public Result sendSms(String phone) {
        try {
            boolean flag = isPhoneLegal(phone);
            if (!flag) {
                return new Result(false, "手机号输入有误");
            }
            tbUserService.sendSms(phone);
            return new Result(true, "发送短信成功");
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, "发送短信失败");
        }
    }

    //注册用户信息
    @RequestMapping("/save")
    private Result save(@RequestBody TbUser tbUser, String code) {
        try {
            int check = tbUserService.isCheck(tbUser.getPhone(), code);
            if (check == 0) {
                return new Result(false, "验证码失效");
            }
            if (check == 1) {
                return new Result(false, "验证码输入错误");
            }
            tbUserService.save(tbUser);
            return new Result(true, "添加成功");
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, "添加失败");
        }
    }
}
