package com.laotabu.mq.service;



/**
 * @Author: lsd
 * @Date: 2023/11/29 - 14:04
 * @Desc: 消息中间件生产者
 */

public interface MQProducerService<T> {

    void sendMsgToRocketMQ(String topic, T msg);
    void sendDelayMsgToRocketMQ(String topic, String msg, Integer time, Integer level);
}
