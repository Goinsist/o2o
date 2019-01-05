package com.imooc.o2o.web.frontend;

import com.imooc.o2o.entity.PersonInfo;
import com.imooc.o2o.entity.Product;
import com.imooc.o2o.service.ProductService;
import com.imooc.o2o.util.HttpServletRequestUtil;
import com.imooc.o2o.util.MatrixToImage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping("/frontend")
public class ProductDetailController {
    @Autowired
    private ProductService productService;

    private Map<String, Object> listProductDetailPageInfo(HttpServletRequest request) {
        Map<String, Object> modelMap = new HashMap<String, Object>();
        //获取前台传递过来的productId
        long productId = HttpServletRequestUtil.getLong(request, "productId");
        Product product = null;
        //空值判断
        if (productId != -1) {
            //根据productId获取商品信息，包含商品详情图列表
            product = productService.getProductById(productId);
            //2.0新增
            PersonInfo user = (PersonInfo) request.getSession().getAttribute("user");
            if (user == null) {
                modelMap.put("needQRCode", false);

            } else {
                modelMap.put("needQRCode", true);
            }
            modelMap.put("product", product);
            modelMap.put("success", true);
        } else {
            modelMap.put("success", false);
            modelMap.put("errMsg", "empty productId");
        }
        return modelMap;
    }

    //微信获取用户信息的api前缀
    @Value("${wechat.prefix}")
    private String urlPrefix;
    //微信获取用户信息的api中间部分
    @Value("${wechat.middle}")
    private String urlMiddle;
    //微信获取用户信息的api后缀
    @Value("${wechat.suffix}")
    private String urlSuffix;
    //微信回传给的响应添加授权信息的url
    @Value("${wechat.productmap.url}")
    private String productMapUrl;


    /**
     * 功能描述:
     * 生成商品的消费凭证二维码，拱操作员扫描，证明已消费，微信扫一扫就能链接到对应的url里
     *
     * @return
     * @author gongyu
     * @date 2019/1/4 0004 17:29
     */
    @RequestMapping(value = "/generateqrcode4product", method = RequestMethod.GET)
    @ResponseBody
    private void generateQRCode4Product(HttpServletRequest request, HttpServletResponse response) {
        //获取前端传递过来的商品id
        long productId = HttpServletRequestUtil.getLong(request, "productId");
        //从session里获取当前顾客的信息
        PersonInfo user = (PersonInfo) request.getSession().getAttribute("user");
        //空值判断
        if (productId != -1 && user != null && user.getUserId() != null) {
            //获取当前时间戳，以保证二维码的时间有效性，精确到毫秒
            long timpStamp = System.currentTimeMillis();
            //将商品id，顾客id和timestamp传入content，赋值到state中，这样微信获取到这些信息后会回传到用户商品映射信息的添加方法里
            //加上aaa是为了一会在添加信息的方法里替换这些信息使用
            String content = "{aaaproductIdaaa:" + productId + ",aaacustomerIdaaa:" + user.getUserId() + ",aaacreateTimeaaa:" + timpStamp + "}";
            MatrixToImage.QRCodetoImage(urlPrefix, urlMiddle, content, urlSuffix, productMapUrl, response);
        }
    }
}