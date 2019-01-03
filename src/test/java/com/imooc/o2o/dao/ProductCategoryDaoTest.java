/**
 * FileName: ProductCategoryDaoTest
 * Author:   gongyu
 * Date:     2018/5/10 15:12
 * Description:
 */
package com.imooc.o2o.dao;


import com.imooc.o2o.entity.ProductCategory;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.junit.Assert.assertEquals;

/**
 * 〈一句话功能简述〉<br> 
 * 〈〉
 *
 * @author gongyu
 * @create 2018/5/10
 * @since 1.0.0
 */
//控制测试方法顺序执行
@RunWith(SpringRunner.class)
@SpringBootTest
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class ProductCategoryDaoTest {
@Autowired
private ProductCategoryDao productCategoryDao;
@Test
public void testBQueryByShopId()throws Exception{
    long shopId=1;
    List<ProductCategory> productCategoryList=productCategoryDao.queryProductCategory(shopId);
    System.out.println("该店铺自定义类别数为:"+productCategoryList.size());
}
@Test

    public void testABatchInsertProductCategory(){
        ProductCategory productCategory=new ProductCategory();
        productCategory.setProductCategoryName("商品类别1");
        productCategory.setPriority(1);
        productCategory.setCreateTime(new Date());
        productCategory.setShopId(1L);
        ProductCategory productCategory2=new ProductCategory();
        productCategory2.setProductCategoryName("商品类别2");
        productCategory2.setPriority(2);
        productCategory2.setCreateTime(new Date());
        productCategory2.setShopId(1L);
        List<ProductCategory> productCategoryList=new ArrayList<ProductCategory>();
        productCategoryList.add(productCategory);
        productCategoryList.add(productCategory2);
        int effectedNum=productCategoryDao.batchInsertProductCategory(productCategoryList);

   assertEquals(2,effectedNum);

    }
    @Test
    public void testCDeleteProductCategory()throws Exception{
        long shopId=1;
        List<ProductCategory> productCategoryList=productCategoryDao.queryProductCategory(shopId);
        for(ProductCategory pc:productCategoryList){
            if("商品类别1".equals(pc.getProductCategoryName())||"商品类别2".equals(pc.getProductCategoryName())){
                int effectedNum=productCategoryDao.deleteProductCategory(pc.getProductCategoryId(),shopId);
            assertEquals(1,effectedNum);
            }
        }

    }
}
