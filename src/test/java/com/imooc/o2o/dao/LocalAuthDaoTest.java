/**
 * FileName: LocalAuthDaoTest
 * Author:   gongyu
 * Date:     2018/6/16 10:04
 * Description:
 */
package com.imooc.o2o.dao;


import com.imooc.o2o.entity.LocalAuth;
import com.imooc.o2o.entity.PersonInfo;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Date;

import static org.junit.Assert.assertEquals;

/**
 * 〈一句话功能简述〉<br> 
 * 〈〉
 *
 * @author gongyu
 * @create 2018/6/16
 * @since 1.0.0
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class LocalAuthDaoTest  {
    @Autowired
    private LocalAuthDao localAuthDao;
    private static final String username="testusername";
    private static final String password="testpassword";
@Test
    public void testAInsertLocalAuth() throws Exception{
     //新增一条平台账号信息
    LocalAuth localAuth=new LocalAuth();
    PersonInfo personInfo=new PersonInfo();
    personInfo.setUserId(1L);
    //给平台账号绑定上用户信息
    localAuth.setPersonInfo(personInfo);
    //设置上用户名和密码
    localAuth.setUsername(username);
    localAuth.setPassword(password);
    localAuth.setCreateTime(new Date());
    int effectedNum=localAuthDao.insertLocalAuth(localAuth);
    assertEquals(1,effectedNum);
    }
@Test
    public void testBQueryLocalByUserNameAndPwd()throws Exception{
    //按照账号和密码查询用户信息
    LocalAuth localAuth=localAuthDao.queryLocalByUserNameAndPwd(username,password);
    assertEquals("测试",localAuth.getPersonInfo().getName());
    }
@Test
    public void testCQueryLocalByUserId()throws Exception{
    //按照用户id查询平台账号，进而获取用户信息
    LocalAuth localAuth=localAuthDao.queryLocalByUserId(1L);
    assertEquals("测试",localAuth.getPersonInfo().getName());
    }
@Test
    public void testDUpdateLocalAuth()throws Exception{
    //依据用户id，平台账号，以及就密码修改平台账号密码
    Date nbw=new Date();
    int effectedNum=localAuthDao.updateLocalAuth(1L,username,password,password+"new",nbw);
   assertEquals(1,effectedNum);
   //查询出该平台账号的最新信息
    LocalAuth localAuth=localAuthDao.queryLocalByUserId(1L);
    //输出新密码
    System.out.println(localAuth.getPassword());
}
}
