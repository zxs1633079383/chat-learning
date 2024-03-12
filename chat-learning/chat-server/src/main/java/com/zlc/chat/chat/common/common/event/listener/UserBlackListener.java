package com.zlc.chat.chat.common.common.event.listener;

import com.zlc.chat.chat.common.common.constant.UserActiveStatusEnum;
import com.zlc.chat.chat.common.common.event.UserBlackEvent;
import com.zlc.chat.chat.common.common.event.UserOnlineEvent;
import com.zlc.chat.chat.common.user.dao.UserDao;
import com.zlc.chat.chat.common.user.domain.entity.User;
import com.zlc.chat.chat.common.user.service.Cache.UserCache;
import com.zlc.chat.chat.common.user.service.IUserBackpackService;
import com.zlc.chat.chat.common.user.service.IpService;
import com.zlc.chat.chat.common.websocket.domain.vo.resp.WSBaseResp;
import com.zlc.chat.chat.common.websocket.service.WebSocketService;
import com.zlc.chat.chat.common.websocket.service.adapter.WebSocketAdapter;
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
public class UserBlackListener {

    @Autowired
    private IUserBackpackService userBackpackService;

    @Autowired
    private IpService  ipService;

    @Autowired
    private UserDao userDao;

    @Autowired
    private UserCache userCache;

    @Autowired
    private WebSocketService webSocketService;

    @TransactionalEventListener(classes = UserBlackEvent.class,phase = TransactionPhase.AFTER_COMMIT,fallbackExecution = true)
    @Async
    public void saveMsg(UserBlackEvent event){
        log.error("用户拉黑事件触发");
        User user = event.getUser();
        webSocketService.sendMsgToAll(WebSocketAdapter.buildBlack(user));




    }

    @TransactionalEventListener(classes = UserBlackEvent.class,phase = TransactionPhase.AFTER_COMMIT,fallbackExecution = true)
    @Async
    public void changeUserState(UserBlackEvent event){
        log.error("用户拉黑事件触发: 更改用户状态");
        userDao.invalidUid(event.getUser().getId());


    }

    @TransactionalEventListener(classes = UserBlackEvent.class,phase = TransactionPhase.AFTER_COMMIT,fallbackExecution = true)
    @Async
    public void evictCache(UserBlackEvent event){
        log.error("用户拉黑事件触发: 清除缓存");
        userCache.evictBlackMap();


    }

}
