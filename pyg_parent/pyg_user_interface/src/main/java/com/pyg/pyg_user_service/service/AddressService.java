package com.pyg.pyg_user_service.service;

import com.pyg_pojo.pojo.TbAddress;

import java.util.List;

public interface AddressService {

    //根据用户名查询收货地址
    public abstract List<TbAddress> findByUserId(String userId);
}
