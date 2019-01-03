package com.imooc.o2o.service.impl;

import com.imooc.o2o.dao.UserProductMapDao;
import com.imooc.o2o.dto.UserProductMapExecution;
import com.imooc.o2o.entity.UserProductMap;
import com.imooc.o2o.service.UserProductMapService;
import com.imooc.o2o.util.PageCalculate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class UserProductMapServiceImpl implements UserProductMapService {
    @Autowired
    private UserProductMapDao userProductMapDao;

    @Override
    public UserProductMapExecution listUserProductMap(UserProductMap userProductMapCondition, Integer pageIndex, Integer pageSize) {
        //空值判断
        if (userProductMapCondition != null && pageIndex != null && pageSize != null) {
            //页转行
            int beginIndex = PageCalculate.calculateRowIndex(pageIndex, pageSize);
            //依据查询条件分页取出列表
            List<UserProductMap> userProductMapList = userProductMapDao.queryUserProductMapList(userProductMapCondition, beginIndex, pageSize);
            //按照同等的查询条件获取总数
            int count = userProductMapDao.queryUserProductMapCount(userProductMapCondition);
            UserProductMapExecution se = new UserProductMapExecution();
            se.setUserProductMapList(userProductMapList);
            se.setCount(count);
            return se;
        } else {
            return null;
        }

    }
}