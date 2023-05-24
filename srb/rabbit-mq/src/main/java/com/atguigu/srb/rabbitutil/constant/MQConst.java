package com.atguigu.srb.rabbitutil.constant;

/*
* 消息队列基本配置信息
* @param "EXCHANGE_TOPIC_SMS" ---> 短信交换机
* @param "ROUTING_SMS_ITEM" ---> 短信路由
* @param "QUEUE_SMS_ITEM" ---> 消息队列
* */
public class MQConst {

    public static final String EXCHANGE_TOPIC_SMS = "exchange.topic.sms";//交换机
    public static final String ROUTING_SMS_ITEM = "routing.sms.item";//路由
    public static final String QUEUE_SMS_ITEM  = "queue.sms.item";//消息队列
}
