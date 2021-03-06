/**
 * FileName: FrontendController
 * Author:   gongyu
 * Date:     2018/6/8 9:48
 * Description:
 */
package com.imooc.o2o.web.frontend;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * 〈一句话功能简述〉<br> 
 * 〈〉
 *
 * @author gongyu
 * @create 2018/6/8
 * @since 1.0.0
 */
@Controller
@RequestMapping(value = "/frontend")
public class FrontendController {
    @RequestMapping(value = "/index",method = RequestMethod.GET)
    private String index(){
        return "frontend/index";
    }

    /**
     * 商品列表页路由
     * @return
     */
    @RequestMapping(value = "/shoplist",method = RequestMethod.GET)
    private String showShopList(){
        return "frontend/shoplist";
    }

    /**
     * 店铺详情页路由
     * @return
     */
    @RequestMapping(value = "/shopdetail",method = RequestMethod.GET)
    private String showShopDetail(){
        return "frontend/shopdetail";
    }
    /**
     * 商品详情页路由
     * @return
     */
     @RequestMapping(value = "/productdetail",method = RequestMethod.GET)
    private String showProductDetail(){
        return "frontend/productdetail";
    }


    /**
     * 店铺的奖品列表页路由
     * @return
     */
    @RequestMapping(value = "/awardlist",method = RequestMethod.GET)
    private String showAwardList(){
        return "frontend/awardlist";
    }

    /**
     * 奖品兑换列表页路由
     * @return
     */
    @RequestMapping(value = "/pointrecord",method = RequestMethod.GET)
    private String showPointRecord(){
        return "frontend/pointrecord";
    }

    /**
     * 奖品详情页路由
     * @return
     */
    @RequestMapping(value = "/awarddetail",method = RequestMethod.GET)
    private String showMyAwardDetail(){
        return "frontend/awarddetail";
    }

    /**
     * 消费记录列表页路由
     * @return
     */
    @RequestMapping(value = "/myrecord",method = RequestMethod.GET)
    private String showMyRecord(){
        return "frontend/myrecord";
    }



    /**
     * 用户各店铺积分信息页路由
     * @return
     */
    @RequestMapping(value = "/mypoint",method = RequestMethod.GET)
    private String showMyPoint(){
        return "frontend/mypoint";
    }




}
