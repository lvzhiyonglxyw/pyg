package com.pinyougou.search.service.impl;
import com.alibaba.dubbo.config.annotation.Service;
import com.pinyougou.search.service.SeachItenService;
import com.pyg_dao.mapper.TbItemMapper;
import com.pyg_pojo.pojo.TbItem;
import com.pyg_pojo.pojo.TbItemExample;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.solr.core.SolrTemplate;
import org.springframework.data.solr.core.query.Criteria;
import org.springframework.data.solr.core.query.HighlightOptions;
import org.springframework.data.solr.core.query.SimpleFilterQuery;
import org.springframework.data.solr.core.query.SimpleHighlightQuery;
import org.springframework.data.solr.core.query.result.HighlightEntry;
import org.springframework.data.solr.core.query.result.HighlightPage;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Transactional
public class SeachItenServiceImpl implements SeachItenService {

    @Autowired
    private TbItemMapper tbItemMapper;
    @Autowired
    private SolrTemplate solrTemplate;

    /**
     * 使用solr进行查询
     *
     * @param searchMap
     * @return
     */
    @Transactional
    @Override
    public Map seachItem(Map searchMap) {
        //先看看参数中的关键字keywords
        String keyStr = (String) searchMap.get("keywords");//keywords是关键字
        System.out.println(keyStr);

        //高亮查询
        SimpleHighlightQuery highlightQuery = new SimpleHighlightQuery();
        //=======进行查询solr库=====

        //================================设置高亮选项===========================
        HighlightOptions options = new HighlightOptions();
        options.addField("item_title"); //设置需要高亮效果的域
        options.setSimplePrefix("<em style='color:red'>"); // 高亮头部
        options.setSimplePostfix("</em>"); // 高亮尾部

        //将高亮选项设置到查询highlightQuery中
        highlightQuery.setHighlightOptions(options);
        //================================设置高亮选项===========================


        //================================设置分类查询===========================
        String categoryStr = (String) searchMap.get("category");
        if (categoryStr != null && categoryStr.length() > 1) {
            //增加过滤查询条件
            SimpleFilterQuery filterQuery = new SimpleFilterQuery();

            Criteria criteria = new Criteria("item_category");
            criteria = criteria.contains(categoryStr); //将分类条件封装到Criteria中
            filterQuery.addCriteria(criteria); //设置查询条件

            highlightQuery.addFilterQuery(filterQuery); //过滤查询
        }

        //================================设置品牌查询===========================
        String brandStr = (String) searchMap.get("brand");
        if (brandStr != null && brandStr.length() > 1) {

            SimpleFilterQuery filterQuery = new SimpleFilterQuery();
            Criteria criteria = new Criteria("item_brand");
            criteria = criteria.contains(brandStr);
            filterQuery.addCriteria(criteria);

            highlightQuery.addFilterQuery(filterQuery);
        }


        //================================设置规格查询===========================
        Map<String, String> specMap = (Map<String, String>) searchMap.get("spec");
        if (specMap != null) {
            for (String key : specMap.keySet()) { //循环前端传入的规格条件

                SimpleFilterQuery fq = new SimpleFilterQuery();
                Criteria criteria = new Criteria("item_spec_" + key); //item_spec_屏幕尺寸
                criteria = criteria.contains(specMap.get(key)); //获取传入map的value值封装到条件中
                fq.addCriteria(criteria);
                //specMap.get(key)
                highlightQuery.addFilterQuery(fq);
            }
        }


        //================================设置价格过滤查询===========================
        String priceStr = (String) searchMap.get("price");
        if (priceStr != null && priceStr.length() > 0) {
            String[] prices = priceStr.split("-");

            //大于等于起始价格
            SimpleFilterQuery fq = new SimpleFilterQuery();
            Criteria criteria = new Criteria("item_price");
            criteria = criteria.greaterThanEqual(prices[0]);
            fq.addCriteria(criteria);
            highlightQuery.addFilterQuery(fq);

            //小于等于结束价格
            if (!"*".equals(prices[1])) { //如果第二个参数不是*
                SimpleFilterQuery filterQuery2 = new SimpleFilterQuery();
                Criteria criteria2 = new Criteria("item_price");
                criteria2 = criteria2.lessThanEqual(prices[1]);
                filterQuery2.addCriteria(criteria2);
                highlightQuery.addFilterQuery(filterQuery2);
            }
        }

        //================================设置价格排序===========================
        String sortStr = (String) searchMap.get("sort");

        if ("ASC".equals(sortStr)) { //升序
            Sort sort = new Sort(Sort.Direction.ASC, "item_price");
            highlightQuery.addSort(sort); //增加排序条件
        } else {//其他都是降序
            Sort sort = new Sort(Sort.Direction.DESC, "item_price");
            highlightQuery.addSort(sort); //增加排序条件
        }


        Criteria criteria = new Criteria("item_keywords"); //域的名字
        if (keyStr != null && keyStr.length() > 0) { //判断前端页面确定传入了关键字
            criteria = criteria.contains(keyStr); //查询条件有关键字的内容，查询条件
        }
        //封装高亮查询条件
        highlightQuery.addCriteria(criteria);

        //准备分页
        Integer pageNo = (Integer) searchMap.get("pageNo");
        Integer pageSize = (Integer) searchMap.get("pageSize");

        highlightQuery.setOffset((pageNo - 1) * pageSize);  //起始记录位置
        highlightQuery.setRows(pageSize);  //一页的记录数


        //通过查询返回page对象
        HighlightPage<TbItem> page = solrTemplate.queryForHighlightPage(highlightQuery, TbItem.class);

        //从page对象中获取list集合
        List<TbItem> items = page.getContent();

        //循环items没有高亮的效果
        for (TbItem item : items) {
            List<HighlightEntry.Highlight> highlights = page.getHighlights(item);//getHighlights

            if (highlights.size() > 0) { //防止数组中无高亮效果数据
                HighlightEntry.Highlight highlight = highlights.get(0);
                List<String> snipplets = highlight.getSnipplets();

                if (snipplets.size() > 0) {
                    item.setTitle(snipplets.get(0)); //设置高亮效果
                }
            }
        }

        //封装返回值,原先的rows
        Map map = new HashMap();
        map.put("content", items); //content需要和searchController.js中的返回值要对上

        //返回总记录数，原先的total
        map.put("total", page.getTotalElements()); //总记录数

        return map;
    }

    /**
     * 根据商品id进行商品导入到solr库中
     *
     * @param ids
     */
    @Override
    public void importItem(Long[] ids) {
        TbItemExample example = new TbItemExample();
        //条件查询数组中所有的值
        example.createCriteria().andGoodsIdIn(Arrays.asList(ids));
        List<TbItem> tbItems = tbItemMapper.selectByExample(example);
        for (TbItem tbItem : tbItems) {
            System.out.println(tbItem.getGoodsId() + "==" + tbItem.getTitle());
        }
        //将查询的数据添加到solr库中
        solrTemplate.saveBeans(tbItems);
        //提交
        solrTemplate.commit();
    }

    /**
     * 根据商品id从solr库中删除
     *
     * @param ids
     */
    @Override
    public void removeItem(Long[] ids) {
        TbItemExample example = new TbItemExample();
        example.createCriteria().andGoodsIdIn(Arrays.asList(ids));
        List<TbItem> tbItems = tbItemMapper.selectByExample(example);
        for (TbItem tbItem : tbItems) {
            System.out.println(tbItem.getGoodsId() + "==" + tbItem.getTitle());
            solrTemplate.deleteById(tbItem.getGoodsId().toString());
        }
        solrTemplate.commit();
    }
}
