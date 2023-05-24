package com.atguigu.srb.base.config;

import feign.Logger;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenFeignConfig {

    /*
    * OpenFeign设置日志级别FULL>HEADERS>BASIC>NONE
    * */
    @Bean
    Logger.Level feignLoggerLevel(){
        return Logger.Level.FULL;
    }
}
