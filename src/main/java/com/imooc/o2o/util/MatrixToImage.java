package com.imooc.o2o.util;

import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLEncoder;

public class MatrixToImage {
public static void QRCodetoImage(String urlPrefix, String urlMiddle, String content, String urlSuffix, String changeUrl, HttpServletResponse response) {
    //将content的信息先进行base64编码以避免特殊字符造成的干扰，之后拼接目标url
    try {
        String longUrl = urlPrefix + changeUrl + urlMiddle + URLEncoder.encode(content, "UTF-8") + urlSuffix;
        //将目标url转换成短的URL
        String shortUrl = ShortNetAddress.getShortURL(longUrl);
        //调用二维码生成的工具类方法，传入短的url，生成二维码
        BitMatrix qRcodeImg = CodeUtil.generateQRcodeStream(shortUrl, response);
        //将二维码以图片流的形式输出到前端
        MatrixToImageWriter.writeToStream(qRcodeImg, "png", response.getOutputStream());
    }catch (IOException e){
        e.printStackTrace();
    }
}
}
