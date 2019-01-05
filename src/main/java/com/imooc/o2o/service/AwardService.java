package com.imooc.o2o.service;

import com.imooc.o2o.dto.AwardExecution;
import com.imooc.o2o.dto.ImageHolder;
import com.imooc.o2o.entity.Award;

public interface AwardService {

    /**
    *
    * 功能描述:
    * 根据传入的条件分页返回奖品列表，并返回该查询条件下的总数
     * @return
    * @author gongyu
    * @date 2019/1/4 0004 11:01
    */
    AwardExecution getAwardList(Award awardCondition,Integer pageIndex,Integer pageSize);
/**
*
* 功能描述:
* 根据awardId查询奖品信息
 * @return
* @author gongyu
* @date 2019/1/4 0004 11:01
*/
Award getAwardById(Long awardId);

/**
*
* 功能描述:
* 添加奖品信息，并添加奖品图片
 * @return 
* @author gongyu
* @date 2019/1/4 0004 11:02
*/
AwardExecution addAward(Award award, ImageHolder thumbnail);
/**
*
* 功能描述:
* 根据传入的奖品实例修改对应的奖品信息，若传入图片则替换掉原先的图片
 * @return 
* @author gongyu
* @date 2019/1/4 0004 11:04
*/
AwardExecution modifyAward(Award award,ImageHolder thumbnail);
}
