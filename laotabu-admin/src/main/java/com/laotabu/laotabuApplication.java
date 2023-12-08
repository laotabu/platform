package com.laotabu;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

/**
 * 启动程序
 * 
 * @author laotabu
 */
@SpringBootApplication(exclude = { DataSourceAutoConfiguration.class })
public class laotabuApplication
{
    public static void main(String[] args)
    {
        // System.setProperty("spring.devtools.restart.enabled", "false");
        SpringApplication.run(laotabuApplication.class, args);

    }
}
