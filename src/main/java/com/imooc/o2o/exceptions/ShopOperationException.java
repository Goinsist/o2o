/**
 * FileName: ShopOperationException
 * Author:   gongyu
 * Date:     2018/5/1 9:58
 * Description:
 */
package com.imooc.o2o.exceptions;

/**
 * 〈一句话功能简述〉<br> 
 * 〈〉
 *
 * @author gongyu
 * @create 2018/5/1
 * @since 1.0.0
 */
public class ShopOperationException extends  RuntimeException{
    private static final long serialVersionUID = 2361446884822298905L;

    public ShopOperationException(String msg){
        super(msg);
    }
}
