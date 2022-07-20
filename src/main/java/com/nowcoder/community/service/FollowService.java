package com.nowcoder.community.service;

import com.nowcoder.community.entity.User;
import com.nowcoder.community.util.CommunityConstant;
import com.nowcoder.community.util.RedisKeyUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.core.RedisOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.SessionCallback;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class FollowService implements CommunityConstant {

    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private UserService userService;

    /**
     * 关注
     * @param entityType
     * @param entityId
     */
    public void follow(int userId, int entityType, int entityId){
        redisTemplate.execute(new SessionCallback() {
            @Override
            public Object execute(RedisOperations operations) throws DataAccessException {
                String followeeKey = RedisKeyUtil.getFolloweeKey(userId, entityType);
                String followerKey = RedisKeyUtil.getFollowerKey(entityType, entityId);
                operations.multi();
                redisTemplate.opsForZSet().add(followeeKey, entityId, System.currentTimeMillis());
                redisTemplate.opsForZSet().add(followerKey, userId, System.currentTimeMillis());
                return operations.exec();
            }
        });
    }

    /**
     * 取消关注
     * @param entityType
     * @param entityId
     */
    public void unfollow(int userId, int entityType, int entityId){
        redisTemplate.execute(new SessionCallback() {
            @Override
            public Object execute(RedisOperations operations) throws DataAccessException {
                String followeeKey = RedisKeyUtil.getFolloweeKey(userId, entityType);
                String followerKey = RedisKeyUtil.getFollowerKey(entityType, entityId);
                operations.multi();
                redisTemplate.opsForZSet().remove(followeeKey, entityId, System.currentTimeMillis());
                redisTemplate.opsForZSet().remove(followerKey, userId, System.currentTimeMillis());
                return operations.exec();
            }
        });
    }

    /**
     * 统计某用户关注某实体的数量
     * @param userId
     * @param entityType
     * @return
     */
    public long findFolloweeCount(int userId, int entityType){
        String followeeKey = RedisKeyUtil.getFolloweeKey(userId, entityType);
        return redisTemplate.opsForZSet().zCard(followeeKey);
    }

    /**
     * 统计某实体的粉丝数量
     * @param entityType
     * @param entityId
     * @return
     */
    public long findFollowerCount(int entityType, int entityId){
        String followerKey = RedisKeyUtil.getFollowerKey(entityType, entityId);
        return redisTemplate.opsForZSet().zCard(followerKey);
    }

    /**
     * 查询当前用户是否已关注该实体
     * @param userId
     * @param entityType
     * @param entityId
     * @return
     */
    public boolean hasFollowed(int userId, int entityType, int entityId){
        String followeeKey = RedisKeyUtil.getFolloweeKey(userId, entityType);
        return redisTemplate.opsForZSet().score(followeeKey, entityId) != null;
    }

    /**
     * 某用户关注的人
     * @param userId
     * @param offset
     * @param limit
     * @return
     */
    public List<Map<String,Object>> findFollowees(int userId, int offset, int limit){
        List<Map<String,Object>> followees = new ArrayList<>();
        String followeeKey = RedisKeyUtil.getFolloweeKey(userId, ENTITY_TYPE_USER);
        Set<Integer> targetIds = redisTemplate.opsForZSet().reverseRange(followeeKey, offset, offset + limit - 1);
        if(targetIds == null){
            return null;
        }
        for (Integer targetId : targetIds){
            Map<String,Object> map = new HashMap<>();
            User user = userService.selectById(targetId);
            map.put("user", user);
            Double score = redisTemplate.opsForZSet().score(followeeKey, targetId);
            map.put("followTime", new Date(score.longValue()));
            followees.add(map);
        }
        return followees;
    }

    /**
     * 某用户的粉丝
     * @param userId
     * @param offset
     * @param limit
     * @return
     */
    public List<Map<String,Object>> findFollowers(int userId, int offset, int limit){
        List<Map<String,Object>> followers = new ArrayList<>();
        String followerKey = RedisKeyUtil.getFollowerKey(ENTITY_TYPE_USER, userId);
        Set<Integer> targetIds = redisTemplate.opsForZSet().reverseRange(followerKey, offset, offset + limit - 1);
        if(targetIds == null){
            return null;
        }

        for (Integer targetId : targetIds){
            Map<String,Object> map = new HashMap<>();
            User user = userService.selectById(targetId);
            map.put("user", user);
            Double score = redisTemplate.opsForZSet().score(followerKey, targetId);
            map.put("followTime", new Date(score.longValue()));
            followers.add(map);
        }
        return followers;
    }
}
