/**
 * FileName: ShopExceution
 * Author:   gongyu
 * Date:     2018/4/14 21:12
 * Description: 店铺结果状态
 */
package com.imooc.o2o.dto;

import com.imooc.o2o.entity.Shop;
import com.imooc.o2o.enums.ShopStateEnum;

import java.util.List;

/**
 * 〈一句话功能简述〉<br> 
 * 〈店铺结果状态〉
 *
 * @author gongyu
 * @create 2018/4/14
 * @since 1.0.0
 */
public class ShopExceution {
    //结果状态
    private int state;
    //状态标识;
    private String stateInfo;

    //店铺数量
    private int count;

    //操作的shop(增删改店铺的时候用到)
    private Shop shop;

    //shop列表(查询店铺列表的时候使用)
   private List<Shop> shopList;
   public  ShopExceution(){

   }
 //店铺操作失败的构造器
   public  ShopExceution(ShopStateEnum stateEnum){
        this.state=stateEnum.getState();
        this.stateInfo=stateEnum.getStateInfo();

   }
   //店铺操作成功的时候使用的构造器
   public  ShopExceution(ShopStateEnum stateEnum,Shop shop){
       this.state=stateEnum.getState();
       this.stateInfo=stateEnum.getStateInfo();
        this.shop=shop;
   }
    //店铺操作成功的时候使用的构造器
    public  ShopExceution(ShopStateEnum stateEnum,List<Shop> shop){
        this.state=stateEnum.getState();
        this.stateInfo=stateEnum.getStateInfo();
        this.shopList=shopList;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public String getStateInfo() {
        return stateInfo;
    }

    public void setStateInfo(String stateInfo) {
        this.stateInfo = stateInfo;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public Shop getShop() {
        return shop;
    }

    public void setShop(Shop shop) {
        this.shop = shop;
    }

    public List<Shop> getShopList() {
        return shopList;
    }

    public void setShopList(List<Shop> shopList) {
        this.shopList = shopList;
    }
}
