/**
 * FileName: AreaOperationException
 * Author:   gongyu
 * Date:     2018/6/15 15:42
 * Description:
 */
package com.imooc.o2o.exceptions;

/**
 * 〈一句话功能简述〉<br> 
 * 〈〉
 *
 * @author gongyu
 * @create 2018/6/15
 * @since 1.0.0
 */
public class AreaOperationException extends RuntimeException {
    private static final long serialVersionUID = 2244068273735820249L;
    public AreaOperationException(String msg){
        super(msg);
    }
}
