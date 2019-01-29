/**
 * FileName: ImageUtil
 * Author:   gongyu
 * Date:     2018/4/13 21:41
 * Description: 图片处理
 */
package com.imooc.o2o.util;

import com.imooc.o2o.dto.ImageHolder;
import net.coobird.thumbnailator.Thumbnails;
import net.coobird.thumbnailator.geometry.Positions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

/**
 * 〈一句话功能简述〉<br>
 * 〈图片处理〉
 *
 * @author gongyu
 * @create 2018/4/13
 * @since 1.0.0
 */
public class ImageUtil {
    private static Logger logger = LoggerFactory.getLogger(ImageUtil.class);
    private static final SimpleDateFormat sDateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
    private static final Random r = new Random();
    private static String basePath = PathUtil.getImgBasePath();

    public static String generateThumbnail(ImageHolder thumbnail, String targetAddr) {
        String realFileName = getRandomFileName();
        String extension = getFileExtension(thumbnail.getImageName());
        makeDirPath(targetAddr);
        String relativeAddr = targetAddr + realFileName + extension;
        File dest = new File(PathUtil.getImgBasePath() + relativeAddr);
        try {
            Thumbnails.of(thumbnail.getImage()).size(200, 200)
                    .watermark(Positions.BOTTOM_RIGHT,
                            ImageIO.read(new File(basePath + "/upload/watermark.jpg")), 0.05f)
                    .outputQuality(0.9f).toFile(dest);
        } catch (IOException e) {
            e.printStackTrace();

        }
        return relativeAddr;
    }

    /**
     * 处理详情图，并返回新生成图片的相对路径值
     * @param thumbnail
     * @param targetAddr
     * @return
     */
    public static String  generateNormalImg(ImageHolder thumbnail, String targetAddr) {
      //获取不重复的随机名
        String realFileName = getRandomFileName();
        //获取文件的扩展名如png,jpg等
        String extension = getFileExtension(thumbnail.getImageName());
        //如果目标路径不存在，则自动创建
        makeDirPath(targetAddr);
        //获取文件存储的相对路径（带文件名）
        String relativeAddr = targetAddr + realFileName + extension;
        logger.debug("current relativeAddr is:"+relativeAddr);
        File dest = new File(PathUtil.getImgBasePath() + relativeAddr);
        logger.debug("current complete addr is:"+PathUtil.getImgBasePath()+relativeAddr);
        //调用Thumbnails生成带有水印的图片
        try {
            Thumbnails.of(thumbnail.getImage()).size(337, 640).watermark(Positions.BOTTOM_RIGHT,ImageIO.read(new File(basePath + "/upload/watermark.jpg")), 0.25f).outputQuality(0.9f).toFile(dest);
        } catch (IOException e) {
            throw new RuntimeException("创建缩略图失败：" + e.toString());
        }
        return relativeAddr;
    }

    /**
     * 获取输入文件流的扩展名
     *
     * @param
     * @return
     */
    private static String getFileExtension(String fileName) {



      return fileName.substring(fileName.lastIndexOf("."));




    }

    /**
     * 创建目标路径所涉及到的目录，即/home/work/xiangze/xxx.jpg,那么home work xiangze 这三个文件夹都得自动创建
     *
     * @param targetAddr
     */
    private static void makeDirPath(String targetAddr) {
        String realFileParentPath = PathUtil.getImgBasePath() + targetAddr;
        File dirPath = new File(realFileParentPath);
        if (!dirPath.exists()) {
            dirPath.mkdirs();
        }

    }


    /**
     * 生成随机文件名，当前年月日小时分钟秒钟+五位随机数
     *
     * @return
     */
    public static String getRandomFileName() {
        //获取随机五位数
        int rannum = r.nextInt(89999) + 10000;
        String nowTimeStr = sDateFormat.format(new Date());
        return nowTimeStr + rannum;


    }

    /**
     * storePath是文件路径还是目录的路径，
     * 如果storepath是文件路径则删除该文件，
     * 如果storepath是目录路径则删除该目录下的所有文件
     * @param storePath
     */
    public static void deleteFileOrPath(String storePath ){
        File fileOrPath=new File(PathUtil.getImgBasePath()+storePath);
        if(fileOrPath.exists()){
            if(fileOrPath.isDirectory()){
                File files[]=fileOrPath.listFiles();
                for(int i=0;i<files.length;i++){
                    files[i].delete();

                }
            }
            fileOrPath.delete();
        }
    }
//    public static void main(String[] args) throws IOException {
//
////        可以传入文件也可以传入图片流
//        Thumbnails.of(new File("c:/Users/Administrator/Desktop/xiaohuangren.jpg"))
//                .size(200, 200).watermark(Positions.BOTTOM_RIGHT,
//                ImageIO.read(new File(basePath + "/watermark.jpg")), 0.25f).outputQuality(0.8f)
//                .toFile("c:/Users/Administrator/Desktop/xiaohuangrennew.jpg");
//    }
}
