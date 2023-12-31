package com.laotabu.activiti.config;

import org.activiti.api.runtime.shared.identity.UserGroupManager;
import org.activiti.engine.cfg.ProcessEngineConfigurator;
import org.activiti.engine.delegate.event.ActivitiEventListener;
import org.activiti.engine.impl.bpmn.parser.factory.DefaultActivityBehaviorFactory;
import org.activiti.engine.impl.persistence.StrongUuidGenerator;
import org.activiti.spring.SpringAsyncExecutor;
import org.activiti.spring.SpringProcessEngineConfiguration;
import org.activiti.spring.boot.*;
import org.activiti.spring.boot.process.validation.AsyncPropertyValidator;
import org.activiti.validation.ProcessValidatorImpl;
import org.activiti.validation.validator.ValidatorSet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.io.Resource;
import org.springframework.core.task.SyncTaskExecutor;
import org.springframework.core.task.TaskExecutor;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

/**
 * @Author: lsd
 * @Date: 2023/11/6 - 16:37
 * @Desc:
 */
@Configuration
public class ActivitiConfig extends AbstractProcessEngineAutoConfiguration {

    @Bean("activitiDataSourece")
    @ConfigurationProperties(prefix = "spring.activiti.datasource")
    public DataSource activitiDataSourec(){
        return DataSourceBuilder
                .create()
                .build();
    }
    @Bean
    @Primary
    public SpringProcessEngineConfiguration springProcessEngineConfiguration(
            PlatformTransactionManager transactionManager,
            SpringAsyncExecutor springAsyncExecutor,
            ActivitiProperties activitiProperties,
            @Autowired(required = false) DefaultActivityBehaviorFactoryMappingConfigurer processEngineConfigurationConfigurer,
            @Autowired(required = false) List<ProcessEngineConfigurator> processEngineConfigurators,
            UserGroupManager userGroupManager,
            @Qualifier("activitiDataSourece") DataSource dataSource) throws IOException {
        SpringProcessEngineConfiguration conf = new SpringProcessEngineConfiguration();
        conf.setConfigurators(processEngineConfigurators);
        //设置数据源
        conf.setDataSource(dataSource);
        //设置全局事务管理器
        conf.setTransactionManager(transactionManager);
        if (springAsyncExecutor != null) {
            conf.setAsyncExecutor(springAsyncExecutor);
        }
        //设置发布名
        conf.setDeploymentName(activitiProperties.getDeploymentName());
        conf.setDatabaseSchema(activitiProperties.getDatabaseSchema());
        conf.setDatabaseSchemaUpdate(activitiProperties.getDatabaseSchemaUpdate());
        conf.setDbHistoryUsed(activitiProperties.isDbHistoryUsed());
        conf.setAsyncExecutorActivate(activitiProperties.isAsyncExecutorActivate());
        if (!activitiProperties.isAsyncExecutorActivate()) {
            ValidatorSet springBootStarterValidatorSet = new ValidatorSet("activiti-spring-boot-starter");
            springBootStarterValidatorSet.addValidator(new AsyncPropertyValidator());
            if (conf.getProcessValidator() == null) {
                ProcessValidatorImpl processValidator = new ProcessValidatorImpl();
                processValidator.addValidatorSet(springBootStarterValidatorSet);
                conf.setProcessValidator(processValidator);
            } else {
                conf.getProcessValidator().getValidatorSets().add(springBootStarterValidatorSet);
            }
        }
        //设置邮件发送配置
        conf.setMailServerHost(activitiProperties.getMailServerHost());
        conf.setMailServerPort(activitiProperties.getMailServerPort());
        conf.setMailServerUsername(activitiProperties.getMailServerUserName());
        conf.setMailServerPassword(activitiProperties.getMailServerPassword());
        conf.setMailServerDefaultFrom(activitiProperties.getMailServerDefaultFrom());
        conf.setMailServerUseSSL(activitiProperties.isMailServerUseSsl());
        conf.setMailServerUseTLS(activitiProperties.isMailServerUseTls());
        if (userGroupManager != null) {
            conf.setUserGroupManager(userGroupManager);
        }
        conf.setHistoryLevel(activitiProperties.getHistoryLevel());
        conf.setCopyVariablesToLocalForTasks(activitiProperties.isCopyVariablesToLocalForTasks());
        conf.setSerializePOJOsInVariablesToJson(activitiProperties.isSerializePOJOsInVariablesToJson());
        conf.setJavaClassFieldForJackson(activitiProperties.getJavaClassFieldForJackson());
        if (activitiProperties.getCustomMybatisMappers() != null) {
            conf.setCustomMybatisMappers(
                    getCustomMybatisMapperClasses(activitiProperties.getCustomMybatisMappers()));
        }
        if (activitiProperties.getCustomMybatisXMLMappers() != null) {
            conf.setCustomMybatisXMLMappers(
                    new HashSet<>(activitiProperties.getCustomMybatisXMLMappers()));
        }
        if (activitiProperties.getCustomMybatisXMLMappers() != null) {
            conf.setCustomMybatisXMLMappers(
                    new HashSet<>(activitiProperties.getCustomMybatisXMLMappers()));
        }
        //设置其Id产生器
//        conf.setIdGenerator(new RxIdGenerator());
        if (activitiProperties.getDeploymentMode() != null) {
            conf.setDeploymentMode(activitiProperties.getDeploymentMode());
        }
        conf.setActivityBehaviorFactory(new DefaultActivityBehaviorFactory());
        if (processEngineConfigurationConfigurer != null) {
            processEngineConfigurationConfigurer.configure(conf);
        }
        //加上全局事件的控制
//        List<ActivitiEventListener> eventListeners = new ArrayList<>();
//        eventListeners.add(new GlobalEventListener());
//        conf.setEventListeners(eventListeners);
        //修改其流程缓存
//        conf.setProcessDefinitionCache(new FlowDefCache());
        return conf;
    }

    @Override
    @Primary
    @Bean
    public TaskExecutor taskExecutor() {
        return new SyncTaskExecutor();
    }

//    @Bean
//    public ProcessDiagramGenerator activitiDiagramGenerator() {
//        return new ActivitiDiagramGenerator();
//    }

}
