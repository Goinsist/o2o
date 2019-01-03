package com.imooc.o2o.service;

import com.imooc.o2o.entity.ProductSellDaily;

import java.util.Date;
import java.util.List;

public interface ProductSellDailyService {
  /**
  *
  * 功能描述:
  * 每日定时对所有店铺的商品销量进行统计
   * @return 
  * @author gongyu
  * @date 2019/1/3 0003 11:22
  */
    void dailyCalculate();
/**
*
* 功能描述:
* 根据查询条件返回商品日销售的统计列表
 * @return 
* @author gongyu
* @date 2019/1/3 0003 11:33
*/
    List<ProductSellDaily> listProductSellDaily(ProductSellDaily productSellDailyCondition, Date beginTime, Date endTime);
}
