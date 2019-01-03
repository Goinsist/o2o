/**
 * FileName: WechatAuthOperationException
 * Author:   gongyu
 * Date:     2018/6/15 12:29
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
public class WechatAuthOperationException extends RuntimeException {
    private static final long serialVersionUID = 4315866839048210269L;
   public WechatAuthOperationException(String msg){
       super(msg);
   }
}
