//package com.laotabu.web.service.impl;
//
//import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
//import com.laotabu.common.annotation.DataSource;
//import com.laotabu.common.enums.DataSourceType;
//import com.laotabu.mq.domain.K3ERP;
//import com.laotabu.web.mapper.K3ERPMapper;
//import com.laotabu.web.service.K3ERPService;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//
///**
// * @Author: lsd
// * @Date: 2023/10/19 - 10:08
// * @Desc:
// */
//@Service
//public class K3ERPServiceImpl extends ServiceImpl<K3ERPMapper, K3ERP> implements K3ERPService {
//
//    @Autowired
//    K3ERPMapper k3ERPMapper;
//
//    @Override
//    @DataSource(DataSourceType.K3)
//    public void test() {
//        K3ERP k3ERP = k3ERPMapper.selectById(1);
//        System.out.println(k3ERP);
//    }
//}
