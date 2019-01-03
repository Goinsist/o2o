package com.imooc.o2o.service;

import com.imooc.o2o.dto.ImageHolder;
import com.imooc.o2o.dto.ShopExceution;
import com.imooc.o2o.entity.Shop;
import com.imooc.o2o.exceptions.ShopOperationException;


public interface ShopService {
    /**
     * 根据shopCondition分页返回相应数据
     * @param shopCondition
     * @param pageIndex
     * @param pageSize
     * @return
     */
    public ShopExceution getShopList(Shop shopCondition, int pageIndex, int pageSize);
    ShopExceution addShop(Shop shop, ImageHolder thumbnail)throws ShopOperationException;

    /**
     * 通过店铺id获取店铺信息
     * @param shopId
     * @return
     */
   Shop getByShopId(long shopId);

    /**
     *更新店铺信息，包括对图片的处理
     * @param shop
     * @param thumbnail

     * @return
     * @throws ShopOperationException
     */
   ShopExceution modifyShop(Shop shop, ImageHolder thumbnail)throws ShopOperationException;


}
