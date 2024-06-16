package com.ropulva.calendar.business.layer.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;

@Configuration
public class RedisConfig {

    @Autowired
    private RedisConnectionFactory redisConnectionFactory;


    @Bean
    public boolean checkRedisConnection() throws Exception {
        RedisConnection connection = null;
        try {
            connection = redisConnectionFactory.getConnection();
            connection.ping();
            return true;
        } catch (Exception e) {
            throw new RuntimeException("Failed to connect to Redis", e);
        } finally {
            if (connection != null) {
                connection.close();
            }
        }
    }
}
