/**
 * FileName: WechatUtil
 * Author:   gongyu
 * Date:     2018/6/14 11:15
 * Description:
 */
package com.imooc.o2o.util.wechat;

/**
 * 〈一句话功能简述〉<br> 
 * 〈〉
 *
 * @author gongyu
 * @create 2018/6/14
 * @since 1.0.0
 */


import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.imooc.o2o.dto.UserAccessToken;
import com.imooc.o2o.dto.WechatUser;
import com.imooc.o2o.entity.PersonInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import java.io.*;
import java.net.ConnectException;
import java.net.URL;

/**
 * 微信工具类
 */
public class WechatUtil {
    private static Logger log=LoggerFactory.getLogger(WechatUtil.class);
public static UserAccessToken getUserAcessToken(String code )throws IOException{
    //测试号信息里的appId
    String appId="wx029844ea402328c3";
    log.debug("appId:"+appId);
    //测试号信息里的appsecret
    String appsecret="597ef46d1c7905a107be4c12d19c13fa";
    log.debug("secret:"+appsecret);
    //根据传入的code，拼接处访问微信定义好的接口的URL
    String url="https://api.weixin.qq.com/sns/oauth2/access_token?appid="+appId+"&secret="+appsecret
            +"&code="+code+"&grant_type=authorization_code";
    //向相应URL发送请求获取token json字符串
    String tokenStr=httpsRequest(url,"GET",null);
    log.debug("userAccessToken:"+tokenStr);
    UserAccessToken token=new UserAccessToken();
    ObjectMapper objectMapper=new ObjectMapper();
    try {
        //将json字符串转换成相应对象
        token=objectMapper.readValue(tokenStr,UserAccessToken.class);
    }catch (JsonParseException e){
        log.error("获取用户accessToken失败："+e.getMessage());
        e.printStackTrace();
    }catch (JsonMappingException e){
        log.error("获取用户accessToken失败:"+e.getMessage());

    }catch (IOException e){
        log.error("获取用户accessToken失败");
        return null;
    }
    return token;
    }


    public static WechatUser getUserInfo(String accessToken, String openId){
    //根据传入的accessToken以及openId拼接处访问微信定义的端口并获取用户信息url
        String url="https://api.weixin.qq.com/sns/userinfo?access_token="+accessToken
                +"&openid="+openId+"&lang=zh_CN";
        //访问该URL获取用户信息json字符串
        String userStr=httpsRequest(url,"GET",null);
        log.debug("user info:"+userStr);
        WechatUser user=new WechatUser();
        ObjectMapper objectMapper=new ObjectMapper();
        try{
            //将json字符串转换成相应对象
            user=objectMapper.readValue(userStr,WechatUser.class);
        }catch (JsonParseException e){
            log.error("获取用户信息失败："+e.getMessage());
            e.printStackTrace();
        }catch (JsonMappingException e){
            log.error("获取用户信息失败:"+e.getMessage());
            e.printStackTrace();
        }catch (IOException e){
            log.error("获取用户信息失败:"+e.getMessage());
            e.printStackTrace();
        }
        if(user==null){
            log.error("获取用户信息失败.");
            return null;
        }
        return user;

    }

    /**
     * 发起https请求并获取结果
     * @param requestUrl
     * @param requestMethod
     * @param outputStr
     * @return
     */
    public static String httpsRequest(String requestUrl,String requestMethod,String outputStr){
StringBuffer buffer=new StringBuffer();
try {
    //创建SSLContext对象，并使用我们制定的信任管理器初始化
    TrustManager[] tm={new MyX509TrustManager()};
    SSLContext sslContext=SSLContext.getInstance("SSL","SunJSSE");
    sslContext.init(null,tm,new java.security.SecureRandom());
//从上述SSLContext对象中得到SSLSocketFactory对象
    SSLSocketFactory ssf=sslContext.getSocketFactory();
    URL url=new URL(requestUrl);
    HttpsURLConnection httpsURLConn=(HttpsURLConnection)url.openConnection();
    httpsURLConn.setSSLSocketFactory(ssf);
    httpsURLConn.setDoOutput(true);
    httpsURLConn.setDoInput(true);
    httpsURLConn.setUseCaches(false);
    //设置请求方式(GET/POST)
    httpsURLConn.setRequestMethod(requestMethod);
    if("GET".equalsIgnoreCase(requestMethod)){
        httpsURLConn.connect();}
    //当有数据需要提交时
    if(null!=outputStr){
        OutputStream outputStream=httpsURLConn.getOutputStream();
        //注意编码格式，防治中文乱码
        outputStream.write(outputStr.getBytes("UTF-8"));
        outputStream.close();
    }
    //将返回的输入流转换成字符串
    InputStream inputStream=httpsURLConn.getInputStream();
    InputStreamReader inputStreamReader=new InputStreamReader(inputStream,"utf-8");
    BufferedReader bufferedReader=new BufferedReader(inputStreamReader);
    String str=null;
    while ((str=bufferedReader.readLine())!=null){
        buffer.append(str);
    }
    bufferedReader.close();
    inputStreamReader.close();
    //释放资源
    inputStream.close();
    inputStream=null;
    httpsURLConn.disconnect();
    log.debug("https buffer:"+buffer.toString());
}catch (ConnectException e){
    log.error("Weixin server connection timed out.");
}catch (Exception e){
    log.error("https request error:{}",e);
}
return buffer.toString();

    }

    /**
     * 将WechatUser里的信息转换成PersonInfo的信息并返回Personinfo实体
     * @param user
     * @return
     */
    public static PersonInfo getPersonInfoFromRequest(WechatUser user) {
        PersonInfo personInfo = new PersonInfo();
        personInfo.setName(user.getNickName());
        personInfo.setGender(user.getSex()+"");
        personInfo.setProfileImg(user.getHeadimgurl());
        personInfo.setEnableStatus(1);
        return personInfo;
    }
}
