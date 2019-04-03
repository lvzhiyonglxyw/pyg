package com.pinyougou.search.service;

import java.util.Map;

public interface SeachItenService {

    /**
     * 使用solr查询
     */
    public abstract Map seachItem(Map map);

    /**
     * 根据商品id进行商品导入到solr库中
     *
     * @param ids
     */
    public abstract void importItem(Long[] ids);

    /**
     * 根据商品id从solr库中删除
     *
     * @param ids
     */
    public abstract void removeItem(Long[] ids);
}
