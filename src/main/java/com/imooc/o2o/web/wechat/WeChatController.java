/**
 * FileName: WeChatController
 * Author:   gongyu
 * Date:     2018/6/14 10:03
 * Description:
 */
package com.imooc.o2o.web.wechat;

import com.imooc.o2o.util.wechat.SignUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * 〈一句话功能简述〉<br> 
 * 〈〉
 *
 * @author gongyu
 * @create 2018/6/14
 * @since 1.0.0
 */
@Controller
@RequestMapping("wechat")
public class WeChatController {
    private static Logger log=LoggerFactory.getLogger(WeChatController.class);

    @RequestMapping(method = RequestMethod.GET)
    public void doGet(HttpServletRequest request, HttpServletResponse response){
        log.debug("weixin get...");
        //微信加密签名，signature结合了开发者填写的token参数和请求中的timestamp参数，nonce参数
        String signature=request.getParameter("signature");
        //时间戳
        String timestamp=request.getParameter("timestamp");
        //随机数
        String nonce=request.getParameter("nonce");
        //随机字符串
        String echostr=request.getParameter("echostr");
        //通过校验signature对请求进行校验，若校验成功则远洋返回echostr，表示接入成功，否则接入失败
        PrintWriter out=null;
        try {
            out=response.getWriter();
            if(SignUtil.checkSignature(signature,timestamp,nonce)){
                log.debug("weixin get success...");
                out.print(echostr);
            }
        }catch (IOException e){
            e.printStackTrace();
        }finally {
            if(out!=null){
                out.close();
            }
        }
    }
}
