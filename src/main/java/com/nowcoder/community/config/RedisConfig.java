package com.nowcoder.community.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.RedisSerializer;

/**
 * redis配置类
 */
@Configuration
public class RedisConfig {

    @Bean
    public RedisTemplate<String,Object> redisTemplate(RedisConnectionFactory factory){
        RedisTemplate<String, Object> redisTemplate = new RedisTemplate<>();
        redisTemplate.setConnectionFactory(factory);

        //序列化key
        redisTemplate.setKeySerializer(RedisSerializer.string());
        //序列化value
        redisTemplate.setValueSerializer(RedisSerializer.json());
        //序列化Hash的key
        redisTemplate.setHashKeySerializer(RedisSerializer.string());
        //序列化Hash的value
        redisTemplate.setHashValueSerializer(RedisSerializer.json());
        return redisTemplate;
    }
}

