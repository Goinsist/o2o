package com.imooc.o2o.service.impl;

import com.imooc.o2o.dao.UserAwardMapDao;
import com.imooc.o2o.dao.UserShopMapDao;
import com.imooc.o2o.dto.UserAwardMapExecution;
import com.imooc.o2o.entity.UserAwardMap;
import com.imooc.o2o.entity.UserShopMap;
import com.imooc.o2o.enums.UserAwardMapStateEnum;
import com.imooc.o2o.exceptions.UserAwardMapOperationException;
import com.imooc.o2o.service.UserAwardMapService;
import com.imooc.o2o.util.PageCalculate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

@Service
public class UserAwardMapServiceImpl implements UserAwardMapService {
@Autowired
private UserAwardMapDao userAwardMapDao;
@Autowired
private UserShopMapDao userShopMapDao;
    @Override
    public UserAwardMapExecution listUserAwardMap(UserAwardMap userAwardMapCondition, Integer pageIndex, Integer pageSize) {
        //空值判断
        if(userAwardMapCondition!=null&&pageIndex!=null&&pageSize!=null){
            //页转行
            int beginIndex=PageCalculate.calculateRowIndex(pageIndex,pageSize);
            //根据查询条件分页返回用户与奖品的映射信息列表(用户领取奖品的信息列表)
            List<UserAwardMap> userAwardMapList=userAwardMapDao.queryUserAwardMapList(userAwardMapCondition,beginIndex,pageSize);
            //返回总数
            int count=userAwardMapDao.queryUserAwardMapCount(userAwardMapCondition);
            UserAwardMapExecution ue=new UserAwardMapExecution();
            ue.setUserAwardMapList(userAwardMapList);
            ue.setCount(count);
            return ue;
        }else {
            return null;
        }

    }

    @Override
    public UserAwardMap getUserAwardMapById(Long userAwardMapId) {
        return userAwardMapDao.queryUserAwardMapById(userAwardMapId);
    }

    @Override
    @Transactional
    public UserAwardMapExecution addUserAwardMap(UserAwardMap userAwardMap) {
       //空值判断，主要是确定userId和shopId不为空
        if(userAwardMap!=null&&userAwardMap.getUser()!=null&&userAwardMap.getUser().getUserId()!=null
                &&userAwardMap.getShop()!=null&&userAwardMap.getShop().getShopId()!=null){
            //设置默认值
            userAwardMap.setCreateTime(new Date());
            userAwardMap.setUsedStatus(0);
            try {
                int effectedNum=0;
                //若该奖品需要消耗积分，则将tb_user_shop_map对应的用户积分抵扣
                if(userAwardMap.getPoint()!=null&&userAwardMap.getPoint()>0){
                    //根据用户id和店铺id获取该用户在店铺积分
                    UserShopMap userShopMap=userShopMapDao.queryUserShopMap(userAwardMap.getUser().getUserId(),userAwardMap.getShop().getShopId());
               //判断该用户在店铺里是否有积分
                    if(userShopMap!=null){
                        //若有积分，必须确保店铺积分大于本次要兑换奖品需要的积分
                        if(userShopMap.getPoint()>=userAwardMap.getPoint()){
                            //积分抵扣
                            userShopMap.setPoint(userShopMap.getPoint()-userAwardMap.getPoint());
                            //更新积分信息
                            effectedNum=userShopMapDao.updateUserShopMapPoint(userShopMap);
                            if(effectedNum<=0){
                                throw new UserAwardMapOperationException("更新积分信息失败");
                            }
                        }else {
                            throw new UserAwardMapOperationException("积分不足无法领取奖品");
                        }
                    }else {
                        //在店铺没有积分，则抛出异常
                        throw  new UserAwardMapOperationException("在本店铺没有积分");
                    }
                }
                //插入礼品兑换信息
                effectedNum=userAwardMapDao.insertUserAwardMap(userAwardMap);
                if(effectedNum<=0){
                    throw  new UserAwardMapOperationException("领取奖励失败");
               }
               return new UserAwardMapExecution(UserAwardMapStateEnum.SUCCESS,userAwardMap);
            }catch (Exception e){
                throw new UserAwardMapOperationException("领取奖励失败:"+e.toString());
            }
        }else {
            return new UserAwardMapExecution(UserAwardMapStateEnum.NULL_USERAWARD_INFO);
        }

    }

      @Override
      @Transactional
    public UserAwardMapExecution modifyUserAwardMap(UserAwardMap userAwardMap)throws UserAwardMapOperationException{
        //空值判断，主要是检查传入的userAwardId以及领取状态是否为空
          if(userAwardMap==null||userAwardMap.getUserAwardId()==null||userAwardMap.getUsedStatus()==null){
              return new UserAwardMapExecution(UserAwardMapStateEnum.NULL_USERAWARD_ID);
          }else {
              try{
                  //更新可用状态
                  int effectedNum=userAwardMapDao.updateUserAwardMap(userAwardMap);
                  if(effectedNum<=0){
                      return new UserAwardMapExecution(UserAwardMapStateEnum.INNER_ERROR);

                  }else {
                      return new UserAwardMapExecution(UserAwardMapStateEnum.SUCCESS,userAwardMap);
                  }
              }catch (Exception e){
                  throw new UserAwardMapOperationException("modifyUserAwardMap error:"+e.getMessage());
              }
          }


    }


}
