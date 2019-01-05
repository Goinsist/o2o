package com.imooc.o2o.service;

import com.imooc.o2o.dto.UserAwardMapExecution;
import com.imooc.o2o.entity.UserAwardMap;
import com.imooc.o2o.exceptions.UserAwardMapOperationException;

public interface UserAwardMapService {

    /**
    *
    * 功能描述:
    * 根据传入的查询条件分页获取映射列表及总数
     * @return 
    * @author gongyu
    * @date 2019/1/4 0004 10:16
    */
    UserAwardMapExecution listUserAwardMap(UserAwardMap userAwardMapCondition,Integer pageIndex,Integer pageSize);

/**
*
* 功能描述:
* 根据传入的id获取映射信息
 * @return 
* @author gongyu
* @date 2019/1/4 0004 10:19
*/
UserAwardMap getUserAwardMapById(Long userAwardMapId);
/**
*
* 功能描述:
* 领取奖品，添加映射信息
 * @return 
* @author gongyu
* @date 2019/1/4 0004 14:47
*/
UserAwardMapExecution addUserAwardMap(UserAwardMap userAwardMap);

/**
*
* 功能描述:
* 修改映射信息，这里主要修改奖品领取状态
 * @return 
* @author gongyu
* @date 2019/1/5 0005 12:34
*/
UserAwardMapExecution modifyUserAwardMap(UserAwardMap userAwardMap)throws UserAwardMapOperationException;
}
