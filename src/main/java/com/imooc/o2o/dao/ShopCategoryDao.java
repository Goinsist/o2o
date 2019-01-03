/**
 * FileName: ShopCategoryDao
 * Author:   gongyu
 * Date:     2018/5/5 14:22
 * Description:
 */
package com.imooc.o2o.dao;

import com.imooc.o2o.entity.ShopCategory;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 〈一句话功能简述〉<br> 
 * 〈〉
 *
 * @author gongyu
 * @create 2018/5/5
 * @since 1.0.0
 */
public interface ShopCategoryDao {
    List<ShopCategory>  queryShopCategory(@Param("shopCategoryCondition") ShopCategory shopCategoryCondition);
}
