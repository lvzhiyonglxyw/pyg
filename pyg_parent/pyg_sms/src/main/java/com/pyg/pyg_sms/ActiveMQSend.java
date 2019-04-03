package com.pyg.pyg_sms;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.jms.JMSException;
import javax.jms.MapMessage;
import javax.jms.Message;
import javax.jms.MessageListener;

@Component
public class ActiveMQSend implements MessageListener {

    @Autowired
    private SmsSend smsSend;

    @Override
    public void onMessage(Message message) {
        MapMessage mapMessage = (MapMessage) message;
        try {
            smsSend.send(mapMessage.getString("phone"), mapMessage.getString("code"));
        } catch (JMSException e) {
            e.printStackTrace();
        }
    }
}
