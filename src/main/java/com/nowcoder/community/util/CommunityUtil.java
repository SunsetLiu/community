package com.nowcoder.community.util;

import org.apache.commons.lang3.StringUtils;
import org.springframework.util.DigestUtils;

import java.util.UUID;

/**
 * 本系统的工具类
 */
public class CommunityUtil {

    /**
     * 生成随机字符串
     * @return
     */
    public static String generateUUID(){
        return UUID.randomUUID().toString().replaceAll("-", "");
    }

    /**
     * MD5加密（如果同一个字符串，加密出来的都是一样的，而且不能解密
     * 因此解决方法是：字段传后面再加一串随机字符串进行加密，随机字符串越长，解密难度会越大
     * @param key
     * @return
     */
    public static String md5(String key){
        if(StringUtils.isBlank(key)){
            return null;
        }
        return DigestUtils.md5DigestAsHex(key.getBytes());
    }

}
