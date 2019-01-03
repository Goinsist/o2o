/**
 * FileName: PersonInfoServiceImpl
 * Author:   gongyu
 * Date:     2018/6/15 13:00
 * Description:
 */
package com.imooc.o2o.service.impl;

import com.imooc.o2o.dao.PersonInfoDao;
import com.imooc.o2o.entity.PersonInfo;
import com.imooc.o2o.service.PersonInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 〈一句话功能简述〉<br> 
 * 〈〉
 *
 * @author gongyu
 * @create 2018/6/15
 * @since 1.0.0
 */
@Service
public class PersonInfoServiceImpl implements PersonInfoService {
 @Autowired
 private PersonInfoDao personInfoDao;

    @Override
    public PersonInfo getPersonInfoById(Long userId) {
        return personInfoDao.queryPersonInfoById(userId);
    }
}
