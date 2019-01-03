package com.imooc.o2o.service.impl;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.imooc.o2o.cache.JedisUtil;
import com.imooc.o2o.dao.AreaDao;
import com.imooc.o2o.entity.Area;
import com.imooc.o2o.exceptions.AreaOperationException;
import com.imooc.o2o.service.AreaService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
public class AreaServiceImpl implements AreaService {
   @Autowired
    private AreaDao areaDao;
   @Autowired
   private JedisUtil.Keys jedisKeys;
   @Autowired
   private JedisUtil.Strings jedisStrings;


   private static Logger log=LoggerFactory.getLogger(AreaServiceImpl.class);
    @Override
    @Transactional
    public List<Area> getAreaList() {
        //定义Redis的key
        String key=AREALISTKEY;
        //定义接收对象
        List<Area> areaList=null;
        //定义jackson的数据转换操作类
        ObjectMapper mapper=new ObjectMapper();
        //判断key是否存在
        if(!jedisKeys.exists(key)){
            //若不存在，则从数据库里面取出相应数据
            areaList=areaDao.queryArea();
            //将相关的实体类集合转换成string，存入redis里面对应的key中
            String jsonString= null;
            try {
                jsonString = mapper.writeValueAsString(areaList);
            } catch (JsonProcessingException e) {
                e.printStackTrace();
                log.error(e.getMessage());
                throw new AreaOperationException(e.getMessage());
            }
            jedisStrings.set(key,jsonString);
        }else {
            String jsonString=jedisStrings.get(key);
            JavaType javaType=mapper.getTypeFactory().constructParametricType(ArrayList.class,Area.class);
            try {
                areaList=mapper.readValue(jsonString,javaType);
            } catch (JsonParseException e) {
                e.printStackTrace();
                log.error(e.getMessage());
                throw new AreaOperationException(e.getMessage());
            }catch (JsonMappingException e){
                e.printStackTrace();
                log.error(e.getMessage());
                throw new AreaOperationException(e.getMessage());
            }catch (IOException e){
                e.printStackTrace();
                log.error(e.getMessage());
                throw new AreaOperationException(e.getMessage());
            }
        }
        return areaList;
    }
}
