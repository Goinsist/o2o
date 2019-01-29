/**
 * FileName: ShopDaoTest
 * Author:   gongyu
 * Date:     2018/4/13 20:30
 * Description: 商店测试类
 */
package com.imooc.o2o.dao;

import com.imooc.o2o.entity.Area;
import com.imooc.o2o.entity.PersonInfo;
import com.imooc.o2o.entity.Shop;
import com.imooc.o2o.entity.ShopCategory;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Date;
import java.util.List;

import static org.junit.Assert.assertEquals;

/**
 * 〈一句话功能简述〉<br> 
 * 〈商店添加测试类〉
 *
 * @author gongyu
 * @create 2018/4/13
 * @since 1.0.0
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class ShopDaoTest  {
    @Autowired
    private ShopDao shopDao;
    @Test
    public void testQueryShopListAndCount(){
        Shop shopCondition=new Shop();
            ShopCategory childCategory=new ShopCategory();
            ShopCategory parentCategory=new ShopCategory();
            parentCategory.setShopCategoryId(4L);
            childCategory.setParent(parentCategory);
            shopCondition.setShopCategory(childCategory);

        List<Shop> shopList=shopDao.queryShopList(shopCondition,0,6);
        int count=shopDao.queryShopCount(shopCondition);
        System.out.println("店铺列表的大小:"+shopList.size());
        System.out.println("店铺总数："+count);




    }
//    @Test
//    @Ignore
//    public void testQueryByShopId(){
//        long shopId=1;
//        Shop shop=shopDao.queryByShopId(shopId);
//        System.out.println("areaId:"+shop.getArea().getAreaId());
//        System.out.println("areaName:"+shop.getArea().getAreaName());
//    }
//    @Test
//    @Ignore
//    public  void testInsertShop(){
//        Shop shop=new Shop();
//        PersonInfo owner=new PersonInfo();
//        Area area=new Area();
//        ShopCategory shopCategory=new ShopCategory();
//        owner.setUserId(1L);
//        area.setAreaId(2);
//        shopCategory.setShopCategoryId(1L);
//        shop.setOwner(owner);
//        shop.setArea(area);
//        shop.setShopCategory(shopCategory);
//        shop.setShopName("测试的店铺");
//        shop.setShopDesc("test");
//        shop.setShopAddr("test");
//        shop.setPhone("test");
//        shop.setShopImg("test");
//        shop.setCreateTime(new Date());
//        shop.setEnableStatus(1);
//        shop.setAdvice("审核中");
//        int effectedNum=shopDao.insertShop(shop);
//        assertEquals(1,effectedNum);



    //}
    @Test
    @Ignore
    public  void testUpdateShop(){
        Shop shop=new Shop();
        shop.setShopId(1L);

        shop.setShopDesc("测试描述324");
        shop.setShopAddr("测试地址5232");
        shop.setLastEditTime(new Date());
        int effectedNum=shopDao.updateShop(shop);
        assertEquals(1,effectedNum);



    }


}
