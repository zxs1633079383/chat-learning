package com.zlc.chat.chat.common.user.domain.entity;

import lombok.Data;
import org.apache.commons.lang3.StringUtils;

import java.io.Serializable;
import java.util.Objects;
import java.util.Optional;

/**
 * 1. 作者: 16330
 * 2. 日期: 2024/3/11--11:35
 * 3. 目的:
 */


@Data
public class IpInfo implements Serializable {
    //注册IP
    private String createIp;
    //具体的详情
    private IpDetail createIpDetail;
    //最新登录的IP
    private String updateIp;
    //注册时的ip详情
    private IpDetail updateIpDetail;

    public void refreshIp(String ip) {
        if(StringUtils.isBlank(ip)){
            return;
        }
        if(StringUtils.isBlank(createIp)){
            createIp = ip;
        }
        updateIp = ip;;
    }

    public String needRefreshIp() {
        boolean notNeedRefresh = Optional.ofNullable(updateIpDetail)
                .map(IpDetail::getIp)
                .filter(ip -> Objects.equals(ip, updateIp))
                .isPresent();

        return notNeedRefresh ? null : updateIp;
    }

    public void refreshIpDetail(IpDetail ipDetail) {
        if(Objects.equals(createIp,ipDetail.getIp())){
            createIpDetail = ipDetail;
        }
        if(Objects.equals(updateIp,ipDetail.getIp())){
            updateIpDetail = ipDetail;
        }
    }
}
