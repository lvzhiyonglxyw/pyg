package com.pyg_sole_utils;

import com.alibaba.fastjson.JSON;
import com.pyg_dao.mapper.TbItemMapper;
import com.pyg_pojo.pojo.TbItem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.data.solr.core.SolrTemplate;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Component
public class SolrUtils {

    @Autowired
    private SolrTemplate solrTemplate;
    @Autowired
    private TbItemMapper tbItemMapper;

    public void importItemData() {
        List<TbItem> tbItems = tbItemMapper.selectByExample(null);
        for (TbItem tbItem : tbItems) {
            System.out.println(tbItem.getId() + " " + tbItem.getTitle() + " " + tbItem.getPrice());
            Map<String,String> specMap = JSON.parseObject(tbItem.getSpec(), Map.class);
            tbItem.setSpecMap(specMap);
        }
        //保存数据
        /**
         * saveBean()保存单个的值
         * saveBeans()保存多个值
         *
         */
        solrTemplate.saveBeans(tbItems);
        //提交
        solrTemplate.commit();
    }

    public static void main(String[] args) {
        ApplicationContext context = new ClassPathXmlApplicationContext("classpath*:spring/applicationContext*.xml");
        SolrUtils solrUtil = (SolrUtils) context.getBean("solrUtils");
        solrUtil.importItemData();
    }
}
