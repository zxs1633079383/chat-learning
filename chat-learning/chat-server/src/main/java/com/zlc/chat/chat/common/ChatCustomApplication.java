package com.zlc.chat.chat.common;

import org.apache.ibatis.annotations.Mapper;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import javax.swing.*;

/**
 * 1. 作者: 16330
 * 2. 日期: 2024/3/8--17:39
 * 3. 目的:
 */

@SpringBootApplication(scanBasePackages = {"com.zlc.chat.chat.common"})
@MapperScan("com.zlc.chat.chat.common.user.mapper")
public class ChatCustomApplication {
    public static void main(String[] args) {
        SpringApplication.run(ChatCustomApplication.class);
    }
}
