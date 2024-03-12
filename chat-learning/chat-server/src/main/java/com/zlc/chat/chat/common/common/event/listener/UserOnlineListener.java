package com.zlc.chat.chat.common.common.event.listener;

import com.zlc.chat.chat.common.common.constant.IdempotentEnum;
import com.zlc.chat.chat.common.common.constant.ItemEnum;
import com.zlc.chat.chat.common.common.constant.UserActiveStatusEnum;
import com.zlc.chat.chat.common.common.event.UserOnlineEvent;
import com.zlc.chat.chat.common.common.event.UserRegisterEvent;
import com.zlc.chat.chat.common.user.dao.UserDao;
import com.zlc.chat.chat.common.user.domain.entity.User;
import com.zlc.chat.chat.common.user.service.IUserBackpackService;
import com.zlc.chat.chat.common.user.service.IpService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

/**
 * 1. 作者: 16330
 * 2. 日期: 2024/3/11--10:28
 * 3. 目的:
 */

@Component
@Slf4j
public class UserOnlineListener {

    @Autowired
    private IUserBackpackService userBackpackService;

    @Autowired
    private IpService  ipService;

    @Autowired
    private UserDao userDao;

    @TransactionalEventListener(classes = UserOnlineEvent.class,phase = TransactionPhase.AFTER_COMMIT,fallbackExecution = true)
    @Async
    public void saveDB(UserOnlineEvent event){
        User user = event.getUser();
        User user_new = new User();
        user_new.setId(user.getId());
        user_new.setLastOptTime(user.getLastOptTime());
        user_new.setIpInfo(user.getIpInfo());
        user_new.setActiveStatus(UserActiveStatusEnum.ONLINE.getStatus());
        userDao.updateById(user_new);
        //用户Ip详情的解析.
        log.error("用户IP解析详情");
        ipService.refreshIpDetailAsync(user.getId());


    }



}
