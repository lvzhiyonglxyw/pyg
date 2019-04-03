package com.pinyougou.sellergoods.service.impl;

import java.util.Date;
import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSON;
import com.pyg.pyg_sellergoods_interface.GoodsService;
import com.pyg_dao.mapper.*;
import com.pyg_pojo.entity.Goods;
import com.pyg_pojo.entity.PageResult;
import com.pyg_pojo.entity.Result;
import com.pyg_pojo.pojo.*;
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
public class GoodsServiceImpl implements GoodsService {

    @Autowired
    private TbGoodsMapper tbGoodsMapper;

    @Autowired
    private TbGoodsMapper goodsMapper;

    @Autowired
    private TbGoodsDescMapper tbGoodsDescMapper;

    @Autowired
    private TbItemMapper tbItemMapper;

    @Autowired
    private TbItemCatMapper tbItemCatMapper;

    @Autowired
    private TbBrandMapper tbBrandMapper;

    @Autowired
    private TbSellerMapper tbSellerMapper;

    //根据商品id查询
    @Override
    public Goods findOne(Long id) {
        Goods goods = new Goods();

        TbGoods tbGoods = tbGoodsMapper.selectByPrimaryKey(id);
        goods.setTbGoods(tbGoods);

        TbGoodsDesc tbGoodsDesc = tbGoodsDescMapper.selectByPrimaryKey(id);
        goods.setTbGoodsDesc(tbGoodsDesc);

        TbItemExample example = new TbItemExample();
        example.createCriteria().andGoodsIdEqualTo(id);
        List<TbItem> list = tbItemMapper.selectByExample(example);
        goods.setItemList(list);

        return goods;
    }

    /**
     * 查询全部
     */
    @Override
    public List<TbGoods> findAll() {
        return goodsMapper.selectByExample(null);
    }

    /**
     * 按分页查询
     */
    @Override
    public PageResult findPage(int pageNum, int pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        Page<TbGoods> page = (Page<TbGoods>) goodsMapper.selectByExample(null);
        return new PageResult(page.getTotal(), page.getResult());
    }

    /**
     * 修改商品状态
     * <p>
     * 商品状态为0进行提交审核，（别的操作不能做）
     * 商品状态为1和审核未通过可进行审核通过【和审核未通过】
     * 商品未通过审核，上架可以提交申请修改状态为1，运营商进行再次审核
     * 只有审核通过的才可以上架（审核通过状态为2，上架状态为4）
     * 只有上架的才可以下架（上架状态为4,下架状态为5）
     * 只有下架的才可以上架（下架状态为5,上架状态为4）
     * 商品关闭可以操作任何状态
     *
     * @param ids    商品id
     * @param status 商品状态
     */
    @Override
    public void updateStatus(Long[] ids, String status) {
        for (Long id : ids) {
            TbGoods tbGoods = goodsMapper.selectByPrimaryKey(id);

            if (!tbGoods.getAuditStatus().equals(status)) {
                if ("0".equals(tbGoods.getAuditStatus()) && "1".equals(status)) {  //商品状态为0进行审核修改为1
                    //赋值修改状态
                    System.out.println("修改成审核中状态");
                    tbGoods.setAuditStatus(status);
                    goodsMapper.updateByPrimaryKey(tbGoods);

                } else if ("3".equals(tbGoods.getAuditStatus()) && "1".equals(status)) {//商品未通过审核，上架可以提交申请修改状态为1，运营商进行再次审核
                    tbGoods.setAuditStatus(status);
                    goodsMapper.updateByPrimaryKey(tbGoods);
                } else if (("1".equals(tbGoods.getAuditStatus()) && "2".equals(status))) {//商品状态为1或商品状态为3，可通过审核修改状态为2
                    //赋值修改状态
                    System.out.println("修改成以审核状态");
                    tbGoods.setAuditStatus(status);
                    goodsMapper.updateByPrimaryKey(tbGoods);
                } else if ("1".equals(tbGoods.getAuditStatus()) && "3".equals(status)) {//商品状态为1可未通过审核修改状态为3
                    //赋值修改状态
                    System.out.println("修改成未通过审核状态");
                    tbGoods.setAuditStatus(status);
                    goodsMapper.updateByPrimaryKey(tbGoods);
                } else if ("2".equals(tbGoods.getAuditStatus()) && "4".equals(status)) {//商品状态为2，可商品上架状态为4
                    //赋值修改状态
                    System.out.println("修改成商品上架状态");
                    tbGoods.setAuditStatus(status);
                    goodsMapper.updateByPrimaryKey(tbGoods);
                } else if ("4".equals(tbGoods.getAuditStatus()) && "5".equals(status)) {//商品状态为4，可商品下架状态为5
                    //赋值修改状态
                    System.out.println("修改成商品下架状态");
                    tbGoods.setAuditStatus(status);
                    goodsMapper.updateByPrimaryKey(tbGoods);
                } else if ("5".equals(tbGoods.getAuditStatus()) && "4".equals(status)) {//商品状态为5，可商品上架状态为4
                    //赋值修改状态
                    System.out.println("修改成商品上架状态");
                    tbGoods.setAuditStatus(status);
                    goodsMapper.updateByPrimaryKey(tbGoods);
                } else if ("6".equals(status)) {//商品关闭
                    System.out.println("修改成商品关闭状态");
                    //赋值修改状态
                    tbGoods.setAuditStatus(status);
                    goodsMapper.updateByPrimaryKey(tbGoods);
                }
            }
        }
    }

    /**
     * 增加
     */
    @Override
    public void add(Goods goods) {
        // 添加商品信息spu表
        TbGoods tbGoods = goods.getTbGoods();
        tbGoods.setAuditStatus("0");
        tbGoods.setIsDelete("0");
        tbGoodsMapper.insert(tbGoods);
        // 添加商品描述信息
        TbGoodsDesc tbGoodsDesc = goods.getTbGoodsDesc();
        // 向商品描述信息表中添加商品表的主键
        tbGoodsDesc.setGoodsId(tbGoods.getId());
        tbGoodsDescMapper.insert(tbGoodsDesc);
        // 添加库存表信息sku

        if ("1".equals(tbGoods.getIsEnableSpec())) {
            TbItem tbItem = noDef(goods, tbGoods, tbGoodsDesc);
            // 添加数据
            tbItemMapper.insert(tbItem);
        } else {
            TbItem tbItem = Def(tbGoods, tbGoodsDesc);
            // 添加数据
            tbItemMapper.insert(tbItem);

        }
    }


    /**
     * 修改
     */
    @Override
    public void update(TbGoods goods) {
        goodsMapper.updateByPrimaryKey(goods);
    }

    /**
     * 批量删除
     */
    @Override
    public void delete(Long[] ids) {
        for (Long id : ids) {
            TbGoods tbGoods = goodsMapper.selectByPrimaryKey(id);
            tbGoods.setIsDelete("1");
            goodsMapper.updateByPrimaryKey(tbGoods);
        }
    }


    @Override
    public PageResult findPage(TbGoods goods, int pageNum, int pageSize) {
        PageHelper.startPage(pageNum, pageSize);

        TbGoodsExample example = new TbGoodsExample();
        TbGoodsExample.Criteria criteria = example.createCriteria();
        //增加逻辑删除不显示
        criteria.andIsDeleteEqualTo("0");
        if (goods != null) {
            if (goods.getSellerId() != null && goods.getSellerId().length() > 0) {
                criteria.andSellerIdLike("%" + goods.getSellerId() + "%");
            }
            if (goods.getGoodsName() != null && goods.getGoodsName().length() > 0) {
                criteria.andGoodsNameLike("%" + goods.getGoodsName() + "%");
            }
            if (goods.getAuditStatus() != null && goods.getAuditStatus().length() > 0) {
                criteria.andAuditStatusLike("%" + goods.getAuditStatus() + "%");
            }
            if (goods.getIsMarketable() != null && goods.getIsMarketable().length() > 0) {
                criteria.andIsMarketableLike("%" + goods.getIsMarketable() + "%");
            }
            if (goods.getCaption() != null && goods.getCaption().length() > 0) {
                criteria.andCaptionLike("%" + goods.getCaption() + "%");
            }
            if (goods.getSmallPic() != null && goods.getSmallPic().length() > 0) {
                criteria.andSmallPicLike("%" + goods.getSmallPic() + "%");
            }
            if (goods.getIsEnableSpec() != null && goods.getIsEnableSpec().length() > 0) {
                criteria.andIsEnableSpecLike("%" + goods.getIsEnableSpec() + "%");
            }
            if (goods.getIsDelete() != null && goods.getIsDelete().length() > 0) {
                criteria.andIsDeleteLike("%" + goods.getIsDelete() + "%");
            }

        }

        Page<TbGoods> page = (Page<TbGoods>) goodsMapper.selectByExample(example);
        return new PageResult(page.getTotal(), page.getResult());
    }

    //修改商品信息
    @Override
    public void update(Goods goods) {
        // 修改商品信息spu表
        TbGoods tbGoods = goods.getTbGoods();
        tbGoods.setAuditStatus("0");
        tbGoods.setIsDelete("0");
        tbGoodsMapper.updateByPrimaryKey(tbGoods);
        // 添加商品描述信息
        TbGoodsDesc tbGoodsDesc = goods.getTbGoodsDesc();
        // 向商品描述信息表中添加商品表的主键
        tbGoodsDesc.setGoodsId(tbGoods.getId());
        tbGoodsDescMapper.updateByPrimaryKey(tbGoodsDesc);
        // 添加库存表信息sku

        if ("1".equals(tbGoods.getIsEnableSpec())) {
            TbItem tbItem = noDef(goods, tbGoods, tbGoodsDesc);
            // 修改数据
            tbItemMapper.updateByPrimaryKey(tbItem);

        } else {
            TbItem tbItem = Def(tbGoods, tbGoodsDesc);

            // 修改数据
            tbItemMapper.updateByPrimaryKey(tbItem);
        }
    }

    //有规格选项
    public TbItem noDef(Goods goods, TbGoods tbGoods, TbGoodsDesc tbGoodsDesc) {
        List<TbItem> itemList = goods.getItemList();
        for (TbItem tbItem : itemList) {
            // 标题
            String title = tbGoods.getGoodsName();
            // 规格数据在库存表中是对象，以键值对的形式存在
            Map<String, String> map = JSON.parseObject(tbItem.getSpec(), Map.class);
            for (String key : map.keySet()) {
                // 根据键取值
                title += " " + map.get(key).toString();
            }
            // 商品标题
            tbItem.setTitle(title);
            // 商品卖点
            tbItem.setSellPoint(tbGoods.getCaption());
            // 商品图片
            List<Map> images = JSON.parseArray(tbGoodsDesc.getItemImages(), Map.class);
            if (images.size() > 0) {
                tbItem.setImage(images.get(0).get("url").toString());
            }
            // 所属类目，叶子类目
            tbItem.setCategoryid(tbGoods.getCategory3Id());
            // 创建时间
            tbItem.setCreateTime(new Date());
            // 更新时间
            tbItem.setUpdateTime(new Date());
            // 商品表主键
            tbItem.setGoodsId(tbGoods.getId());
            // 商家用户id
            tbItem.setSellerId(tbGoods.getSellerId());
            // 分类名称
            TbItemCat itemCat = tbItemCatMapper.selectByPrimaryKey(tbGoods.getTypeTemplateId());
            tbItem.setCategory(itemCat.getName());
            // 品牌名称
            TbBrand tbBrand = tbBrandMapper.selectByPrimaryKey(tbGoods.getBrandId());
            tbItem.setBrand(tbBrand.getName());
            // 商家名称
            TbSeller tbSeller = tbSellerMapper.selectByPrimaryKey(tbGoods.getSellerId());
            tbItem.setSeller(tbSeller.getName());
            return tbItem;
        }
        return null;

    }

    //没有规格选项
    public TbItem Def(TbGoods tbGoods, TbGoodsDesc tbGoodsDesc) {
        TbItem tbItem = new TbItem();
        // 标题
        String title = tbGoods.getGoodsName();
        // 商品标题
        tbItem.setTitle(title);
        // 商品卖点
        tbItem.setSellPoint(tbGoods.getCaption());
        // 商品图片
        List<Map> images = JSON.parseArray(tbGoodsDesc.getItemImages(), Map.class);
        for (Map map : images) {
            if (map.get("url") != null) {
                tbItem.setImage(images.get(0).get("url").toString());
            }
        }
        // 所属类目，叶子类目
        tbItem.setCategoryid(tbGoods.getCategory3Id());
        // 创建时间
        tbItem.setCreateTime(new Date());
        // 更新时间
        tbItem.setUpdateTime(new Date());
        // 商品表主键
        tbItem.setGoodsId(tbGoods.getId());
        // 商家用户id
        tbItem.setSellerId(tbGoods.getSellerId());
        // 分类名称
        TbItemCat itemCat = tbItemCatMapper.selectByPrimaryKey(tbGoods.getTypeTemplateId());
        tbItem.setCategory(itemCat.getName());
        // 品牌名称
        TbBrand tbBrand = tbBrandMapper.selectByPrimaryKey(tbGoods.getBrandId());
        tbItem.setBrand(tbBrand.getName());
        // 商家名称
        TbSeller tbSeller = tbSellerMapper.selectByPrimaryKey(tbGoods.getSellerId());
        tbItem.setSeller(tbSeller.getName());
        // 默认价格
        tbItem.setPrice(tbGoods.getPrice());
        // 默认库存
        tbItem.setNum(9999);
        // 默认是否启用为不启用
        tbItem.setStatus("0");
        // 是否默认商品为不默认
        tbItem.setIsDefault("0");
        return tbItem;
    }
}
