package com.nowcoder.community.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.text.SimpleDateFormat;

/**
 * 演示需要配置的相关练习
 */
@Configuration
public class AlphaConfig {

    /**
     * 演示时间的格式化的配置
     * @return
     */
    @Bean
    public SimpleDateFormat simpleDateFormat(){
        return new SimpleDateFormat("yyyy-mm-dd HH:mm:ss");
    }
}
