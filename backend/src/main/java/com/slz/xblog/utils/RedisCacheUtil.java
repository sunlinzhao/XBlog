package com.slz.xblog.utils;

/**
 * @author : SunLZ
 * @project : backend
 * @date : 2024/10/23
 */
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.concurrent.TimeUnit;

@Component
public class RedisCacheUtil {

    @Resource
    private RedisTemplate<String, Object> redisTemplate;
    // 保存数据
    public void set(String key, Object value, long timeout) {
        String json_value = JsonUtil.objectToJson(value);
        if (json_value != null) {
            redisTemplate.opsForValue().set(key, json_value, timeout, TimeUnit.SECONDS);
        } else {
            throw new RuntimeException("数据转换异常");
        }
    }

    // 获取数据
    public Object get(String key) {
        if (!exists(key)) {
            throw new RuntimeException("缓存不存在");
        }
        return redisTemplate.opsForValue().get(key);
    }

    // 删除数据
    public void delete(String key) {
        redisTemplate.delete(key);
    }

    // 判断是否存在
    public boolean exists(String key) {
        return Boolean.TRUE.equals(redisTemplate.hasKey(key));
    }

    // 设置过期时间
    public void expire(String key, long timeout) {
        if (!exists(key)) {
            throw new RuntimeException("缓存不存在");
        }
        redisTemplate.expire(key, timeout, TimeUnit.SECONDS);
    }

    // 存入并设置过期时间
    public void setWithExpire(String key, Object value, long timeout) {
        String json_value = JsonUtil.objectToJson(value);
        if (json_value != null) {
            redisTemplate.opsForValue().set(key, json_value, timeout, TimeUnit.SECONDS);
        } else {
            redisTemplate.opsForValue().set(key, value, timeout, TimeUnit.SECONDS);
        }
    }
}

