package com.zlc.chat.chat.common.common.event.listener;

import com.zlc.chat.chat.common.common.constant.IdempotentEnum;
import com.zlc.chat.chat.common.common.constant.ItemEnum;
import com.zlc.chat.chat.common.common.event.UserRegisterEvent;
import com.zlc.chat.chat.common.user.dao.UserDao;
import com.zlc.chat.chat.common.user.domain.entity.User;
import com.zlc.chat.chat.common.user.service.IUserBackpackService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
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
public class UserRegisterListener {

    @Autowired
    private IUserBackpackService userBackpackService;

    @Autowired
    private UserDao userDao;

    @TransactionalEventListener(classes = UserRegisterEvent.class,phase = TransactionPhase.AFTER_COMMIT)
    @Async
    public void sendCart(UserRegisterEvent event){
        User user = event.getUser();
        userBackpackService.accquireItem(user.getId(), ItemEnum.MODIFY_NAME_CARD.getId(), IdempotentEnum.UID,user.getId().toString());
    }

    @TransactionalEventListener(classes = UserRegisterEvent.class,phase = TransactionPhase.AFTER_COMMIT)
    @Async
    public void sendBadge(UserRegisterEvent event){
        User user = event.getUser();
        int userCount = userDao.count();
        if(userCount<10){
            userBackpackService.accquireItem(user.getId(), ItemEnum.REG_TOP10_BADGE.getId(), IdempotentEnum.UID,user.getId().toString());
        }else if(userCount<100){
            userBackpackService.accquireItem(user.getId(), ItemEnum.REG_TOP100_BADGE.getId(), IdempotentEnum.UID,user.getId().toString());
        }

    }

}
