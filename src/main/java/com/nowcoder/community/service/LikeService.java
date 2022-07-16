package com.nowcoder.community.service;

import com.nowcoder.community.util.RedisKeyUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

@Service
public class LikeService {

    @Autowired
    private RedisTemplate redisTemplate;

    /**
     * 点赞
     * @param userId
     * @param entityType
     * @param entityId
     */
    public void like(int userId, int entityType, int entityId){
        String entityLikeKey = RedisKeyUtil.getEntityLikeKey(entityType, entityId);
        Boolean isMember = redisTemplate.opsForSet().isMember(entityLikeKey, userId);
        if(isMember){
            redisTemplate.opsForSet().remove(entityLikeKey, userId);
        }else{
            redisTemplate.opsForSet().add(entityLikeKey, userId);
        }
    }

    /**
     * 获取实体的赞的数量
     * @param entityType
     * @param entityId
     * @return
     */
    public long findEntityLikeCount(int entityType, int entityId){
        String entityLikeKey = RedisKeyUtil.getEntityLikeKey(entityType, entityId);
        return redisTemplate.opsForSet().size(entityLikeKey);
    }

    /**
     * 获取用户对某实体的点赞状态
     * @param userId
     * @param entityType
     * @param entityId
     * @return
     */
    public int findUserLikeStatus(int userId, int entityType, int entityId){
        String entityLikeKey = RedisKeyUtil.getEntityLikeKey(entityType, entityId);
        Boolean isMember = redisTemplate.opsForSet().isMember(entityLikeKey, userId);
        return isMember ? 1 : 0;
    }
}
