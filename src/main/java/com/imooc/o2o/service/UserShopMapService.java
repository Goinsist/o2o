package com.imooc.o2o.service;

import com.imooc.o2o.dto.UserShopMapExecution;
import com.imooc.o2o.entity.UserShopMap;

public interface UserShopMapService {
    /**
    *
    * 功能描述:
    * 根据传入的查询信息分页查询用户积分列表
     * @return 
    * @author gongyu
    * @date 2019/1/3 0003 17:46
    */
UserShopMapExecution listUserShopMap(UserShopMap userShopMapCondition,Integer pageIndex,Integer pageSize );

/**
*
* 功能描述:
* 根据用户id和店铺id返回该用户在某个店铺的积分情况
 * @return 
* @author gongyu
* @date 2019/1/3 0003 17:48
*/
UserShopMap getUserShopMap(Long userId,Long shopId);
}
