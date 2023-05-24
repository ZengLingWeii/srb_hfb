package com.atguigu.srb.base.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.jsontype.impl.LaissezFaireSubTypeValidator;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

/*
* Redis序列化和反序列化的配置，覆盖JDBC原有的序列方法
* */

@Configuration
public class RedisConfig {

    /*①
     * Redis的常用命令：
     * 启动后台: redis-server.exe redis.windows.conf
     * 查看Redis: redis-cli -h 127.0.0.1 -p 6379
     * 关闭和退出: shutdown/exit
     * 配置哨兵: sentinel monitor mymaster 127.0.0.1 6379 1 (mymaster表示要监控的主数据库的名字)/(建立一个配置文件:sentinel.conf)
     * 启动哨兵: redis-sentinel /path/to/sentinel.conf
     * */

    /*②
     * Redis的简单数据操作:
     * 查看数据库: select X (表示查看X号数据库)
     * 设置键值对: set name zlw (key->name : value->zlw)
     * 通过键拿值: get name (得到值: zlw)
     * 查看数据库所有的key: key*
     * 判断当前key是否存在: exists name (判断是否有名为name的key)
     * 移除key值: move name (表示移除name这个key)
     * */
    @Bean
    public RedisTemplate<String, Object> redisTemplate(LettuceConnectionFactory redisConnectionFactory) {

        //创建对象RedisTemplate<String,Object>
        RedisTemplate<String, Object> redisTemplate = new RedisTemplate<>();
        //Redis设置连接工厂
        redisTemplate.setConnectionFactory(redisConnectionFactory);

        //首先解决Key的序列化方式
        StringRedisSerializer stringRedisSerializer = new StringRedisSerializer();
        redisTemplate.setKeySerializer(stringRedisSerializer);

        //解决Value的序列化方式
        Jackson2JsonRedisSerializer<Object> jackson2JsonRedisSerializer = new Jackson2JsonRedisSerializer<>(Object.class);

        //序列化时将类的数据类型存入json，以便反序列化的时候转换成正确的类型
        ObjectMapper objectMapper = new ObjectMapper();
        //将当前对象的数据类型(例：scr.core.pojo.Dict)也存入序列化的结果字符串中
        objectMapper.activateDefaultTyping(LaissezFaireSubTypeValidator.instance, ObjectMapper.DefaultTyping.NON_FINAL);

        // 解决jackson2无法反序列化LocalDateTime时间的问题
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS); //禁用WRITE_DATES序列化方案
        objectMapper.registerModule(new JavaTimeModule()); //使用JavaTimeModule时间序列化方案
        jackson2JsonRedisSerializer.setObjectMapper(objectMapper);

        redisTemplate.setValueSerializer(jackson2JsonRedisSerializer);
        return redisTemplate;
    }
}
