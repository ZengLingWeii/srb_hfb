package com.atguigu.srb.gateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@EnableDiscoveryClient//启动gateway网关
@SpringBootApplication
public class ServiceGatewayApplication {

    public static void main(String[] args) { SpringApplication.run(ServiceGatewayApplication.class,args); }
}
