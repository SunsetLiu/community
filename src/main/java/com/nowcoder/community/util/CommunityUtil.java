package com.nowcoder.community.util;

import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang3.StringUtils;
import org.springframework.util.DigestUtils;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * 本系统的工具类
 */
public class CommunityUtil<ma, main> {

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

    /**
     * 获取JSON格式的字符串
     * @param code
     * @param msg
     * @param map
     * @return
     */
    public static String getJSONString(int code, String msg, Map<String, Object> map){
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("code", code);
        jsonObject.put("msg", msg);
        if(map != null){
            for (String key: map.keySet()) {
                jsonObject.put(key, map.get(key));
            }
        }
        return jsonObject.toJSONString();
    }

    /**
     * 获取JSON格式的字符串
     * @param code
     * @param msg
     * @return
     */
    public static String getJSONString(int code, String msg){
        return getJSONString(code, msg, null);
    }

    /**
     * 获取JSON格式的字符串
     * @param code
     * @return
     */
    public static String getJSONString(int code){
        return getJSONString(code, null, null);
    }

    /**
     * 测试JSON工具
     * @param args
     */
    public static void main(String[] args) {
        Map<String, Object> map = new HashMap<>();
        map.put("姓名", "张三");
        map.put("性别", "男");
        System.out.println(getJSONString(1, "成功", map));
    }
}
