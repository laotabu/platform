package com.laotabu.mq.listener;

import com.laotabu.mq.domain.constant.ConsumerGroup;
import com.laotabu.mq.domain.constant.Topic;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.formula.functions.T;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;

/**
 * @Author: lsd
 * @Date: 2023/11/29 - 15:22
 * @Desc:
 */


@Component
@RocketMQMessageListener(topic = Topic.ROCKETMQ_ASSETS_TOPIC,
        consumerGroup = ConsumerGroup.ROCKETMQ_ASSETS_CONSUMER_GROUP)
@Slf4j
public class RockMQMessageListener implements RocketMQListener<String> {
    @Override
    public void onMessage(String message) {
        System.out.println(message);
    }
}
