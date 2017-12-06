package com.lccf.base.controller;

import java.util.concurrent.TimeUnit;
import javax.annotation.Resource;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RequestMapping("api")
public class BaseController {
  @Resource
   RedisTemplate redisTemplate;
  public final  static  String  EMAIL_KEY= "email_key.";
  /**
   * 频率限制
   * @param key
   * @param time 时间
   * @param count 限制次数
   * @return
   */
  public  Boolean apiFrequency(String key,Long time ,Integer count){
    Long value = redisTemplate.opsForValue().increment(key, 1L);
    redisTemplate.expire(key, time, TimeUnit.SECONDS);
    if(value > count) {
      return false;
    }
    return true;
  }

}
