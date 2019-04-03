package com.pyg.pyg_user_service.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.pinyougou.utils.HttpClient;
import com.pyg.pyg_user_service.service.TbUserService;
import com.pyg_dao.mapper.TbUserMapper;
import com.pyg_pojo.pojo.TbUser;
import org.apache.activemq.command.ActiveMQQueue;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;
import org.springframework.transaction.annotation.Transactional;

import javax.jms.JMSException;
import javax.jms.MapMessage;
import javax.jms.Message;
import javax.jms.Session;
import java.util.Date;
import java.util.concurrent.TimeUnit;

@Service
@Transactional
public class TbUserServiceImpl implements TbUserService {

    @Autowired
    private TbUserMapper tbUserMapper;

    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private JmsTemplate jmsTemplate;

    @Autowired
    private ActiveMQQueue activeMQQueue;

    //注册用户
    @Override
    public void save(TbUser tbUser) {
        tbUser.setCreated(new Date());
        tbUser.setUpdated(new Date());
        //密码加密
        tbUser.setPassword(DigestUtils.md5Hex(tbUser.getPassword()));
        tbUserMapper.insert(tbUser);
    }

    //发送短信
    @Override
    public void sendSms(String phone) {
        try {
            //随机生成六位数验证码
            String code = RandomStringUtils.randomNumeric(6);
            //把生成的验证码保存到redis中
            redisTemplate.boundHashOps("sendSms").put(phone, code);
            //设置验证码5分钟后
            redisTemplate.boundHashOps("sendSms").expire(300000, TimeUnit.MILLISECONDS);

            ////////////////////////使用MQ////////////////////////////////////////
            jmsTemplate.send(activeMQQueue, new MessageCreator() {
                @Override
                public Message createMessage(Session session) throws JMSException {
                    MapMessage mapMessage = session.createMapMessage();
                    mapMessage.setString("phone", phone);
                    mapMessage.setString("code", code);
                    return mapMessage;
                }
            });
            ////////////////////////使用httpClient///////////////////////////////
            /*HttpClient client = new HttpClient("http://localhost:9002/send.do?phone=" + phone + "&code=" + code);
            //发送get请求
            client.get();
            String content = client.getContent();
            System.out.println(content);*/
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //判断生成的密码和输入的密码是否相等
    @Override
    public int isCheck(String phone, String code) {
        int flag = 0;
        String phoneStr = (String) redisTemplate.boundHashOps("sendSms").get(phone);
        //判断redis中是否有数据
        if (phoneStr != null) {
            //判断生成的密码和输入的密码是否相等
            if (!code.equals(phoneStr)) {
                //验证码有误
                return 1;
            }
        } else {
            //验证码失效
            return 0;
        }
        //验证码成功
        return 2;
    }
}
