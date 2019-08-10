package com.skyguard.kproxy.cache;

import com.skyguard.kproxy.config.ServiceConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.BoundHashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

@Component
public class RedisCache implements Cache{

    @Autowired
    private RedisTemplate redisTemplate;

    @Override
    public ServiceConfig getServiceConfig(String host) {

        BoundHashOperations<String,String,ServiceConfig> hashOperations = redisTemplate.boundHashOps(host);
        ServiceConfig serviceConfig = hashOperations.get(host);


        return serviceConfig;
    }



}
