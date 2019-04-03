package com.pyg.pyg_sellergoods_interface;

import com.pyg_pojo.pojo.TbItem;

import java.util.List;

public interface ItemService {
    /**
     * 根据spuid查询sku信息
     *
     * @param goodsId
     * @return
     */
    public List<TbItem> findByGoodsId(Long goodsId);
}
