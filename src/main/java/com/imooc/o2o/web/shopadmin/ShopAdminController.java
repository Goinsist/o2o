/**
 * FileName: ShopAdminController
 * Author:   gongyu
 * Date:     2018/5/5 10:15
 * Description:
 */
package com.imooc.o2o.web.shopadmin;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * 〈一句话功能简述〉<br> 
 * 〈〉
 *
 * @author gongyu
 * @create 2018/5/5
 * @since 1.0.0
 */
@Controller
@RequestMapping(value = "/shopadmin",method = RequestMethod.GET)
public class ShopAdminController {
    @RequestMapping(value = "/shopoperation")
    public String shopOperation(){
        return "shop/shopoperation";
    }

    @RequestMapping(value = "/shoplist")
    public String shopList(){
        return "shop/shoplist";
    }

    @RequestMapping(value = "/shopmanagement")
    //转发至店铺管理页面
    public String shopManagement(){
        return "shop/shopmanagement";
    }


    @RequestMapping(value = "/productoperation")
    //转发至商品添加/编辑页面
    public String productOperation(){
        return "shop/productoperation";
    }


    @RequestMapping(value = "/productmanagement")
    //转发至管理页面
    public String productManagement(){
        return "shop/productmanagement";
    }

    @RequestMapping(value = "/productcategorymanagement")
    public  String productcategorymanagement(){
        return "shop/productcategorymanagement";
    }

    @RequestMapping(value = "/shopauthmanagement")
    public String shopAuthManagement(){
        //转发至店铺授权页面
        return "shop/shopauthmanagement";
    }

    @RequestMapping(value = "/shopauthedit")
    public String shopAuthEdit(){
        //转发至授权信息修改页面
        return "shop/shopauthedit";
    }


    @RequestMapping(value = "/operationfail",method = RequestMethod.GET)
    public String operationFail(){
        //转发至操作失败页面页面
        return "shop/operationfail";
    }


    @RequestMapping(value = "/operationsuccess",method = RequestMethod.GET)
    public String operationSuccess(){
        //转发至操作成功页面页面
        return "shop/operationSuccess";
    }
    @RequestMapping(value = "/productbuycheck",method = RequestMethod.GET)
    public String productBuyCheck(){
        //转发至店铺的消费记录页面
        return "shop/productbuycheck";
    }

}
