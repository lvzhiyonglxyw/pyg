package com.pinyougou.shop.controller;

import java.util.List;

import com.pyg.pyg_sellergoods_interface.GoodsService;
import com.pyg_pojo.entity.Goods;
import com.pyg_pojo.entity.PageResult;
import com.pyg_pojo.entity.Result;
import com.pyg_pojo.pojo.TbGoods;
import org.apache.activemq.ActiveMQXAConnectionFactory;
import org.apache.activemq.command.ActiveMQQueue;
import org.apache.activemq.command.ActiveMQTopic;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.alibaba.dubbo.config.annotation.Reference;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.ObjectMessage;
import javax.jms.Session;

@SuppressWarnings("all")
@RestController
@RequestMapping("/goods")
public class GoodsController {
    @Reference
    private GoodsService goodsService;
    /*
      @Reference
      private SeachItenService seachItenService;

      @Reference
      private FreeMarkerService freeMarkerService;*/
    @Autowired
    private JmsTemplate jmsTemplate;
    //添加sorl库数据
    @Autowired
    private ActiveMQQueue queueSolrCreateDestination;
    //删除solr库数据
    @Autowired
    private ActiveMQQueue queueSolrDeleteDestination;
    //生成静态模板
    @Autowired
    private ActiveMQTopic topicPageCreateDestination;
    //删除静态模板
    @Autowired
    private ActiveMQTopic topicPageDeleteDestination;

    @RequestMapping("/updateStatus")
    public Result updateStatus(Long[] ids, String status) {
        try {
            goodsService.updateStatus(ids, status);
            if ("4".equals(status)) {
                //上架
                //添加solr库数据
                //seachItenService.importItem(ids);
                //发送消息
                jmsTemplate.send(queueSolrCreateDestination, new MessageCreator() {
                    @Override
                    public Message createMessage(Session session) throws JMSException {
                        return session.createObjectMessage(ids);
                    }
                });

                //生成模板
                for (Long id : ids) {
                    //freeMarkerService.importTemplate(id);
                    jmsTemplate.send(topicPageCreateDestination, new MessageCreator() {
                        @Override
                        public Message createMessage(Session session) throws JMSException {
                            return session.createObjectMessage(id);
                        }
                    });
                }
            }
            if ("5".equals(status)) {
                //下架
                //删除solr库数据
                //seachItenService.removeItem(ids);
                jmsTemplate.send(queueSolrDeleteDestination, new MessageCreator() {
                    @Override
                    public Message createMessage(Session session) throws JMSException {
                        return session.createObjectMessage(ids);
                    }
                });
                //删除模板
                for (Long id : ids) {
                    //freeMarkerService.removeTemplate(id);
                    jmsTemplate.send(topicPageDeleteDestination, new MessageCreator() {
                        @Override
                        public Message createMessage(Session session) throws JMSException {
                            return session.createObjectMessage(id);
                        }
                    });
                }
            }
            if ("6".equals(status)) {
                //商家关闭
                //删除solr库数据
                //seachItenService.removeItem(ids);
                jmsTemplate.send(queueSolrDeleteDestination, new MessageCreator() {
                    @Override
                    public Message createMessage(Session session) throws JMSException {
                        return session.createObjectMessage(ids);
                    }
                });
                //删除模板
                for (Long id : ids) {
                    // freeMarkerService.removeTemplate(id);
                    jmsTemplate.send(topicPageDeleteDestination, new MessageCreator() {
                        @Override
                        public Message createMessage(Session session) throws JMSException {
                            return session.createObjectMessage(id);
                        }
                    });
                }
            }
            return new Result(true, "操作成功");
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, "操作失败");
        }
    }

    /**
     * 返回全部列表
     *
     * @return
     */
    @RequestMapping("/findAll")
    public List<TbGoods> findAll() {
        return goodsService.findAll();
    }

    // 根据商品id查询
    @RequestMapping("/findOne")
    public Goods findOne(Long id) {
        return goodsService.findOne(id);
    }

    /**
     * 返回全部列表
     *
     * @return
     */
    @RequestMapping("/findPage")
    public PageResult findPage(int page, int rows) {
        return goodsService.findPage(page, rows);
    }

    /**
     * 增加
     *
     * @param goods
     * @return
     */
    @RequestMapping("/add")
    public Result add(@RequestBody Goods goods) {
        try {
            //添加商家用户登录名
            String name = SecurityContextHolder.getContext().getAuthentication().getName();
            goods.getTbGoods().setSellerId(name);
            goodsService.add(goods);
            return new Result(true, "增加成功");
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, "增加失败");
        }
    }

    // 修改商品信息
    @RequestMapping("/update")
    public Result update(@RequestBody Goods goods) {
        try {
            // 添加商家用户id
            String name = SecurityContextHolder.getContext().getAuthentication().getName();
            goods.getTbGoods().setSellerId(name);
            goodsService.update(goods);
            return new Result(true, "修改成功");
        } catch (Exception e) {
            // TODO: handle exception
            return new Result(false, "修改失败");
        }
    }


    /**
     * 批量删除
     *
     * @param ids
     * @return
     */
    @RequestMapping("/delete")
    public Result delete(Long[] ids) {
        try {
            goodsService.delete(ids);
            return new Result(true, "删除成功");
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, "删除失败");
        }
    }

    /**
     * 查询+分页
     *
     * @param
     * @param page
     * @param rows
     * @return
     */
    @RequestMapping("/search")
    public PageResult search(@RequestBody TbGoods goods, int page, int rows) {
        //商家只查询自己的商品信息
        String name = SecurityContextHolder.getContext().getAuthentication().getName();
        goods.setSellerId(name);
        return goodsService.findPage(goods, page, rows);
    }

}
