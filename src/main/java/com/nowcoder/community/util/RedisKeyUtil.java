package com.nowcoder.community.util;

import com.nowcoder.community.entity.LoginTicket;

public class RedisKeyUtil {

    private static final String SPLIT = ":";
    private static final String PREFIX_ENTITY_LIKE = "like:entity";
    private static final String PREFIX_USER_LIKE = "like:user";
    private static final String PREFIX_FOLLOWEE = "followee";
    private static final String PREFIX_FOLLOWER = "follower";
    private static final String PREFIX_KAPTCHA = "kaptcha";
    private static final String PREFIX_TICKET = "ticket";
    private static final String PREFIX_USER = "user";

    /**
     * 获取某个实体的赞的key
     * @param entityType 实体的类型，1贴子，2评论
     * @param entityId 实体的ID
     * @return like:entity:entityType:entityId->set(userId)
     */
    public static String getEntityLikeKey(int entityType, int entityId){
        return PREFIX_ENTITY_LIKE + SPLIT + entityType + SPLIT + entityId;
    }

    /**
     * 获取某个用户的赞的key
     * @param userId
     * @return like:user:userId->int
     */
    public static String getUserLikeKey(int userId){
        return PREFIX_USER_LIKE + SPLIT + userId;
    }

    /**
     * 获取某用户关注的实体的key
     * @param entityType
     * @param userId
     * @return followee:userId:entityType->zset(entityId,now)
     */
    public static String getFolloweeKey( int userId, int entityType){
        return PREFIX_FOLLOWEE + SPLIT + userId + SPLIT + entityType;
    }

    /**
     * 获取某实体的关注用户的key
     * @param entityType
     * @param entityId
     * @return follower:entityType:entityId->zset(userId,now)
     */
    public static String getFollowerKey(int entityType, int entityId){
        return PREFIX_FOLLOWER + SPLIT + entityType + SPLIT + entityId;
    }

    /**
     * 获取验证码的key
     * @param owner
     * @return kaptcha:owner -> string
     */
    public static String getKaptchaKey(String owner){
        return PREFIX_KAPTCHA + SPLIT + owner;
    }

    /**
     * 获取凭证的key
     * @param ticket
     * @return
     */
    public static String getTicketKey(String ticket){
        return PREFIX_KAPTCHA + SPLIT + ticket;
    }

    /**
     * 获取用户的key
     * @param userId
     * @return
     */
    public static String getUserKey(int userId){
        return PREFIX_KAPTCHA + SPLIT + userId;
    }

}
