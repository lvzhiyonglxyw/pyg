package com.pinyougou.page.service.impl;

import com.pinyougou.page.service.FreeMarkerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.ObjectMessage;

@Component
@Transactional
public class ItemPageCreateListener implements MessageListener {
    //创建静态页面

    @Autowired
    private FreeMarkerService freeMarkerService;

    @Override
    public void onMessage(Message message) {
        ObjectMessage objectMessage = (ObjectMessage) message;
        try {
            Long id = (Long) objectMessage.getObject();
            freeMarkerService.importTemplate(id);
        } catch (JMSException e) {
            e.printStackTrace();
        }
    }


}
