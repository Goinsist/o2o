/**
 * FileName: ProductExecution
 * Author:   gongyu
 * Date:     2018/5/12 9:13
 * Description:
 */
package com.imooc.o2o.dto;

import com.imooc.o2o.entity.Product;
import com.imooc.o2o.enums.ProductStateEnum;

import java.util.List;

/**
 * 〈一句话功能简述〉<br> 
 * 〈〉
 *
 * @author gongyu
 * @create 2018/5/12
 * @since 1.0.0
 */

public class ProductExecution {

    //结果状态
    private int state;
    //状态标识
    private String stateInfo;

    //商品数量
    private int count;
    //操作的product(增删改商品的时候用)
    private Product product;

//   获取produt列表
    private List<Product> productList;

    public ProductExecution() {

    }

    //操作失败的构造器
    public ProductExecution(ProductStateEnum stateEnum){
        this.state=stateEnum.getState();
        this.stateInfo=stateEnum.getStateInfo();
    }

    //操作成功的构造器 针对单个商品
    public ProductExecution(ProductStateEnum stateEnum,Product product){
        this.state=stateEnum.getState();
        this.stateInfo=stateEnum.getStateInfo();
        this.product=product;
    }
    //操作成功的构造器
    public ProductExecution(ProductStateEnum stateEnum,List<Product> productList){
        this.state=stateEnum.getState();
        this.stateInfo=stateEnum.getStateInfo();
        this.productList=productList;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
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

    public List<Product> getProductList() {
        return productList;
    }

    public void setProductList(List<Product> productList) {
        this.productList = productList;
    }
}
