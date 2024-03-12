package com.zlc.chat.chat.common.user.domain.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

/**
 * 1. 作者: 16330
 * 2. 日期: 2024/3/11--11:37
 * 3. 目的:
 */

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class IpDetail {


    private String ip;
    private String isp_id;
    private String isp;
    private String city;
    private String city_id;
    private String country;
    private String country_id;
    private String region;
    private String region_id;
    private String area; // 新添加的字段
    private String queryIp;
    private String area_id;

    private String county;
    private String county_id;




    /**
     *         "area": "",
     *         "country": "中国",
     *         "isp_id": "100017",
     *         "queryIp": "117.85.133.4",
     *         "city": "无锡",
     *         "ip": "117.85.133.4",
     *         "isp": "电信",
     *         "county": "",
     *         "region_id": "320000",
     *         "area_id": "",
     *         "county_id": null,
     *         "region": "江苏",
     *         "country_id": "CN",
     *         "city_id": "320200"
     */
}
