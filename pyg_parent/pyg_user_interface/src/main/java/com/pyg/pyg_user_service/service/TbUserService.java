package com.pyg.pyg_user_service.service;

import com.pyg_pojo.pojo.TbUser;

public interface TbUserService {
    //注册用户信息
    public abstract void save(TbUser tbUser);

    //发送短信
    public abstract void sendSms(String phone);

    //判断生成的密码和输入的密码是否相等
    public abstract int isCheck(String phone, String code);
}
