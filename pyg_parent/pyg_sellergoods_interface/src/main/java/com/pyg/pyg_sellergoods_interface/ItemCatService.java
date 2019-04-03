package com.pyg.pyg_sellergoods_interface;

import java.util.List;

import com.pyg_pojo.entity.PageResult;
import com.pyg_pojo.pojo.TbItem;
import com.pyg_pojo.pojo.TbItemCat;
import com.pyg_pojo.pojo.TbItemCatExample;

/**
 * 服务层接口
 *
 * @author Administrator
 */
public interface ItemCatService {


    /**
     * 返回全部列表
     *
     * @return
     */
    public List<TbItemCat> findAll();


    /**
     * 返回分页列表
     *
     * @return
     */
    public List<TbItemCat> findPage(Long id);


    /**
     * 增加
     */
    public void add(TbItemCat itemCat);


    /**
     * 修改
     */
    public void update(TbItemCat itemCat);


    /**
     * 根据ID获取实体
     *
     * @param id
     * @return
     */
    public TbItemCat findOne(Long id);


    /**
     * 批量删除
     *
     * @param ids
     */
    public void delete(Long[] ids);


    /**
     * 分页
     * @param pageNum 当前页 码
     * @param pageSize 每页记录数
     * @return
     */
    /*public PageResult findPage(TbItemCat itemCat, int pageNum, int pageSize);*/

}
