package com.mouth.mojbackendjudgeservice;

import com.mouth.mojbackendjudgeservice.rabbitmq.InitRabbitMq;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
@EnableAspectJAutoProxy(proxyTargetClass = true, exposeProxy = true)
@ComponentScan("com.mouth")
@EnableDiscoveryClient
@EnableFeignClients(basePackages = {"com.mouth.mojbackendserviceclient.service"})
public class MojBackendJudgeServiceApplication {

    public static void main(String[] args) {
        //初始化消息队列
        InitRabbitMq.doInit();
        SpringApplication.run(MojBackendJudgeServiceApplication.class, args);
    }

}
