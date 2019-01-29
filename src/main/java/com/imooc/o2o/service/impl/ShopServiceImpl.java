/**
 * FileName: ShopServiceImpl
 * Author:   gongyu
 * Date:     2018/5/1 9:27
 * Description:
 */
package com.imooc.o2o.service.impl;

import com.imooc.o2o.dao.ShopAuthMapDao;
import com.imooc.o2o.dao.ShopDao;
import com.imooc.o2o.dto.ImageHolder;
import com.imooc.o2o.dto.ShopExceution;
import com.imooc.o2o.entity.Shop;
import com.imooc.o2o.entity.ShopAuthMap;
import com.imooc.o2o.enums.ShopStateEnum;
import com.imooc.o2o.exceptions.ShopOperationException;
import com.imooc.o2o.service.ShopService;
import com.imooc.o2o.util.ImageUtil;
import com.imooc.o2o.util.PageCalculate;
import com.imooc.o2o.util.PathUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

/**
 * 〈一句话功能简述〉<br>
 * 〈〉
 *
 * @author gongyu
 * @create 2018/5/1
 * @since 1.0.0
 */
@Service
public class ShopServiceImpl implements ShopService {
    @Autowired
    private ShopDao shopDao;
    @Autowired
    private ShopAuthMapDao shopAuthMapDao;

    @Override
    public ShopExceution getShopList(Shop shopCondition, int pageIndex, int pageSize) {
        //将页码转换成行码
       int rowIndex=PageCalculate.calculateRowIndex(pageIndex,pageSize);
       //依据查询条件，调用dao层返回相关的店铺列表
        List<Shop> shopList=shopDao.queryShopList(shopCondition,rowIndex,pageSize);
        //依据相同的查询条件，返回店铺总数
        int count=shopDao.queryShopCount(shopCondition);
        ShopExceution se=new ShopExceution();
        if(shopList!=null){
            se.setShopList(shopList);
            se.setCount(count);
        }else {
            se.setState(ShopStateEnum.INNER_ERROR.getState());
        }
        return se;
    }

    @Override
@Transactional
    public ShopExceution addShop(Shop shop,ImageHolder thumbnail) throws ShopOperationException {
//     空值判断，判断shop是不是包含必须的值

        if (shop == null) {
            return new ShopExceution(ShopStateEnum.NULL_SHOP);
        }
            try {
//            给店铺信息附初始值
                shop.setEnableStatus(ShopStateEnum.CHECK.getState());
                shop.setCreateTime(new Date());
                shop.setLastEditTime(new Date());

                int effectedNum;
                //添加店铺信息
try {
    effectedNum = shopDao.insertShop(shop);
}catch (Exception e) {
    throw new ShopOperationException("insert失败:" + e.getMessage());
}


                    if (effectedNum <= 0) {
//                ShopOperationException事务会回滚
                        throw new ShopOperationException("店铺创建失败");

                    } else {
                        if (thumbnail.getImage() != null) {
//                    存储图片
                            try {
                                addshopImg(shop, thumbnail);
                            } catch (Exception e) {
                                throw new ShopOperationException("addshopImgInputStream error" + e.getMessage());
                            }
                            //更新店铺的图片地址
                            effectedNum = shopDao.updateShop(shop);
                            if (effectedNum <= 0) {
                                throw new ShopOperationException("更新图片地址失败");
                            }
                            //执行增加shopAuthMap操作
                            ShopAuthMap shopAuthMap=new ShopAuthMap();
                            shopAuthMap.setEmployee(shop.getOwner());
                            shopAuthMap.setShop(shop);
                            shopAuthMap.setTitle("店家");
                            shopAuthMap.setTitleFlag(0);
                            shopAuthMap.setCreateTime(new Date());
                            shopAuthMap.setLastEditTime(new Date());
                            shopAuthMap.setEnableStatus(1);
                            try{
                                effectedNum=shopAuthMapDao.insertShopAuthMap(shopAuthMap);
                                if(effectedNum<=0){
                                    throw new ShopOperationException("授权创建失败");
                                }
                            }catch (Exception e){
                                throw  new ShopOperationException("insertShopAuthMap error:"+e.getMessage());
                            }
                        }
                    }

        } catch (Exception e) {
            throw new ShopOperationException("addShop error：" + e.getMessage());
        }
        return new ShopExceution(ShopStateEnum.CHECK, shop);
    }

    @Override
    public Shop getByShopId(long shopId) {
        return shopDao.queryByShopId(shopId);
    }

    @Override
    @Transactional
    public ShopExceution modifyShop(Shop shop, ImageHolder thumbnail) throws ShopOperationException {
        if (shop == null || shop.getShopId() == null) {
            return new ShopExceution(ShopStateEnum.NULL_SHOP);
        } else {
// 1.判断是否需要处理图片
            try {
                if (thumbnail!=null&&thumbnail.getImage() != null && thumbnail.getImageName()!= null && !"".equals(thumbnail.getImageName())) {
                    Shop tempShop = shopDao.queryByShopId(shop.getShopId());
                    if (tempShop.getShopImg() != null) {
                        ImageUtil.deleteFileOrPath(tempShop.getShopImg());
                    }
                    addshopImg(shop, thumbnail);
                }else {

                }

//        2.更新店铺信息
                shop.setLastEditTime(new Date());
                int effectedNum = shopDao.updateShop(shop);
                if (effectedNum <= 0) {
                    return new ShopExceution(ShopStateEnum.INNER_ERROR);
                } else {
                    shop = shopDao.queryByShopId(shop.getShopId());
                    return new ShopExceution(ShopStateEnum.SUCCESS, shop);
                }
            } catch (Exception e) {
                    throw new ShopOperationException("modifyShop error:"+e.getMessage());
            }


        }
    }

    private void addshopImg(Shop shop, ImageHolder thumbnail) {
//        获取shop图片目录的相对值路径
        try {

            String dest = PathUtil.getShopImagePath(shop.getShopId());
            String shopImgInputStreamAddr = ImageUtil.generateThumbnail(thumbnail, dest);
            shop.setShopImg(shopImgInputStreamAddr);

        } catch (Exception e) {
            e.printStackTrace();
        }


    }
}
