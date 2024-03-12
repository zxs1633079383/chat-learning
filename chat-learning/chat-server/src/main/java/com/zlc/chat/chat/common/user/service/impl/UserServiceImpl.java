package com.zlc.chat.chat.common.user.service.impl;

import com.zlc.chat.chat.common.common.annotation.RedissonLock;
import com.zlc.chat.chat.common.common.constant.BlackTypeEnum;
import com.zlc.chat.chat.common.common.constant.ItemEnum;
import com.zlc.chat.chat.common.common.constant.ItemTypeEnum;
import com.zlc.chat.chat.common.common.event.UserBlackEvent;
import com.zlc.chat.chat.common.common.event.UserRegisterEvent;
import com.zlc.chat.chat.common.common.exception.BusinessException;
import com.zlc.chat.chat.common.common.utils.AssertUtil;
import com.zlc.chat.chat.common.user.dao.BlackDao;
import com.zlc.chat.chat.common.user.dao.ItemConfigDao;
import com.zlc.chat.chat.common.user.dao.UserBackpackDao;
import com.zlc.chat.chat.common.user.dao.UserDao;
import com.zlc.chat.chat.common.user.domain.entity.*;
import com.zlc.chat.chat.common.user.domain.vo.req.WearingBlackReq;
import com.zlc.chat.chat.common.user.domain.vo.resp.BadgeResp;
import com.zlc.chat.chat.common.user.domain.vo.resp.UserInfoResp;
import com.zlc.chat.chat.common.user.service.Cache.ItemCache;
import com.zlc.chat.chat.common.user.service.UserService;
import com.zlc.chat.chat.common.user.service.adapter.UserAdapter;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * 1. 作者: 16330
 * 2. 日期: 2024/3/9--15:33
 * 3. 目的:
 */

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserDao userDao;

    @Autowired
    private UserBackpackDao userBackpackDao;

    @Autowired
    private ItemCache itemCache;

    @Autowired
    private ItemConfigDao itemConfigDao;

    @Autowired
    private ApplicationEventPublisher applicationEventPublisher;

    @Autowired
    private BlackDao blackDao;

    @Override
    @Transactional
    public Long register(User userSave) {
        boolean save = userDao.save(userSave);
        //todo 用户注册的事件
        applicationEventPublisher.publishEvent(new UserRegisterEvent(this,userSave));
        return userSave.getId();
    }

    @Override
    public UserInfoResp getUserInfo(Long uid) {
        User user = userDao.getById(uid);
        Integer count  =  userBackpackDao.getCountByValidItemId(uid, ItemEnum.MODIFY_NAME_CARD.getId());

        return UserAdapter.buidUserInfo(user,count);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    @RedissonLock(key = "#uid")
    public void modifyName(Long uid, String req) {
        User user =  userDao.getByName(req);
        AssertUtil.isEmpty(user,"名字已经被抢占了, 请换一个");
        UserBackpack modifyNameItem = userBackpackDao.getFirstVaildItem(uid, ItemEnum.MODIFY_NAME_CARD.getId());
        AssertUtil.isNotEmpty(modifyNameItem,"改名卡不够了,请期待后续活动");
        //使用改名卡
        boolean success = userBackpackDao.useItem(modifyNameItem);
        if(success){
            //改名
            userDao.modifyName(uid,req);
        }

//        if(user!=null){
//            throw new BusinessException("名字重复 换个名字吧");
//        }
    }

    @Override
    @Cacheable(cacheNames = "item")
    public List<BadgeResp> badges(Long uid) {
        //查看所有的徽章
        List<ItemConfig> itemConfigs = itemCache.getByType(ItemTypeEnum.BADGE.getType());
        //查询用户拥有的徽章
        List<Long> collect = itemConfigs.stream().map(ItemConfig::getId).collect(Collectors.toList());
        List<UserBackpack> backpacks = userBackpackDao.getByItemIds(uid, collect);
        //查询用户佩戴的徽章
        User user = userDao.getById(uid);



        return UserAdapter.buildBadegResp(itemConfigs,backpacks,user);
    }

    @Override
    public void wearingBadge(Long uid, Long itemId) {
        UserBackpack firstVaildItem = userBackpackDao.getFirstVaildItem(uid, itemId);
        AssertUtil.isNotEmpty(firstVaildItem,"您还没有这个徽章,快去获得吧");
        //确定这个物品是徽章.
        ItemConfig itemConfig = itemConfigDao.getById(itemId);
        AssertUtil.equal(itemConfig.getType(),ItemTypeEnum.BADGE.getType(),"只有徽章才能佩戴");
        userDao.wearingBadge(uid,itemId);

    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void black(WearingBlackReq req) {
        Long uid = req.getUid();
        Black black = new Black();
        black.setType(BlackTypeEnum.UID.getType());
        black.setTarget(uid.toString());
        blackDao.save(black);

        User user = userDao.getById(uid);

        String createIp = Optional.ofNullable(user.getIpInfo()).map(IpInfo::getCreateIp).orElse(null);
        String updateIp = Optional.ofNullable(user.getIpInfo()).map(IpInfo::getUpdateIp).orElse(null);
        if(createIp!=null && createIp.equals(updateIp)){
            blackIp(createIp);
        }else if(createIp!=null){
            blackIp(createIp);
            blackIp(updateIp);
        }


        applicationEventPublisher.publishEvent(new UserBlackEvent(this,user));
    }


    private void blackIp(String ip) {
        if(StringUtils.isBlank(ip)){
            return;
        }
        Black insert = new Black();
        insert.setType(BlackTypeEnum.IP.getType());
        insert.setTarget(ip);
        blackDao.save(insert);
    }
}
