package com.zlc.chat.chat.common.user.service;

public interface IpService {

    //异步刷新ip细节
    void refreshIpDetailAsync(Long id);

}
