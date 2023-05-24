package com.atguigu.srb.rabbitutil.config;

import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/*
* 将消息转为Json
* */
@Configuration
public class MQConfig {

    @Bean
    public Jackson2JsonMessageConverter messageConverter(){
        //json字符串转换器
        return new Jackson2JsonMessageConverter();
    }
}
