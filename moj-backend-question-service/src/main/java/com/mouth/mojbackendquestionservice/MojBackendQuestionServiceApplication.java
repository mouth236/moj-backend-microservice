package com.mouth.mojbackendquestionservice;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@MapperScan("com.mouth.mojbackendquestionservice.mapper")
@EnableScheduling
@EnableAspectJAutoProxy(proxyTargetClass = true, exposeProxy = true)
@ComponentScan("com.mouth")
@EnableDiscoveryClient
@EnableFeignClients(basePackages = {"com.mouth.mojbackendserviceclient.service"})
public class MojBackendQuestionServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(MojBackendQuestionServiceApplication.class, args);
    }

}
