/**
 * FileName: ShopCategoryDaoTest
 * Author:   gongyu
 * Date:     2018/5/5 14:32
 * Description:
 */
package com.imooc.o2o.dao;


import com.imooc.o2o.entity.ShopCategory;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

/**
 * 〈一句话功能简述〉<br> 
 * 〈〉
 *
 * @author gongyu
 * @create 2018/5/5
 * @since 1.0.0
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class ShopCategoryDaoTest  {
@Autowired
    private ShopCategoryDao shopCategoryDao;
@Test
    public  void testQueryShopCategory(){
    List<ShopCategory> shopCategoryList=shopCategoryDao.queryShopCategory(null);

    System.out.println(shopCategoryList.size());
}
}
