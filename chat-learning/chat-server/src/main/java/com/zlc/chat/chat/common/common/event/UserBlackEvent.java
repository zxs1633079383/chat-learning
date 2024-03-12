package com.zlc.chat.chat.common.common.event;

import com.zlc.chat.chat.common.user.domain.entity.User;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEvent;

/**
 * 1. 作者: 16330
 * 2. 日期: 2024/3/11--11:29
 * 3. 目的:
 */

@Getter
@Setter
@Slf4j
public class UserBlackEvent extends ApplicationEvent {


    private User user;

    public UserBlackEvent(Object source, User user) {
        super(source);
        log.error("用户online:" + user);
        this.user = user;
    }
}
