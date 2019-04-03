package com.pinyougou.sellergoods.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.pyg.pyg_sellergoods_interface.ItemService;
import com.pyg_dao.mapper.TbItemMapper;
import com.pyg_pojo.pojo.TbItem;
import com.pyg_pojo.pojo.TbItemExample;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class ItemServiceImpl implements ItemService {

    @Autowired
    private TbItemMapper tbItemMapper;

    /**
     * 根据spuid查询sku信息
     *
     * @param goodsId
     * @return
     */
    @Override
    public List<TbItem> findByGoodsId(Long goodsId) {
        TbItemExample example = new TbItemExample();
        example.createCriteria().andGoodsIdEqualTo(goodsId);
        return tbItemMapper.selectByExample(example);
    }
}
