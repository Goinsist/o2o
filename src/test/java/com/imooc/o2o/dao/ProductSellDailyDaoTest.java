package com.imooc.o2o.dao;

import ch.qos.logback.core.encoder.EchoEncoder;
import com.imooc.o2o.entity.ProductSellDaily;
import com.imooc.o2o.entity.Shop;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
@SpringBootTest
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class ProductSellDailyDaoTest {
    @Autowired
    private ProductSellDailyDao productSellDailyDao;


    /**
     * 测试添加功能
     * @throws Exception
     */
    @Test
    public void testAInsertProductSellDaily()throws Exception{
        //创建商品日销量统计
        int effectedNum=productSellDailyDao.insertProductSellDaily();
        assertEquals(2,effectedNum);
    }
    /**
    *
    * 功能描述:
    * 测试添加功能
     * @return
    * @author gongyu
    * @date 2019/1/3 0003 14:06
    */
    @Test
    public void testBInsertDefaultProductSellDaily()throws Exception{
        //创建商品日销量统计
        int effectedNum=productSellDailyDao.insertDefaultProductSellDaily();
        assertEquals(1,effectedNum);
    }

    /**
     * 测试查询功能
     * @throws Exception
     */
    @Test
    public void testBQueryProductSellDaily()throws Exception{
        ProductSellDaily productSellDaily=new ProductSellDaily();
        //叠加店铺去查询
        Shop shop=new Shop();
        shop.setShopId(15L);
        productSellDaily.setShop(shop);
        List<ProductSellDaily> productSellDailyList=productSellDailyDao.queryProductSellDailyList(productSellDaily,null,null);
        assertEquals(2,productSellDailyList.size());



    }

}
