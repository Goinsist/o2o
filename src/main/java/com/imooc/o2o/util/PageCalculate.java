/**
 * FileName: PageCalculate
 * Author:   gongyu
 * Date:     2018/5/9 18:12
 * Description:
 */
package com.imooc.o2o.util;

/**
 * 〈一句话功能简述〉<br> 
 * 〈〉
 *
 * @author gongyu
 * @create 2018/5/9
 * @since 1.0.0
 */
public class PageCalculate {
    public static int calculateRowIndex(int pageIndex,int pageSize){
        return (pageIndex>0)?(pageIndex-1)*pageSize:0;
    }
}
