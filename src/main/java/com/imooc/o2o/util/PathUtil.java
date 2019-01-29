/**
 * FileName: PathUtil
 * Author:   gongyu
 * Date:     2018/4/13 22:15
 * Description: 地址处理工具
 */
package com.imooc.o2o.util;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

/**
 * 〈一句话功能简述〉<br> 
 * 〈地址处理工具〉
 *
 * @author gongyu
 * @create 2018/4/13
 * @since 1.0.0
 */
@Configuration
public class PathUtil {
//    file.separator:获取文件分隔符
    private static String seperator=System.getProperty("file.separator");
    private static String winPath;
    private static String linuxPath;
    private static String shopPath;
@Value("${win.base.path}")
    public  void setWinPath(String winPath) {
        PathUtil.winPath = winPath;
    }
@Value("${linux.base.path}")
    public  void setLinuxPath(String linuxPath) {
        PathUtil.linuxPath = linuxPath;
    }
@Value("${shop.relevant.path}/")
    public  void setShopPath(String shopPath) {
        PathUtil.shopPath = shopPath;
    }

    /**
     * 返回项目图片根路径
     * @return
     */
    public static String getImgBasePath(){
//os.name获取系统名称
        String os=System.getProperty("os.name");
        String basePath="";
        if(os.toLowerCase().startsWith("win")){
            basePath=winPath;
        }else{
            basePath=linuxPath;
        }
        basePath=basePath.replace("/",seperator);
       return basePath;
    }

    /**
     * 依据不同的业务需求返回子路径
     * @param shopId
     * @return
     */
    public static String getShopImagePath(long shopId){
            String imagePath=shopPath+ shopId + seperator;
            return imagePath.replace("/",seperator);
    }
}
