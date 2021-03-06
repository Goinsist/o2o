package com.imooc.o2o.web.shopadmin;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.imooc.o2o.dto.*;
import com.imooc.o2o.entity.*;
import com.imooc.o2o.enums.UserProductMapStateEnum;
import com.imooc.o2o.service.*;
import com.imooc.o2o.util.HttpServletRequestUtil;
import com.imooc.o2o.util.wechat.WechatUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLDecoder;
import java.text.SimpleDateFormat;
import java.util.*;

@Controller
@RequestMapping("/shopadmin")
public class UserProductManagementController {
    private static Logger log=LoggerFactory.getLogger(UserProductManagementController.class);
    @Autowired
    private UserProductMapService userProductMapService;
    @Autowired
    private ProductSellDailyService productSellDailyService;
    @Autowired
    private WechatAuthService wechatAuthService;
    @Autowired
    private ProductService productService;
    @Autowired
    private ShopAuthMapService shopAuthMapService;

    @RequestMapping(value = "/listuserproductmapsbyshop", method = RequestMethod.GET)
    @ResponseBody
    private Map<String, Object> listUserProductMapsByShop(HttpServletRequest request) {
        Map<String, Object> modelMap = new HashMap<String, Object>();
        //获取分页信息
        int pageIndex = HttpServletRequestUtil.getInt(request, "pageIndex");
        int pageSize = HttpServletRequestUtil.getInt(request, "pageSize");
        //获取当前的店铺信息
        Shop currentShop = (Shop) request.getSession().getAttribute("currentShop");
        //空值校验，主要确保shopid不为空
        if ((pageIndex > -1) && (pageSize > -1) && (currentShop != null) && (currentShop.getShopId() != null)) {
            //添加查询条件
            UserProductMap userProductMapCondition = new UserProductMap();
            userProductMapCondition.setShop(currentShop);
            String productName = HttpServletRequestUtil.getString(request, "productName");
            if (productName != null) {
                //若前端想按照商品名模糊查询，则传入productName
                Product product = new Product();

                    product.setProductName(productName);
                    userProductMapCondition.setProduct(product);
                }
                //根据传入的查询条件获取该店铺的商品销售情况
                UserProductMapExecution ue = userProductMapService.listUserProductMap(userProductMapCondition, pageIndex, pageSize);
                modelMap.put("userProductMapList", ue.getUserProductMapList());
                modelMap.put("count", ue.getCount());
                modelMap.put("success", true);
            } else {
                modelMap.put("success", false);
                modelMap.put("errMsg", "empty pagesize or pageindex or shopid");
            }
            return modelMap;
        }

    @RequestMapping(value = "/listproductselldailyinfobyshop", method = RequestMethod.GET)
    @ResponseBody
    private Map<String, Object> listProductSellDailyInfobyShop(HttpServletRequest request) {
        Map<String, Object> modelMap = new HashMap<String, Object>();
        //获取当前的店铺信息
        Shop currentShop = (Shop) request.getSession().getAttribute("currentShop");
        //空值校验,主要确保shopId不为空
        if ((currentShop != null) && (currentShop.getShopId() != null)) {
            //添加查询条件
            ProductSellDaily productSellDailyCondition = new ProductSellDaily();
            productSellDailyCondition.setShop(currentShop);
            Calendar calendar = Calendar.getInstance();
            //获取昨天的日期
            calendar.set(Calendar.MINUTE,59);
            calendar.set(Calendar.SECOND,59);
            calendar.set(Calendar.MILLISECOND,0);
            calendar.set(Calendar.HOUR_OF_DAY,23);
            calendar.add(Calendar.DATE, -1);
            Date endTime = calendar.getTime();
            //获取七天前的日期
            calendar.set(Calendar.MINUTE,0);
            calendar.set(Calendar.SECOND,0);
            calendar.set(Calendar.MILLISECOND,0);
            calendar.set(Calendar.HOUR_OF_DAY,0);
            calendar.add(Calendar.DATE, -6);
            Date beginTime = calendar.getTime();
            //根据传入的查询条件获取该店铺的商品销售情况
            List<ProductSellDaily> productSellDailyList = productSellDailyService.listProductSellDaily(productSellDailyCondition, beginTime, endTime);
            //指定日期格式
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            //商品名列表，保证唯一性
            HashSet<String> legendData = new HashSet<String>();
            //x轴数据
            TreeSet<String> xData = new TreeSet<String>();
            //定义series
            List<EchartSeries> series = new ArrayList<EchartSeries>();
            //日销量列表
            List<Integer> totalList = new ArrayList<Integer>();
            //当前商品名，默认为空
            String currentProductName = "";
            for (int i = 0; i < productSellDailyList.size(); i++) {
                ProductSellDaily productSellDaily = productSellDailyList.get(i);
                //自动去重
                legendData.add(productSellDaily.getProduct().getProductName());
                xData.add(sdf.format(productSellDaily.getCreateTime()));
                if (!currentProductName.equals(productSellDaily.getProduct().getProductName()) && !currentProductName.isEmpty()) {
                    //如果currentProductName不等于获取的商品名，或者已遍历到列表的末尾
                    //则是遍历到下一个商品的日销量信息了，将前一轮遍历的信息放series当中
                    //包括了商品名以及与商品对应的统计日期以及当日销量
                    EchartSeries es = new EchartSeries();
                    es.setName(currentProductName);
                    es.setData(totalList.subList(0, totalList.size()));
                    series.add(es);
                    //重置titalList
                    totalList = new ArrayList<Integer>();
                    //变换下currentProductId为当前的productId
                    currentProductName = productSellDaily.getProduct().getProductName();
                    //继续添加新的值
                    totalList.add(productSellDaily.getTotal());
                } else {
                    //如果还是当前的productId则继续添加新值
                    totalList.add(productSellDaily.getTotal());
                    currentProductName = productSellDaily.getProduct().getProductName();
                }
                //队列之末，需要将最后的一个商品销量也添加上
                if (i == productSellDailyList.size() - 1) {
                    EchartSeries es = new EchartSeries();
                    es.setName(currentProductName);
                    es.setData(totalList.subList(0, totalList.size()));
                    series.add(es);
                }
            }
            modelMap.put("series", series);
            modelMap.put("legendData", legendData);
            //拼接出xAxis
            List<EchartXAxis> xAxis = new ArrayList<EchartXAxis>();
            EchartXAxis exa = new EchartXAxis();
            exa.setData(xData);
            xAxis.add(exa);
            modelMap.put("xAxis", xAxis);
            modelMap.put("success", true);



        } else {
            modelMap.put("success", false);
            modelMap.put("errMsg", "empty shopId");
        }
        return modelMap;

    }
@RequestMapping(value = "/getuserproductbyid",method = RequestMethod.GET)
    @ResponseBody
    private Map<String,Object> getUserProductMapByProductId(HttpServletRequest request){
        Map<String,Object> modelMap=new HashMap<String, Object>();

        long productId= (long) request.getSession().getAttribute("productId");
            UserProductMapExecution userProductMapCondition = userProductMapService.getUserProductMapByProductId(productId);
        UserProductMap userProductMap=  userProductMapCondition.getUserProductMap();
                    String productName=   productService.getProductById(productId).getProductName();
                             int point   =userProductMap.getPoint();
                             if(userProductMapCondition.getState()==UserProductMapStateEnum.SUCCESS.getState()){
                                 modelMap.put("success",true);
                                 modelMap.put("productName",productName);
                                 modelMap.put("point",point);
                                 return modelMap;

                             }else {
                                 modelMap.put("success",false);
                                 modelMap.put("errMsg","userProduct is null");
                             }
                            return modelMap;
    }


    @RequestMapping(value = "/adduserproductmap", method = RequestMethod.GET)

    private String addUserProductMap(HttpServletRequest request, HttpServletResponse response) throws IOException {
        //获取微信授权信息

        WechatAuth auth = getOperatorInfo(request);
        if (auth != null) {
            PersonInfo operator = auth.getPersonInfo();

            //获取二维码里state携带的content信息并解码
            String qrCodeinfo = new String(URLDecoder.decode(HttpServletRequestUtil.getString(request, "state"), "UTF-8"));
            ObjectMapper mapper = new ObjectMapper();
            WechatInfo wechatInfo = null;
            try {
                //将解码后的内容用aaa去替换掉之前生成二维码的时候加入的aaa前缀，转换成WechatInfo类型
                wechatInfo = mapper.readValue(qrCodeinfo.replace("aaa", "\""), WechatInfo.class);
            } catch (Exception e) {

                return "shop/operationfail";
            }
            //校验二维码是否已经过期
            if (!checkQRCodeInfo(wechatInfo)) {
                return "shop/operationfail";
            }

            //获取添加消费记录所需要的参数并组件成userproductmap实例
            Long productId = wechatInfo.getProductId();
            Long customerId = wechatInfo.getCustomerId();
            UserProductMap userProductMap = compactUserProductMap4Add(customerId, productId, operator);
            //空值校验
            if (userProductMap != null && customerId != -1) {
                try {
                    if (!checkShopAuth(operator.getUserId(), userProductMap)) {
                        return "shop/operationfail";
                    }
                    //添加消费记录
                    UserProductMapExecution se = userProductMapService.addUserProductMap(userProductMap);


                    if (se.getState() == UserProductMapStateEnum.SUCCESS.getState()) {
                        request.getSession().setAttribute("productId", productId);

                        return "shop/operationsuccess";
                    }
                } catch (RuntimeException e) {
                    return "shop/operationfail";
                }
            }
        }
        return "shop/operationfail";
    }


    /**
     * 功能描述:
     * 根据二维码携带的createTime判断其是否超过了十分钟，超过十分钟认定过期
     *
     * @return
     * @author gongyu
     * @date 2019/1/1 0001 15:59
     */
    private boolean checkQRCodeInfo(WechatInfo wechatInfo) {
        if (wechatInfo != null && wechatInfo.getProductId() != null && wechatInfo.getCreateTime() > 0) {
            long nowTime = System.currentTimeMillis();
            if ((nowTime - wechatInfo.getCreateTime() <= 600000)) {
                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }

    }
    /**
    *
    * 功能描述:
    * 根据code获取UserAccestoken,进而通过token里的openid获取微信用户信息
     * @return
    * @author gongyu
    * @date 2019/1/5 0005 11:15
    */
private WechatAuth getOperatorInfo(HttpServletRequest request){
        String code=request.getParameter("code");
        WechatAuth auth=null;
        if(null!=code){
            UserAccessToken token;
            //防止code重复请求失效
            try{
                String openId= (String) request.getSession().getAttribute("openId");
                if(openId==null){
                    token=WechatUtil.getUserAcessToken(code);
                     openId=token.getOpenId();
                    request.getSession().setAttribute("openId",openId);
                    auth=wechatAuthService.getWechatAuthByOpenId(openId);
                }else {

                    auth=wechatAuthService.getWechatAuthByOpenId(openId);
                }

            }catch (IOException e){
                e.printStackTrace();
            }
        }
        return auth;
}

/**
*
* 功能描述:
* 根据传入的customerId，productId以及操作员信息组建用户消费记录
 * @return
* @author gongyu
* @date 2019/1/5 0005 11:22
*/
private UserProductMap compactUserProductMap4Add(Long customerId,Long productId,PersonInfo personInfo ){
    UserProductMap userProductMap=null;
    if(customerId!=null&&productId!=null){
        userProductMap=new UserProductMap();
        PersonInfo customer=new PersonInfo();
        customer.setUserId(customerId);
        //主要为了获取商品积分
        Product product=productService.getProductById(productId);
        userProductMap.setProduct(product);
        userProductMap.setShop(product.getShop());
        userProductMap.setUser(customer);
        userProductMap.setPoint(product.getPoint());
        userProductMap.setCreateTime(new Date());
      userProductMap.setOperator(personInfo);
    }
    return userProductMap;
}
/**
*
* 功能描述:
* 检验扫码人员是否有操作权限
 * @return 
* @author gongyu
* @date 2019/1/5 0005 11:33
*/
private boolean checkShopAuth(long userId,UserProductMap userProductMap){
    //获取该店铺的所有授权信息
    ShopAuthMapExecution shopAuthMapExecution=shopAuthMapService.listShopAuthMapByShopId(userProductMap.getShop().getShopId(),1,1000);
    for(ShopAuthMap shopAuthMap:shopAuthMapExecution.getShopAuthMapList()){
        //看看是否给过该人员进行授权
        if(shopAuthMap.getEmployee().getUserId()==userId){
            return true;
        }
    }
    return false;
}
}