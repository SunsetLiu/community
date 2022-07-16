package com.nowcoder.community.util;

public class RedisKeyUtil {

    private static final String SPLIT = ":";
    private static final String PREFIX_ENTITY_LIKE = "like:entity";

    /**
     * 获取某个实体的赞的key
     * @param entityType 实体的类型，1贴子，2评论
     * @param entityId 实体的ID
     * @return
     */
    public static String getEntityLikeKey(int entityType, int entityId){
        return PREFIX_ENTITY_LIKE + SPLIT + entityType + SPLIT + entityId;
    }
}
