/**
 * FileName: PersonInfoService
 * Author:   gongyu
 * Date:     2018/6/15 12:58
 * Description:
 */
package com.imooc.o2o.service;

import com.imooc.o2o.entity.PersonInfo;

/**
 * 〈一句话功能简述〉<br> 
 * 〈〉
 *
 * @author gongyu
 * @create 2018/6/15
 * @since 1.0.0
 */
public interface  PersonInfoService {

    /**
     * 根据用户id获取personInfo信息
     * @param userId
     * @return
     */
    PersonInfo getPersonInfoById(Long userId);
}
