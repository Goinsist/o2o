/**
 * FileName: ImageHolder
 * Author:   gongyu
 * Date:     2018/5/12 9:27
 * Description:
 */
package com.imooc.o2o.dto;

import java.io.InputStream;

/**
 * 〈一句话功能简述〉<br> 
 * 〈〉
 *
 * @author gongyu
 * @create 2018/5/12
 * @since 1.0.0
 */
public class ImageHolder {
    private String imageName;
    private InputStream image;

    public ImageHolder(String imageName,InputStream image ){
        this.imageName=imageName;
        this.image=image;
    }

    public String getImageName() {
        return imageName;
    }

    public void setImageName(String imageName) {
        this.imageName = imageName;
    }

    public InputStream getImage() {
        return image;
    }

    public void setImage(InputStream image) {
        this.image = image;
    }
}
