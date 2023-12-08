package com.laotabu.mq.service.impl;

import com.laotabu.mq.domain.constant.Topic;
import com.laotabu.mq.service.MQProducerService;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;

/**
 * @Author: lsd
 * @Date: 2023/11/29 - 14:18
 * @Desc:
 */
@Service
public class MQProducerServiceImpl<T> implements MQProducerService<T> {

    private RocketMQTemplate rocketMQTemplate;
    @Autowired
    public MQProducerServiceImpl(RocketMQTemplate rocketMQTemplate) {
        this.rocketMQTemplate = rocketMQTemplate;
    }

    /**
     * 普通发送
     */
    @Override
    public void sendMsgToRocketMQ(String topic, T msg) {
        rocketMQTemplate.convertAndSend(topic, msg);
    }

    @Override
    public void sendDelayMsgToRocketMQ(String topic, String msg, Integer time, Integer level) {
        rocketMQTemplate.syncSend(topic, MessageBuilder.withPayload(msg.getBytes(StandardCharsets.UTF_8)).build(), time, level);
    }
}
