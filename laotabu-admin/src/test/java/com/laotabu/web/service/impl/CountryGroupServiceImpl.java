package com.laotabu.web.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.laotabu.common.annotation.DataSource;
import com.laotabu.common.enums.DataSourceType;
import com.laotabu.web.domain.CountryGroup;
import com.laotabu.web.mapper.CountryGroupMapper;
import com.laotabu.web.service.CountryGroupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @Author: lsd
 * @Date: 2023/10/19 - 15:07
 * @Desc:
 */
@Service
public class CountryGroupServiceImpl extends ServiceImpl<CountryGroupMapper, CountryGroup> implements CountryGroupService {

    @Autowired
    CountryGroupMapper countryGroupMapper;

    @Override
    @DataSource(DataSourceType.K3)
    public void test() {
        CountryGroup countryGroup = this.getById(1);
        System.out.println(countryGroup);
    }
}
