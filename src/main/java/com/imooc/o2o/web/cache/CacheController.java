package com.imooc.o2o.web.cache;

import com.imooc.o2o.service.AreaService;
import com.imooc.o2o.service.CacheService;
import com.imooc.o2o.service.HeadLineService;
import com.imooc.o2o.service.ShopCategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class CacheController {
    @Autowired
    private CacheService  cacheService;
    @Autowired
    private AreaService areaService;
    @Autowired
    private HeadLineService headLineService;
    @Autowired
    private ShopCategoryService shopCategoryService;
/**
*
* 功能描述:
* 清除区域信息相关的所有redis缓存
 * @return 
* @author gongyu
* @date 2019/1/5 0005 14:11
*/
@RequestMapping(value = "/clearcache4area",method = RequestMethod.GET)
    private String clearCache4Area(){
        cacheService.removeFromCache(areaService.AREALISTKEY);
        return "shop/operationsuccess";
    }
    /**
    *
    * 功能描述:
    * 清除头条相关的所有redis缓存
     * @return 
    * @author gongyu
    * @date 2019/1/5 0005 14:12
    */
    @RequestMapping(value = "/clearcache4headline",method = RequestMethod.GET)
    private String clearCache4Headline(){
        cacheService.removeFromCache(headLineService.HLLISTKEY);
        return "shop/operationsuccess";
    }
/**
*
* 功能描述:
* 清除店铺类别相关的所有redis缓存
 * @return
* @author gongyu
* @date 2019/1/5 0005 14:15
*/
    @RequestMapping(value = "/clearcache4shopcategory",method = RequestMethod.GET)
    private String clearCache4ShopCategory(){
        cacheService.removeFromCache(shopCategoryService.SCLISTKEY);
        return "shop/operationsuccess";
    }
}
