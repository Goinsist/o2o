/**
 * FileName: ProductCategoryServiceImpl
 * Author:   gongyu
 * Date:     2018/5/10 12:19
 * Description:
 */
package com.imooc.o2o.service.impl;

import com.imooc.o2o.dao.ProductCategoryDao;
import com.imooc.o2o.dao.ProductDao;
import com.imooc.o2o.dto.ProductCategoryExecution;
import com.imooc.o2o.entity.ProductCategory;
import com.imooc.o2o.enums.ProductCategoryStateEnum;
import com.imooc.o2o.exceptions.ProductCategoryOperationException;
import com.imooc.o2o.service.ProductCategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 〈一句话功能简述〉<br> 
 * 〈〉
 *
 * @author gongyu
 * @create 2018/5/10
 * @since 1.0.0
 */
@Service
public class ProductCategoryServiceImpl implements ProductCategoryService {
   @Autowired
   private ProductCategoryDao productCategoryDao;
   @Autowired
   private ProductDao productDao;
    @Override
    public List<ProductCategory> queryProductCategory(Long shopId) {
       List<ProductCategory> listProductCategory= productCategoryDao.queryProductCategory(shopId);


        return listProductCategory;
    }

    @Override
    @Transactional
    public ProductCategoryExecution batchAddProductCategory(List<ProductCategory> productCategoryList) throws ProductCategoryOperationException {

        if(productCategoryList!=null&&productCategoryList.size()>0){
            try{
                int effectedNum=productCategoryDao.batchInsertProductCategory(productCategoryList);
                if(effectedNum<=0){
                    throw new ProductCategoryOperationException("店铺类别创建失败");
                }else {
                    return new ProductCategoryExecution(ProductCategoryStateEnum.SUCCESS);
                }
            }catch (Exception e){
                throw new ProductCategoryOperationException("batchAddProductCategory error:"+e.getMessage());
            }


        }else {
            return new ProductCategoryExecution(ProductCategoryStateEnum.EMPTY_LIST);
        }


    }

    @Override
    @Transactional
    public ProductCategoryExecution deleteProductCategory(long productCategoryId, long shopId) throws ProductCategoryOperationException {
       //解除tb_product里的商品与该producategoryId的关联
        try {
            int effectedNum=productDao.updateProductCategoryToNull(productCategoryId);
            if(effectedNum<0){
                throw  new RuntimeException("商品类别更新失败");
            }
        }catch (Exception e){
            throw new RuntimeException("deleteProductCategory error:"+e.getMessage());
        }
//删除该productCategory

        try{
            int effectedNum=productCategoryDao.deleteProductCategory(productCategoryId,shopId);
          if(effectedNum<=0){
              throw new ProductCategoryOperationException("商品类别删除失败");
          }else {
              return new ProductCategoryExecution(ProductCategoryStateEnum.SUCCESS);
          }
        }catch (ProductCategoryOperationException e){
            throw new ProductCategoryOperationException("deleteProductCategory error:"+e.getMessage());
        }


    }
}
