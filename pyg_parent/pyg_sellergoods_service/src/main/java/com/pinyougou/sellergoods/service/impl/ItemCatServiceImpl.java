package com.pinyougou.sellergoods.service.impl;

import java.util.List;

import com.pyg.pyg_sellergoods_interface.ItemCatService;
import com.pyg_dao.mapper.TbItemCatMapper;
import com.pyg_pojo.entity.PageResult;
import com.pyg_pojo.pojo.TbItem;
import com.pyg_pojo.pojo.TbItemCat;
import com.pyg_pojo.pojo.TbItemCatExample;
import org.springframework.beans.factory.annotation.Autowired;
import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.springframework.transaction.annotation.Transactional;

/**
 * 服务实现层
 *
 * @author Administrator
 */
@Service
@Transactional
public class ItemCatServiceImpl implements ItemCatService {

    @Autowired
    private TbItemCatMapper itemCatMapper;

    /**
     * 查询全部
     */
    @Override
    public List<TbItemCat> findAll() {
        return itemCatMapper.selectByExample(null);
    }

    /**
     * 按分页查询
     */
    @Override
    public List<TbItemCat> findPage(Long id) {

        TbItemCatExample example = new TbItemCatExample();
        TbItemCatExample.Criteria criteria = example.createCriteria();
        criteria.andParentIdEqualTo(id);
        return itemCatMapper.selectByExample(example);
    }

    /**
     * 增加
     */
    @Override
    public void add(TbItemCat itemCat) {
        itemCatMapper.insert(itemCat);
    }


    /**
     * 修改
     */
    @Override
    public void update(TbItemCat itemCat) {
        itemCatMapper.updateByPrimaryKey(itemCat);
    }

    /**
     * 根据ID获取实体
     *
     * @param id
     * @return
     */
    @Override
    public TbItemCat findOne(Long id) {
        return itemCatMapper.selectByPrimaryKey(id);
    }

    /**
     * 批量删除
     */
    @Override
    public void delete(Long[] ids) {
        for (Long id : ids) {
            itemCatMapper.deleteByPrimaryKey(id);
        }
    }
	
	
/*		@Override
	public PageResult findPage(TbItemCat itemCat, int pageNum, int pageSize) {

		return new PageResult(page.getTotal(), page.getResult());
	}*/

}
