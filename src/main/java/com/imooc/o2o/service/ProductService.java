package com.imooc.o2o.service;

import com.imooc.o2o.dto.ImageHolder;
import com.imooc.o2o.dto.ProductExecution;
import com.imooc.o2o.entity.Product;
import com.imooc.o2o.entity.ProductImg;
import com.imooc.o2o.exceptions.ProductOperationException;

import java.util.List;

public interface ProductService {
    /**
     * 添加商品信息以及图片处理
     * @return
     * @throws ProductOperationException
     */
    ProductExecution addProduct(Product product, ImageHolder thumbnail,
                                List<ImageHolder> productImgList) throws ProductOperationException;


    /**
     * 查询商品列表分页，可输入的条件有：商品名（模糊），商品状态，店铺id，商品类别
     * @param productCondition
     * @param pageIndex
     * @param pageSize
     * @return
     */
    ProductExecution getProductList(Product productCondition, int pageIndex, int pageSize);

    /**
     * 通过商品id查询唯一的商品信息
     * @param productId
     * @return
     */
    Product getProductById(long productId);

    /**
    *
    * 功能描述:
    * 查询该商品下的所有图片信息
     * @return
    * @author gongyu
    * @date 2019/1/18 0018 23:50
    */
    List<ProductImg> queryProductImgList(long productId);

    /**
     * 修改商品信息以及图片处理
     * @param product
     * @param thumbnail
     * @param productImageHolderList
     * @return
     * @throws ProductOperationException
     */
    ProductExecution modifyProduct(Product product, ImageHolder thumbnail, List<ImageHolder> productImageHolderList)throws ProductOperationException;
}
