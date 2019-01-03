/**
 * FileName: WechatAuthServiceTest
 * Author:   gongyu
 * Date:     2018/6/15 12:50
 * Description:
 */
package com.imooc.o2o.service;


import com.imooc.o2o.dto.WechatAuthExecution;
import com.imooc.o2o.entity.PersonInfo;
import com.imooc.o2o.entity.WechatAuth;
import com.imooc.o2o.enums.WechatAuthStateEnum;
import org.junit.Test;
import org.junit.runner.RunWith;
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
 * @create 2018/6/15
 * @since 1.0.0
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class WechatAuthServiceTest  {
@Autowired
    private WechatAuthService wechatAuthService;

@Test
    public void testRegister(){
    //新增一条微信账号
    WechatAuth wechatAuth=new WechatAuth();
    PersonInfo personInfo=new PersonInfo();
    String openId="dafaadgfeasad";
    //给微信账号设置上用户信息，但不设置上用户id
    //希望创建微信账号的时候自动创建用户信息
    personInfo.setCreateTime(new Date());
    personInfo.setName("测试一下");
    personInfo.setUserType(1);
    wechatAuth.setPersonInfo(personInfo);
    wechatAuth.setOpenId(openId);
    wechatAuth.setCreateTime(new Date());
    WechatAuthExecution wae=wechatAuthService.register(wechatAuth);
    assertEquals(WechatAuthStateEnum.SUCCESS.getState(),wae.getState());
    //通过openId找到新增的wechatAuth
    wechatAuth=wechatAuthService.getWechatAuthByOpenId(openId);
    //打印用户名字看看跟预期是否相符
    System.out.println(wechatAuth.getPersonInfo().getName());

}


}