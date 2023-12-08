//package com.laotabu.web;
//
//import com.laotabu.laotabuApplication;
//import com.laotabu.mq.domain.constant.Topic;
//import com.laotabu.mq.service.MQProducerService;
//import org.junit.Test;
//import org.junit.runner.RunWith;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.test.context.junit4.SpringRunner;
//
///**
// * @Author: lsd
// * @Date: 2023/10/19 - 10:33
// * @Desc:
// */
//@RunWith(SpringRunner.class)
//@SpringBootTest(classes = laotabuApplication.class,webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
//public class laotabuApplication {
////    @Autowired
////    K3ERPService k3ERPService;
////    @Autowired
////    CountryGroupService countryGroupService;
//    @Autowired
//    MQProducerService mqProducerService;
//
////    @Test
////    public void testK3_ERPJDBC(){
////        k3ERPService.test();
////    }
////    @Test
////    public void testCountryGroup(){
////        countryGroupService.test();
////    }
//    @Test
//    public void testRocketMQ() throws InterruptedException {
////        for (int i = 0; i < 100; i++) {
////            mqProducerService.sendMsgToRocketMQ(Topic.ROCKETMQ_ASSETS_TOPIC,
////                    "helo broker");
////        }
//        String msg = "延迟消费";
//        System.out.println(msg);
//        mqProducerService.sendDelayMsgToRocketMQ(Topic.ROCKETMQ_ASSETS_TOPIC, msg, 3000, 3);
//        Thread.currentThread().sleep(10000);
//    }
//}
