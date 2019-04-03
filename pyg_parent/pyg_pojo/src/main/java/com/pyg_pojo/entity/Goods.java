package com.pyg_pojo.entity;

import com.pyg_pojo.pojo.TbGoods;
import com.pyg_pojo.pojo.TbGoodsDesc;
import com.pyg_pojo.pojo.TbItem;

import java.io.Serializable;
import java.util.List;

public class Goods implements Serializable {

    private TbGoods tbGoods;//货品表spu
    private TbGoodsDesc tbGoodsDesc;//货品描述表
    private List<TbItem> itemList;//商品表sku

    @Override
    public String toString() {
        return "Goods{" +
                "tbGoods=" + tbGoods +
                ", tbGoodsDesc=" + tbGoodsDesc +
                ", itemList=" + itemList +
                '}';
    }

    public Goods() {
    }

    public TbGoods getTbGoods() {

        return tbGoods;
    }

    public void setTbGoods(TbGoods tbGoods) {
        this.tbGoods = tbGoods;
    }

    public TbGoodsDesc getTbGoodsDesc() {
        return tbGoodsDesc;
    }

    public void setTbGoodsDesc(TbGoodsDesc tbGoodsDesc) {
        this.tbGoodsDesc = tbGoodsDesc;
    }

    public List<TbItem> getItemList() {
        return itemList;
    }

    public void setItemList(List<TbItem> itemList) {
        this.itemList = itemList;
    }

    public Goods(TbGoods tbGoods, TbGoodsDesc tbGoodsDesc, List<TbItem> itemList) {

        this.tbGoods = tbGoods;
        this.tbGoodsDesc = tbGoodsDesc;
        this.itemList = itemList;
    }
}
