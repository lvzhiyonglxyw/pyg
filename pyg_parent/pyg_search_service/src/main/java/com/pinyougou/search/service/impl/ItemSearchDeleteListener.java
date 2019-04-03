package com.pinyougou.search.service.impl;

import com.pinyougou.search.service.SeachItenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.ObjectMessage;

@Component
@Transactional
public class ItemSearchDeleteListener implements MessageListener {

    //删除solr库信息
    @Autowired
    private SeachItenService seachItenService;

    @Override
    public void onMessage(Message message) {
        ObjectMessage objectMessage = (ObjectMessage) message;
        try {
            Long[] ids = (Long[]) objectMessage.getObject();
            seachItenService.removeItem(ids);
        } catch (JMSException e) {
            e.printStackTrace();
        }
    }
}
