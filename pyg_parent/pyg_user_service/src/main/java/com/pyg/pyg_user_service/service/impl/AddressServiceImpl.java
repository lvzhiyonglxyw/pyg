package com.pyg.pyg_user_service.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.pyg.pyg_user_service.service.AddressService;
import com.pyg_dao.mapper.TbAddressMapper;
import com.pyg_pojo.pojo.TbAddress;
import com.pyg_pojo.pojo.TbAddressExample;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class AddressServiceImpl implements AddressService {

    @Autowired
    private TbAddressMapper tbAddressMapper;

    //根据用户名查询收货地址
    public List<TbAddress> findByUserId(String userId) {
        TbAddressExample example = new TbAddressExample();
        example.createCriteria().andUserIdEqualTo(userId);
        return tbAddressMapper.selectByExample(example);
    }
}
