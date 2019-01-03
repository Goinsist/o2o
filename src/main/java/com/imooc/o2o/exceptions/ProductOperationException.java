/**
 * FileName: ProductOperationException
 * Author:   gongyu
 * Date:     2018/5/12 9:12
 * Description:
 */
package com.imooc.o2o.exceptions;

/**
 * 〈一句话功能简述〉<br> 
 * 〈〉
 *
 * @author gongyu
 * @create 2018/5/12
 * @since 1.0.0
 */
public class ProductOperationException extends RuntimeException {

    private static final long serialVersionUID = 5076172298827469013L;

    public ProductOperationException(String msg){
        super(msg);
    }
}
