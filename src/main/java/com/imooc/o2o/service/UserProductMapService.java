package com.imooc.o2o.service;

import com.imooc.o2o.dto.UserProductMapExecution;
import com.imooc.o2o.entity.UserProductMap;
import com.imooc.o2o.exceptions.UserProductMapOperationException;

public interface UserProductMapService {
/**
*
* 功能描述:
* 通过传入的查询条件分页列出用户消费信息列表
 * @return 
* @author gongyu
* @date 2019/1/3 0003 11:23
*/
    UserProductMapExecution listUserProductMap(UserProductMap userProductMapCondition, Integer pageIndex, Integer pageSize);
/*添加消费记录
 */
UserProductMapExecution addUserProductMap(UserProductMap userProductMap)throws UserProductMapOperationException;
UserProductMapExecution getUserProductMapByProductId(Long productId);
}
