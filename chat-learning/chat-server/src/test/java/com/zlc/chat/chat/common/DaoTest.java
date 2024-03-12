package com.zlc.chat.chat.common;

import com.zlc.chat.chat.common.common.config.ThreadPoolConfig;
import com.zlc.chat.chat.common.common.constant.IdempotentEnum;
import com.zlc.chat.chat.common.common.constant.ItemEnum;
import com.zlc.chat.chat.common.common.event.UserOnlineEvent;
import com.zlc.chat.chat.common.common.event.listener.UserOnlineListener;
import com.zlc.chat.chat.common.common.thread.MyUncaughExceptionHandle;
import com.zlc.chat.chat.common.common.utils.JwtUtils;
import com.zlc.chat.chat.common.common.utils.RedisUtils;
import com.zlc.chat.chat.common.user.dao.UserDao;
import com.zlc.chat.chat.common.user.domain.entity.User;
import com.zlc.chat.chat.common.user.service.IUserBackpackService;
import com.zlc.chat.chat.common.user.service.LoginService;
import lombok.extern.slf4j.Slf4j;
import me.chanjar.weixin.common.error.WxErrorException;
import me.chanjar.weixin.mp.api.WxMpQrcodeService;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.bean.result.WxMpQrCodeTicket;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.annotation.Primary;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import java.util.Date;
import java.util.concurrent.TimeUnit;

/**
 * 1. 作者: 16330
 * 2. 日期: 2024/3/9--9:35
 * 3. 目的:
 */

@SpringBootTest()
@RunWith(SpringRunner.class)
@Slf4j
public class DaoTest {

    @Resource
    private WxMpService wxMpService;

    @Autowired
    private JwtUtils jwtUtils;


    @Test
    public void Test() {
        try {
            WxMpQrCodeTicket wxMpQrCodeTicket = wxMpService.getQrcodeService().qrCodeCreateTmpTicket(1, 10000);
            String url = wxMpQrCodeTicket.getUrl();
            System.out.println(url);
        } catch (WxErrorException e) {
            throw new RuntimeException(e);
        }

    }

    @Autowired
    private RedissonClient redissonClient;

    //    校验Token
    @Test
    public void test() {
        RLock lock = redissonClient.getLock("123");
        lock.lock(1000, TimeUnit.SECONDS);
        System.out.println("锁");
        lock.unlock();
    }

    @Autowired
    private LoginService loginService;

    @Test
    public void test1() {
        String token_value = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJ1aWQiOjIwMDAxLCJjcmVhdGVUaW1lIjoxNzEwMDI5NzQyfQ.VHMy4cpV0LP7S1n32Vj8nmXaAn0o6HTI0wC7Y0HJtVo";
        Long validUid = loginService.getValidUid(token_value);
        System.out.println(validUid);
    }



    @Test
    public void thread() throws InterruptedException {
        String token = loginService.login(10001L);
        System.out.println(token);
    }

    @Autowired
    private IUserBackpackService iUserBackpackService;

    @Test
    public void acquire (){
        iUserBackpackService.accquireItem(20001L, ItemEnum.REG_TOP100_BADGE.getId(), IdempotentEnum.UID,"20001");
    }

    @Autowired
    private ApplicationEventPublisher applicationEventPublisher;

    @Test
    public void test2(){
        // 创建一个用户对象
        User user = new User();
        user.setId(1L);
        user.setLastOptTime(new Date());


        // 发布 UserOnlineEvent 事件
        applicationEventPublisher.publishEvent(new UserOnlineEvent(this, user));
    }





}
