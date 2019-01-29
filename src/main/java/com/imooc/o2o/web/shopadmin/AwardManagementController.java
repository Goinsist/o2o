package com.imooc.o2o.web.shopadmin;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.imooc.o2o.dto.AwardExecution;
import com.imooc.o2o.dto.ImageHolder;
import com.imooc.o2o.entity.Award;
import com.imooc.o2o.entity.Shop;
import com.imooc.o2o.enums.AwardStateEnum;
import com.imooc.o2o.service.AwardService;
import com.imooc.o2o.util.CodeUtil;
import com.imooc.o2o.util.HttpServletRequestUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.commons.CommonsMultipartFile;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping("/shopadmin")
public class AwardManagementController {
    @Autowired
    private AwardService awardService;
@RequestMapping(value = "/listawardsbyshop",method = RequestMethod.GET)
@ResponseBody
    private Map<String,Object> listAwardsByShop(HttpServletRequest request){
        Map<String,Object> modelMap=new HashMap<String, Object>();
        //获取分页信息
    int pageIndex=HttpServletRequestUtil.getInt(request,"pageIndex");
    int pageSize=HttpServletRequestUtil.getInt(request,"pageSize");
    //从session里获取shopId
    Shop currentShop=(Shop)request.getSession().getAttribute("currentShop");
    //空值校验
    if((pageIndex>-1)&&(pageSize>-1)&&(currentShop!=null)&&(currentShop.getShopId()!=null)){
//判断查询条件里面是否传入奖品名，有则模糊查询
        String awardName=HttpServletRequestUtil.getString(request,"awardName");
//拼接查询条件
        Award awardCondition=compactAwardCondition4Search(currentShop.getShopId(),awardName);
        //根据查询条件分页获取奖品列表即总数
        AwardExecution ae=awardService.getAwardList(awardCondition,pageIndex,pageSize);
        modelMap.put("awardList",ae.getAwardList());
        modelMap.put("count",ae.getCount());
        modelMap.put("success",true);

    }else {
        modelMap.put("succes",false);
        modelMap.put("errMsg:","pageIndex or pageSize or currentShop is null");
    }
return modelMap;
    }
    @RequestMapping(value = "/modifyaward",method = RequestMethod.POST)
    @ResponseBody
    private Map<String,Object> modifyAward(HttpServletRequest request){
    boolean statusChange=HttpServletRequestUtil.getBoolean(request,"statusChange");
Map<String,Object> modelMap=new HashMap<String, Object>();
//根据传入的状态值决定是否跳过验证码校验
        if(!statusChange&&!CodeUtil.checkVerifyCode(request)){
            modelMap.put("success",false);
            modelMap.put("errMsg","输入了错误的验证码");
            return modelMap;
        }
        //接收前端参数的变量的初始化，包括商品，缩略图
        ObjectMapper mapper=new ObjectMapper();
        Award award=null;
        ImageHolder thumbnail=null;
        CommonsMultipartResolver multipartResolver=new CommonsMultipartResolver(request.getSession().getServletContext());
        //咱们的请求中都带有multi字样，因此没法过滤，只是用来拦截外部非图片流的处理
        //里边有缩略图的空值判断，请放心使用
        try {
if(multipartResolver.isMultipart(request)){
    thumbnail=handleImage(request,thumbnail);
}
        }catch (Exception e){
            modelMap.put("success",false);
            modelMap.put("errMsg",e.toString());
            return modelMap;
        }
        try {
            String awardStr=HttpServletRequestUtil.getString(request,"awardStr");
            //尝试获取前端传过来的表单String流并将其装换成Product实体类
            award=mapper.readValue(awardStr,Award.class);
        }catch (Exception e){
            modelMap.put("success",false);
            modelMap.put("errMsg",e.toString());
            return modelMap;
        }
        //空值判断
        if(award!=null){
            try{
                //从session中获取当前店铺的id并赋值给award，减少对前端数据的依赖
                Shop currentShop=(Shop)request.getSession().getAttribute("currentShop");
                award.setShopId(currentShop.getShopId());
                AwardExecution pe=awardService.modifyAward(award,thumbnail);
                if(pe.getState()==AwardStateEnum.SUCCESS.getState()){
                    modelMap.put("success",true);
                    return  modelMap;

                }else {
                    modelMap.put("success",false);
                    modelMap.put("errMsg",pe.getStateInfo());
                }

        }catch (RuntimeException e){
modelMap.put("success",false);
modelMap.put("errMsg",e.toString());
return modelMap;
            }
    }else {
            modelMap.put("success",false);
            modelMap.put("errMsg","请输入商品信息");
        }
        return modelMap;
}
    private Award compactAwardCondition4Search(long shopId, String awardName) {
        Award awardCondition = new Award();
        awardCondition.setShopId(shopId);
        if (awardName != null) {
            awardCondition.setAwardName(awardName);
        }
        return awardCondition;
    }
    private ImageHolder handleImage(HttpServletRequest request,ImageHolder thumbnail) throws IOException {
        MultipartHttpServletRequest multipartRequest= (MultipartHttpServletRequest) request;
        //取出缩略图并构建ImageHolder对象
        CommonsMultipartFile thumbnailFile=(CommonsMultipartFile)multipartRequest.getFile("thumbnail");
        thumbnail=new ImageHolder(thumbnailFile.getOriginalFilename(),thumbnailFile.getInputStream());
        return thumbnail;
    }
    }