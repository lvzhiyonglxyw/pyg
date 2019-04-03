package com.pinyougou.page.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.pinyougou.page.service.FreeMarkerService;
import com.pyg_dao.mapper.TbGoodsDescMapper;
import com.pyg_dao.mapper.TbGoodsMapper;
import com.pyg_dao.mapper.TbItemCatMapper;
import com.pyg_dao.mapper.TbItemMapper;
import com.pyg_pojo.pojo.*;
import freemarker.template.Configuration;
import freemarker.template.Template;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Transactional
public class FreeMarkerServiceImpl implements FreeMarkerService {

    @Autowired
    private FreeMarkerConfigurer freeMarkerConfigurer;

    @Autowired
    private TbGoodsMapper tbGoodsMapper;

    @Autowired
    private TbGoodsDescMapper tbGoodsDescMapper;

    @Autowired
    private TbItemCatMapper tbItemCatMapper;

    @Autowired
    private TbItemMapper tbItemMapper;

    @Autowired
    private HttpServletRequest request;

    /**
     * 根据商品id生成模板
     *
     * @param goodsId
     */
    @Override
    public void importTemplate(Long goodsId) {
        //通过FreeMarkerConfigurer直接获取Configuration
        Configuration configuration = freeMarkerConfigurer.getConfiguration();
        try {
            //通过Configurtion获取模板template
            Template template = configuration.getTemplate("item.ftl");
            //创建writer对象
            FileWriter writer = new FileWriter(new File("E:/heima/Java/pyg_parent/pyg_page_web/src/main/webapp/" + goodsId + ".html"));
            Map dataModel = new HashMap();
            //根据商品id查询商品表和商品描述表
            //商品表
            TbGoods tbGoods = tbGoodsMapper.selectByPrimaryKey(goodsId);
            //商品描述表
            TbGoodsDesc tbGoodsDesc = tbGoodsDescMapper.selectByPrimaryKey(goodsId);

            //查询三个等级分类
            TbItemCat tbItemCat1 = tbItemCatMapper.selectByPrimaryKey(tbGoods.getCategory1Id());
            TbItemCat tbItemCat2 = tbItemCatMapper.selectByPrimaryKey(tbGoods.getCategory2Id());
            TbItemCat tbItemCat3 = tbItemCatMapper.selectByPrimaryKey(tbGoods.getCategory3Id());

            //查询item表中的规格信息
            TbItemExample example = new TbItemExample();
            example.createCriteria().andGoodsIdEqualTo(goodsId);
            List<TbItem> items = tbItemMapper.selectByExample(example);

            //封装list到map中
            dataModel.put("itemList", items);
            //封装三级分类到map中
            dataModel.put("tbItemCat1", tbItemCat1);
            dataModel.put("tbItemCat2", tbItemCat2);
            dataModel.put("tbItemCat3", tbItemCat3);
            //封装到Map中
            dataModel.put("tbGoods", tbGoods);
            dataModel.put("tbGoodsDesc", tbGoodsDesc);
            //通过template.process进行文件的生成
            template.process(dataModel, writer);
            //关闭流
            writer.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //删除静态模板页面
    @Override
    public void removeTemplate(Long goodsId) {
        new File("E:/heima/Java/pyg_parent/pyg_page_web/src/main/webapp/" + goodsId + ".html").delete();
    }
}
