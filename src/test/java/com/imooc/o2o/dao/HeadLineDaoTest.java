/**
 * FileName: HeadLineDaoTest
 * Author:   gongyu
 * Date:     2018/6/5 9:37
 * Description:
 */
package com.imooc.o2o.dao;


import com.imooc.o2o.entity.HeadLine;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

import static org.junit.Assert.assertEquals;

/**
 * 〈一句话功能简述〉<br> 
 * 〈〉
 *
 * @author gongyu
 * @create 2018/6/5
 * @since 1.0.0
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class HeadLineDaoTest  {
    @Autowired
    private HeadLineDao headLineDao;

    @Test
    public void testQueryArea(){
        List<HeadLine> headLineList=headLineDao.queryHeadLine(new HeadLine());
        assertEquals(4,headLineList.size());
    }
}
