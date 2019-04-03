package com.pinyougou.content.service;

import com.pyg_pojo.entity.PageResult;
import com.pyg_pojo.pojo.TbPromotion;

import java.util.List;

public interface PromotionService {

    //修改活动信息
    public abstract void update(TbPromotion promotion);

    //删除活动信息
    public abstract void delete(String[] ids);

    //条件分页查询
    public abstract PageResult findPage(Integer pageNum, Integer pageSize, TbPromotion tbPromotion);

    //添加活動信息
    public abstract void add(TbPromotion promotion);

    //按照id查询
    public abstract TbPromotion findOne(String id);

    //根据状态查询
    public abstract List<TbPromotion> findStatus();
}
