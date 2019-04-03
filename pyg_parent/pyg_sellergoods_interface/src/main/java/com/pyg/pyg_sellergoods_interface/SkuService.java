package com.pyg.pyg_sellergoods_interface;

import com.pyg_pojo.pojo.TbGoods;
import com.pyg_pojo.pojo.TbItem;

import java.util.List;

public interface SkuService {

    //获取sku数据
    public abstract List<TbItem> findGoodsSku(String sellId);

    //获取商品数据
    public abstract List<TbGoods> findGoods(String sellId);
}
