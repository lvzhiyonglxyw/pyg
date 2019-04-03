package com.pinyougou.sellergoods.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.pyg.pyg_sellergoods_interface.SkuService;
import com.pyg_dao.mapper.TbGoodsMapper;
import com.pyg_dao.mapper.TbItemMapper;
import com.pyg_pojo.pojo.TbGoods;
import com.pyg_pojo.pojo.TbGoodsExample;
import com.pyg_pojo.pojo.TbItem;
import com.pyg_pojo.pojo.TbItemExample;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
public class SkuServiceImpl implements SkuService {

    @Autowired
    private TbGoodsMapper tbGoodsMapper;

    @Autowired
    private TbItemMapper tbItemMapper;


    /*
     * 获取sku数据
     * */
    @Override
    public List<TbItem> findGoodsSku(String sellId) {
        List<TbGoods> goodsList = findGoods(sellId);
        List<TbItem> list = new ArrayList<>();
        for (TbGoods tbGoods : goodsList) {
            TbItemExample example = new TbItemExample();
            TbItemExample.Criteria criteria = example.createCriteria();
            criteria.andGoodsIdEqualTo(tbGoods.getId());
            List<TbItem> itemsList = tbItemMapper.selectByExample(example);
            for (TbItem tbItem : itemsList) {
                list.add(tbItem);
            }
        }
        return list;
    }
    /*
     * 获取商品数据
     * */
    @Override
    public List<TbGoods> findGoods(String sellId) {
        TbGoodsExample example = new TbGoodsExample();
        TbGoodsExample.Criteria criteria = example.createCriteria();
        criteria.andSellerIdEqualTo(sellId);
        List<TbGoods> tbGoodsList = tbGoodsMapper.selectByExample(example);
        return tbGoodsList;
    }
}
