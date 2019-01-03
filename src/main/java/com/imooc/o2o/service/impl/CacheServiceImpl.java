/**
 * FileName: CacheServiceImpl
 * Author:   gongyu
 * Date:     2018/6/15 18:13
 * Description:
 */
package com.imooc.o2o.service.impl;

import com.imooc.o2o.cache.JedisUtil;
import com.imooc.o2o.service.CacheService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Set;

/**
 * 〈一句话功能简述〉<br> 
 * 〈〉
 *
 * @author gongyu
 * @create 2018/6/15
 * @since 1.0.0
 */
@Service
public class CacheServiceImpl implements CacheService {
    @Autowired
    private JedisUtil.Keys jedisKeys;
    @Override
    public void removeFromCache(String keyPrefix) {
        Set<String> keySet=jedisKeys.keys(keyPrefix+"*");
        for(String key:keySet){
            jedisKeys.del(key);
        }
    }
}
