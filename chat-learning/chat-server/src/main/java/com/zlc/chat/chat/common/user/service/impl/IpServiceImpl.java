package com.zlc.chat.chat.common.user.service.impl;

import cn.hutool.core.thread.NamedThreadFactory;
import cn.hutool.http.HttpUtil;
import com.fasterxml.jackson.core.type.TypeReference;
import com.zlc.chat.chat.common.common.domain.vo.resp.ApiResult;
import com.zlc.chat.chat.common.common.utils.JsonUtils;
import com.zlc.chat.chat.common.user.dao.UserDao;
import com.zlc.chat.chat.common.user.domain.entity.IpDetail;
import com.zlc.chat.chat.common.user.domain.entity.IpInfo;
import com.zlc.chat.chat.common.user.domain.entity.User;
import com.zlc.chat.chat.common.user.service.IpService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.concurrent.*;

/**
 * 1. 作者: 16330
 * 2. 日期: 2024/3/11--11:58
 * 3. 目的:
 */

@Service
@Slf4j
public class IpServiceImpl implements IpService, DisposableBean {

    private static ExecutorService executor = new ThreadPoolExecutor(1, 1, 0L,
            TimeUnit.MILLISECONDS
            , new LinkedBlockingDeque<Runnable>(500), new NamedThreadFactory("refresh-ipDetail", false));

    @Autowired
    private UserDao userDao;

    @Override
    public void refreshIpDetailAsync(Long id) {
        executor.execute(()->{
            User user = userDao.getById(id);
            IpInfo ipInfo = user.getIpInfo();
            if(Objects.isNull(ipInfo)){
                return;
            }
            String ip = ipInfo.needRefreshIp();
            if(StringUtils.isBlank(ip)){
                return;
            }
           IpDetail ipDetail =  tryGetIpDetailOrNull(ip);
            if(Objects.nonNull(ipDetail)){
                ipInfo.refreshIpDetail(ipDetail);
                User update = new User();
                update.setId(id);
                update.setIpInfo(ipInfo);
                userDao.updateById(update);
            }

        });
    }

    //尝试获取IP详情
    private static IpDetail tryGetIpDetailOrNull(String ip) {
        for (int i = 0; i < 3; i++) {
            IpDetail ipDetail = getIpDetailOrNull(ip);
            if(Objects.nonNull(ipDetail)){
                return  ipDetail;
            }
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                log.error("tryGetIpDetailOrNull",e);
            }
        }

        return  null;
    }

    private static IpDetail getIpDetailOrNull(String ip) {
      try {
          String url = "https://ip.taobao.com/outGetIpInfo?ip="+ip+"&accessKey=alibaba-Inc";
          String data = HttpUtil.get(url);
          System.out.println(data);
          ApiResult<IpDetail> ipDetailRes = JsonUtils.toObj(data, new TypeReference<ApiResult<IpDetail>>() {});
          IpDetail detail = ipDetailRes.getData();
          log.error("淘宝成功: "+detail);
          return detail;
      }catch (Exception e){
          e.printStackTrace();
      }
      return  null;
    }

    public static void main(String[] args) {
        IpDetail ipDetail = tryGetIpDetailOrNull("106.9.123.137");
        System.out.println("成功:" +ipDetail);
    }

    @Override
    public void destroy() throws Exception {
        executor.shutdown();
        if(!executor.awaitTermination(30,TimeUnit.SECONDS)){
            if(log.isErrorEnabled()){
                log.error("Timed Out while waitting for executor {} to terminate");
            }
        }
    }
}
