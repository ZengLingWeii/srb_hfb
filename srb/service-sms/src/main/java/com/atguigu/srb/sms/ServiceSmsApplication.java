package com.atguigu.srb.sms;

import com.atguigu.srb.sms.client.CoreUserInfoClient;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;


@ComponentScan({"com.atguigu.srb","com.atguigu.common"})
@EnableFeignClients//开启OpenFeign远程服务调用
@SpringBootApplication
public class ServiceSmsApplication {
    public static void main(String[] args) {
        SpringApplication.run(ServiceSmsApplication.class, args);
    }
}
