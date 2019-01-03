/**
 * FileName: ProductCategoryOperationException
 * Author:   gongyu
 * Date:     2018/5/10 15:34
 * Description:
 */
package com.imooc.o2o.exceptions;

/**
 * 〈一句话功能简述〉<br> 
 * 〈〉
 *
 * @author gongyu
 * @create 2018/5/10
 * @since 1.0.0
 */
public class ProductCategoryOperationException extends  RuntimeException{


    private static final long serialVersionUID = 1182563719599527969L;

    public ProductCategoryOperationException(String msg){
        super(msg);
    }
}
