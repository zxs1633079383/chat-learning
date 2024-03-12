package com.zlc.chat.chat.common.common.event;

import com.zlc.chat.chat.common.user.domain.entity.User;
import lombok.Getter;
import lombok.Setter;
import org.springframework.context.ApplicationEvent;

/**
 * 1. 作者: 16330
 * 2. 日期: 2024/3/11--10:22
 * 3. 目的:
 */

@Getter
@Setter
public class UserRegisterEvent extends ApplicationEvent {

    private User user;


    public UserRegisterEvent(Object source,User user)
    {
        super(source);
        this.user = user;
    }
}
