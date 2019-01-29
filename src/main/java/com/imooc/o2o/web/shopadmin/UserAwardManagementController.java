package com.imooc.o2o.web.shopadmin;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.imooc.o2o.dto.*;
import com.imooc.o2o.entity.*;
import com.imooc.o2o.enums.AwardStateEnum;
import com.imooc.o2o.enums.UserAwardMapStateEnum;
import com.imooc.o2o.service.AwardService;
import com.imooc.o2o.service.PersonInfoService;
import com.imooc.o2o.service.UserAwardMapService;
import com.imooc.o2o.service.WechatAuthService;
import com.imooc.o2o.util.CodeUtil;
import com.imooc.o2o.util.HttpServletRequestUtil;
import com.imooc.o2o.util.wechat.WechatUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.commons.CommonsMultipartFile;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping("/shopadmin")
public class UserAwardManagementController {
    @Autowired
    private UserAwardMapService userAwardMapService;
    @Autowired
    private AwardService awardService;
    @Autowired
    private WechatAuthService wechatAuthService;
    @Autowired
    private PersonInfoService personInfoService;
@RequestMapping(value = "/exchangeaward",method = RequestMethod.GET)
    private String exchangeAward(HttpServletRequest request, HttpServletResponse response)throws IOException{
        //获取负责扫描二维码的店员信息
    WechatAuth auth=getOperatorInfo(request);
    if(auth!=null){
//通过userId获取店员信息
        PersonInfo operator=personInfoService.getPersonInfoById(auth.getPersonInfo().getUserId());
        //设置上用户的session

        //解析微信回传过来的自定义参数state，由于之前进行了编码，这里需要解码一下
        String qrCodeinfo=new String(URLDecoder.decode(HttpServletRequestUtil.getString(request,"state"),"UTF-8"));
        ObjectMapper mapper=new ObjectMapper();
        WechatInfo wechatInfo=null;
        try {
            //将解码后的内容用aaa去替换掉之前生成二维码的时候加入的aaa前缀，转换成wechatinfo实体类
            wechatInfo=mapper.readValue(qrCodeinfo.replace("aaa","\""),WechatInfo.class);
        }catch (Exception e){
            return "shop/operationfail";
        }
        //校验二维码是否已经过期
        if(!checkQRCodeInfo(wechatInfo)){
            return "shop/operationfail";
        }
        //获取用户奖品映射的主键
        Long userAwardId=wechatInfo.getUserAwardId();
        //获取顾客id
        Long customerId=wechatInfo.getCustomerId();
        //将顾客信息，操作员信息 以及奖品信息封装成userAwardMap
        UserAwardMap userAwardMap=compactUserAwardMap4Exchange(customerId,userAwardId,operator);
        if(userAwardMap!=null){
            try{
                //检查该员工是否具有扫码权限
                if(!checkPermission(operator.getUserId(),userAwardMap)){
                    return "shop/operationfail";
                }
                //修改奖品的领取状态
                UserAwardMapExecution se=userAwardMapService.modifyUserAwardMap(userAwardMap);
                if(se.getState()==UserAwardMapStateEnum.SUCCESS.getState()){
                    request.getSession().setAttribute("userAwardId",userAwardId);
                    return "shop/awardsuccess";
                }
            }catch (RuntimeException e){
                return "shop/operationfail";
            }
        }
    }
    return "shop/operationfail";
    }

    private boolean checkPermission(Long userId,UserAwardMap userAwardMap){
          PersonInfo personInfo=personInfoService.getPersonInfoById(userId);
          if(personInfo.getUserType()==1){
              return true;
          }else {
              return false;
          }


    }
    private UserAwardMap compactUserAwardMap4Exchange(long customerId,long userAwardId, PersonInfo operator) {
        UserAwardMap userAwardMap = new UserAwardMap();
   userAwardMap.setUserAwardId(userAwardId);
               userAwardMap.setUsedStatus(1);
                 userAwardMap.setUser(personInfoService.getPersonInfoById(customerId));

        return userAwardMap;
    }
    /**
     *
     * 功能描述:
     * 根据二维码携带的createTime判断其是否超过了十分钟，超过十分钟认定过期
     * @return
     * @author gongyu
     * @date 2019/1/1 0001 15:59
     */
    private boolean checkQRCodeInfo(WechatInfo wechatInfo){
        if(wechatInfo!=null&&wechatInfo.getUserAwardId()!=null&&wechatInfo.getCustomerId()!=null&&wechatInfo.getCreateTime()>0){
            long nowTime=System.currentTimeMillis();
            if((nowTime-wechatInfo.getCreateTime()<=600000)){
                return true;
            }else {
                return false;
            }
        }else {
            return false;
        }


    }


    /**
    *
    * 功能描述:
    * 通过商品id获取奖品信息
     * @return
    * @author gongyu
    * @date 2019/1/4 0004 13:41
    */
    @RequestMapping(value = "/getawardbyid",method = RequestMethod.GET)
    @ResponseBody
    private Map<String,Object> getAwardById(HttpServletRequest request) {
        Map<String, Object> modelMap = new HashMap<String, Object>();
        //从request里边获取前端传递过来的awardId
        long awardId = HttpServletRequestUtil.getLong(request, "awardId");
        long userAwardId;
        if(request.getSession().getAttribute("userAwardId")!=null){
            userAwardId= (long) request.getSession().getAttribute("userAwardId");
        }else {
            userAwardId=HttpServletRequestUtil.getLong(request,"userAwardId");
        }

        //空值判断
        if (awardId > -1) {
            //根据传入的id获取奖品信息并返回
            Award award = awardService.getAwardById(awardId);

            modelMap.put("awardName", award.getAwardName());
            modelMap.put("createTime",award.getCreateTime());
            modelMap.put("awardDesc",award.getAwardDesc());
            modelMap.put("point",award.getPoint());
            modelMap.put("awardImg",award.getAwardImg());
            modelMap.put("needQRCode",false);
            modelMap.put("success", true);

   return modelMap;
        }
        if(userAwardId>-1){
           UserAwardMap userAwardMap= userAwardMapService.getUserAwardMapById(userAwardId);
         Award award= awardService.getAwardById(userAwardMap.getAward().getAwardId());
            modelMap.put("awardName", award.getAwardName());
            modelMap.put("createTime",award.getCreateTime());
            modelMap.put("awardDesc",award.getAwardDesc());
            modelMap.put("point",award.getPoint());
            modelMap.put("awardImg",award.getAwardImg());
           modelMap.put("userAwardId",userAwardMap.getUserAwardId());
           if(userAwardMap.getUsedStatus()==1){
               modelMap.put("needQRCode",true);
           }else {
               modelMap.put("needQRCode",false);
           }
            modelMap.put("needQRCode",true);
            modelMap.put("success",true);
            return modelMap;
        }
return modelMap;
    }
@RequestMapping(value = "/addaward",method =RequestMethod.POST)
@ResponseBody
private Map<String,Object> addAward(HttpServletRequest request){
Map<String,Object> modelMap=new HashMap<String, Object>();
//验证码校验
    if(!CodeUtil.checkVerifyCode(request)){
        modelMap.put("success",false);
        modelMap.put("errMsg","输入了错误的验证码");
        return modelMap;
    }
    //接收前端参数的遍历的初始化，包括奖品，缩略图
    ObjectMapper mapper=new ObjectMapper();
    Award award=null;
    String awardStr=HttpServletRequestUtil.getString(request,"awardStr");
    ImageHolder thumbnail=null;
    CommonsMultipartResolver multipartResolver=new CommonsMultipartResolver(request.getSession().getServletContext());
//咱们的请求中都带有multi字样，因此没法过滤，只是用来拦截外部非图片刘的处理
    //里边有缩略图的控和判断，请放心使用
    try{
        if(multipartResolver.isMultipart(request)){
            thumbnail=handleImage(request,thumbnail);
        }
    }catch (Exception e){
        modelMap.put("success",false);
        modelMap.put("errMsg",e.toString());
        return modelMap;
    }
try {
        //将前端传入的awardStr转换成奖品对象
    award=mapper.readValue(awardStr,Award.class);
}catch (Exception e){
        modelMap.put("success",false);
        modelMap.put("errMsg",e.toString());
        return modelMap;
}
//空值判断
    if(award!=null&&thumbnail!=null){
        try{
            //从session中获取当前店铺的id并赋值给award，减少对前端数据的依赖
            Shop currentShop=(Shop)request.getSession().getAttribute("currentShop");
            award.setShopId(currentShop.getShopId());
            //添加award
            AwardExecution ae=awardService.addAward(award,thumbnail);
            if(ae.getState()==AwardStateEnum.SUCCESS.getState()){
                modelMap.put("success",true);
                return modelMap;
            }else {
                modelMap.put("success",false);
                modelMap.put("errMsg",ae.getStateInfo());
            }
        }catch (RuntimeException e){
            modelMap.put("success",false);
            modelMap.put("errMsg",e.toString() );
            return modelMap;
        }
    }else {
        modelMap.put("success",false);
        modelMap.put("errMsg","请输入奖品信息");
    }
    return modelMap;
}
private ImageHolder handleImage(HttpServletRequest request,ImageHolder thumbnail) throws IOException {

    MultipartHttpServletRequest multipartRequest= (MultipartHttpServletRequest) request;
    //取出缩略图并构建ImageHolder对象
    CommonsMultipartFile thumbnailFile=(CommonsMultipartFile)multipartRequest.getFile("thumbnail");
    thumbnail=new ImageHolder(thumbnailFile.getOriginalFilename(),thumbnailFile.getInputStream());

return thumbnail;
}
@RequestMapping(value = "/listuserawardmapsbyshop",method = RequestMethod.GET)
@ResponseBody
    private Map<String,Object> listUserAwardMapsByShop(HttpServletRequest request){
        //从session里获取店铺信息
    Map<String,Object> modelMap=new HashMap<String,Object>();
    Shop currentShop=(Shop)request.getSession().getAttribute("currentShop");
    //获取分页信息
    int pageIndex=HttpServletRequestUtil.getInt(request,"pageIndex");
    int pageSize=HttpServletRequestUtil.getInt(request,"pageSize");
    //空值判断
    if((pageIndex>-1)&&(pageSize>-1)&&(currentShop!=null)&&(currentShop.getShopId()!=null)){
        UserAwardMap userAwardMap=new UserAwardMap();
        userAwardMap.setShop(currentShop);
        //从请求中获取奖品名
        String awardName=HttpServletRequestUtil.getString(request,"awardName");
        if(awardName!=null){
            //如果需要按照奖品名称搜索，则添加搜索条件
            Award award=new Award();
            award.setAwardName(awardName);
            userAwardMap.setAward(award);
        }
        //分页返货结果
        UserAwardMapExecution ue=userAwardMapService.listUserAwardMap(userAwardMap,pageIndex,pageSize);
       modelMap.put("userAwardMapList",ue.getUserAwardMapList());
       modelMap.put("count",ue.getCount());
       modelMap.put("success",true);
    }else {
        modelMap.put("success",false);
        modelMap.put("errMsg","empty pageSize or pageIndex or shopId");
    }
    return modelMap;
    }
/**
*
* 功能描述:
* 获取扫描二维码的电源信息
 * @return
* @author gongyu
* @date 2019/1/5 0005 12:46
*/
private WechatAuth getOperatorInfo(HttpServletRequest request){
        String code=request.getParameter("code");
        WechatAuth auth=null;
        if(null!=code){
            UserAccessToken token;
            try{
                token=WechatUtil.getUserAcessToken(code);
                String openId=token.getOpenId();
                request.getSession().setAttribute("openId",openId);
                auth=wechatAuthService.getWechatAuthByOpenId(openId);
            }catch (IOException e){
                e.printStackTrace();
            }
        }
        return auth;
}
}
