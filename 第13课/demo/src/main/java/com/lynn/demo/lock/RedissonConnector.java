package com.lynn.demo.lock;

import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

/**
 * 获取RedissonClient连接类
 */
@Component
public class RedissonConnector {

    @Value("${spring.redis.host}")
    private String host;
    @Value("${spring.redis.port}")
    private String port;
//    @Value("${spring.redis.password}")
//    private String password;

    RedissonClient redisson;
    @PostConstruct
    public void init(){
        Config config = new Config();
        config.useSingleServer().setAddress("redis://"+host+":"+port);
//        redisson = Redisson.create(config);
    }

    public RedissonClient getClient(){
        return redisson;
    }

}
