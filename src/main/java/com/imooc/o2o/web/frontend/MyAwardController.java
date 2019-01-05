package com.imooc.o2o.web.frontend;

import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.imooc.o2o.dto.UserAwardMapExecution;
import com.imooc.o2o.entity.Award;
import com.imooc.o2o.entity.PersonInfo;
import com.imooc.o2o.entity.Shop;
import com.imooc.o2o.entity.UserAwardMap;
import com.imooc.o2o.enums.UserAwardMapStateEnum;
import com.imooc.o2o.service.AwardService;
import com.imooc.o2o.service.PersonInfoService;
import com.imooc.o2o.service.ShopService;
import com.imooc.o2o.service.UserAwardMapService;
import com.imooc.o2o.util.CodeUtil;
import com.imooc.o2o.util.HttpServletRequestUtil;
import com.imooc.o2o.util.MatrixToImage;
import com.imooc.o2o.util.ShortNetAddress;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping("/frontend")
public class MyAwardController {
    @Autowired
    private AwardService awardService;
    @Autowired
    private UserAwardMapService userAwardMapService;
    @Autowired
    private ShopService shopService;
    @Autowired
    private PersonInfoService personInfoService;
    //微信获取用户信息的api前缀
    @Value("${wechat.prefix}")
    private String urlPrefix;
    //微信获取用户信息的api中间部分
    @Value("${wechat.middle}")
    private String urlMiddle;
    //微信获取用户信息的api后缀
    @Value("${wechat.suffix}")
    private String urlSuffix;
    //微信回传给的响应添加用户奖品映射信息的url
    @Value("${wechat.exchange.url}")
    private String exchangeUrl;
/**
*
* 功能描述:
* 生成奖品的领取二维码，拱操作员扫描，证明已领取，微信扫一扫就能链接到对应的url里面
 * @return 
* @author gongyu
* @date 2019/1/5 0005 11:48
*/
@RequestMapping(value = "/generateqrcode4award",method = RequestMethod.GET)
@ResponseBody
    private void generateQRCode4Product(HttpServletRequest request, HttpServletResponse response){
//获取前端传递过来的用户奖品映射id
    long userAwardId=HttpServletRequestUtil.getLong(request,"userAwardId");
    //根据id获取顾客奖品映射实体类对象
    UserAwardMap userAwardMap=userAwardMapService.getUserAwardMapById(userAwardId);
//从session中获取顾客的信息
    PersonInfo user=(PersonInfo)request.getSession().getAttribute("user");
    //空值判断
    if(userAwardMap!=null&&user!=null&&user.getUserId()!=null&&userAwardMap.getUser().getUserId()==user.getUserId()){
//获取当前时间戳，以保证二维码的时间有效性，精确到毫秒
        long timpStamp=System.currentTimeMillis();
        //将顾客奖品映射id，顾客id和timestamp传入content，赋值到state中，这样微信获取到这些信息后会回传
String content="{aaauserAwardIdaaa:"+userAwardId+",aaacustomerIdaaa:"+user.getUserId()+
        ",aaacreateTimeaaa:"+timpStamp+"}";
        MatrixToImage.QRCodetoImage(urlPrefix,urlMiddle,content,urlSuffix,exchangeUrl,response);

    }
        
    }
@RequestMapping(value = "/getawardbyuserawardid",method = RequestMethod.GET)
@ResponseBody
private Map<String,Object> getAwardbyId(HttpServletRequest request){
    Map<String,Object> modelMap=new HashMap<String,Object>();
    //虎丘前端传递过来的userAwardId
    long userAwardId=HttpServletRequestUtil.getLong(request,"userAwardId");
    //空值判断
    if(userAwardId>-1){
        //根据id获取顾客奖品的映射信息，进而获取奖品id
        UserAwardMap userAwardMap=userAwardMapService.getUserAwardMapById(userAwardId);
    //根据奖品id获取奖品信息
        Award award=awardService.getAwardById(userAwardMap.getAward().getAwardId());
   //将奖品信息和领取状态返回给前端
        modelMap.put("award",award);
        modelMap.put("usedStatus",userAwardMap.getUsedStatus());
        modelMap.put("userAwardMap",userAwardMap);
        modelMap.put("success",true);
    }else {
        modelMap.put("success",false);
        modelMap.put("errMsg","empty awardId");
    }
    return modelMap;
}

@RequestMapping(value = "/listuserawardmapsbycustomer",method = RequestMethod.GET)
@ResponseBody
    private Map<String,Object> listUserAwardMapsByCustomer(HttpServletRequest request){
        Map<String,Object> modelMap=new HashMap<String, Object>();
        //获取分页信息
    int pageIndex=HttpServletRequestUtil.getInt(request,"pageIndex");
    int pageSize=HttpServletRequestUtil.getInt(request,"pageSize");
    //从session'中国区用户信息
    PersonInfo user=(PersonInfo)request.getSession().getAttribute("user");
//空值判断，主要确保用户id为非空
    if((pageIndex>-1)&&(pageSize>-1)&&(user!=null)&&(user.getUserId())!=null){
        UserAwardMap userAwardMapCondition=new UserAwardMap();
        userAwardMapCondition.setUser(user);
        long shopId=HttpServletRequestUtil.getLong(request,"shopId");
        if(shopId>-1){
            //若店铺id为非空，则将其添加进查询条件，即查询该用户在某个店铺的兑换信息
            Shop shop=new Shop();
            shop.setShopId(shopId);
            userAwardMapCondition.setShop(shop);

        }
        String awardName=HttpServletRequestUtil.getString(request,"awardName");
        if(awardName!=null){
            //若奖品名为非空，则将其添加进查询条件里进行模糊查询
            Award award=new Award();
            award.setAwardName(awardName);
            userAwardMapCondition.setAward(award);
        }
        //根据传入的查询条件分页获取用户键盘映射信息
        UserAwardMapExecution ue=userAwardMapService.listUserAwardMap(userAwardMapCondition,pageIndex,pageSize);
        modelMap.put("userAwardMapList",ue.getUserAwardMapList());
        modelMap.put("count",ue.getCount());
        modelMap.put("success",true);

    }else {
        modelMap.put("success",false);
        modelMap.put("errMsg","empty pageSize or pageIndex or userId");
    }
    return modelMap;
    }
@RequestMapping(value = "/adduserawardmap",method = RequestMethod.POST)
@ResponseBody
    private Map<String,Object> addUserAwardMap(HttpServletRequest request){
Map<String,Object> modelMap=new HashMap<String, Object>();
//从session中获取用户信息
    PersonInfo user=(PersonInfo)request.getSession().getAttribute("user");
//从前端请求中获取奖品id
    Long awardId=HttpServletRequestUtil.getLong(request,"awardId");
   //封装成用户奖品映射对象
    UserAwardMap userAwardMap=compactUserAwardMap4Add(user,awardId);
   //空值判断
    if(userAwardMap!=null){
        try {
            //添加兑换信息
            UserAwardMapExecution se=userAwardMapService.addUserAwardMap(userAwardMap);
            if(se.getState()==UserAwardMapStateEnum.SUCCESS.getState()){
                modelMap.put("success",true);
            }else {
                modelMap.put("success",false);
                modelMap.put("errMsg",se.getStateInfo());
            }
        }catch (RuntimeException e){
            modelMap.put("success",false);
            modelMap.put("errMsg",e.toString());
            return modelMap;
        }
    }else {
        modelMap.put("success",false);
        modelMap.put("errMsg","请选择领取的奖品");
    }
    return modelMap;
    }

    private UserAwardMap compactUserAwardMap4Add(PersonInfo user,Long awardId) {

        UserAwardMap userAwardMap = null;
        if (user != null && user.getUserId() != null && awardId != -1) {
            userAwardMap = new UserAwardMap();
            PersonInfo personInfo = personInfoService.getPersonInfoById(user
                    .getUserId());
            Award award = awardService.getAwardById(awardId);
         userAwardMap.setAward(award);
         userAwardMap.setUser(personInfo);
         Shop shop=shopService.getByShopId(award.getShopId());
         userAwardMap.setShop(shop);
         userAwardMap.setPoint(award.getPoint());

        }
        return userAwardMap;
    }
}
