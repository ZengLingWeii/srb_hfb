package com.atguigu.srb.rabbitutil.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;

/*
* 消息发送服务
* 虚拟机安装Erlang(RabbitMQ是由Erlang编写的): rpm -ivh erlang xxx.rpm
* 虚拟机安装Socat(RabbitMQ的安装依赖于Socat): rpm -ivh socat xxx.rpm
* 虚拟机安装RabbitMQ(与Erlang版本要一致): rpm -ivh rabbitmq-service-xxx.rpm
* */
@Service
@Slf4j
public class MQService {

    @Resource
    private AmqpTemplate amqpTemplate;

    /*
    * 发送消息
    * @param exchange 交换机
    * @param routingKey 路由
    * @param message 消息
    * */
    public boolean sendMessage(String exchange,String routingKey,Object message){
        log.info("消息生产 -> 消息队列发送消息");
        amqpTemplate.convertAndSend(exchange,routingKey,message);
        return true;
    }
}
