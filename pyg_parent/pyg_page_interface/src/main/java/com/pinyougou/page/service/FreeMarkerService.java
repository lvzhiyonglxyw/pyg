package com.pinyougou.page.service;

public interface FreeMarkerService {
    //根据商品di生产静态页面
    public abstract void importTemplate(Long goodsId);

    //根据商品id删除静态页面
    public abstract void removeTemplate(Long goodsId);
}
