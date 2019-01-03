package com.imooc.o2o.dao;

import com.imooc.o2o.entity.ProductCategory;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface ProductCategoryDao {

    /**
     * 批量新增商品类别
     * @param productCategoryList
     * @return
     */
    int batchInsertProductCategory(List<ProductCategory> productCategoryList);

    /**
     * 根据传入shopId查询商品分类信息
     * @param shopId
     * @return
     */
    List<ProductCategory> queryProductCategory(Long shopId);

    /**
     * 删除指定商品类别
     * @param productCategory
     * @param shopId
     * @return
     */
    int deleteProductCategory(@Param("productCategoryId") long productCategory, @Param("shopId") Long shopId);
}
