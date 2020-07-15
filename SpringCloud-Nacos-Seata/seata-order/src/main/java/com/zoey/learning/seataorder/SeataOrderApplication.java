package com.zoey.learning.seataorder;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * Seata order application
 * Created in 2020.07.15
 *
 * @author Liaozihong
 */
@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class}) //取消数据源的自动创建。使用我们自己配置的seata代理的数据源
@EnableDiscoveryClient
@EnableFeignClients
@MapperScan("com.zoey.learning.seataorder.dao")
public class SeataOrderApplication {

    /**
     * The entry point of application.
     *
     * @param args the input arguments
     */
    public static void main(String[] args) {
        SpringApplication.run(SeataOrderApplication.class, args);
    }

}
