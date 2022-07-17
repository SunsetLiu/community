package com.nowcoder.community.controller;

import com.nowcoder.community.entity.User;
import com.nowcoder.community.service.LikeService;
import com.nowcoder.community.util.CommunityUtil;
import com.nowcoder.community.util.HostHolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.Map;

@Controller
public class LikeController {

    @Autowired
    private LikeService likeService;

    @Autowired
    private HostHolder hostHolder;

    /**
     * 点赞，是异步请求
     * @param entityType
     * @param entityId
     * @return
     */
    @RequestMapping(path = "/like", method = RequestMethod.POST)
    @ResponseBody
    public String like(int entityType, int entityId, int entityUserId){
        User user = hostHolder.getUser();
        //点赞
        likeService.like(user.getId(), entityType, entityId ,entityUserId);

        //获取实体点赞数
        long entityLikeCount = likeService.findEntityLikeCount(entityType, entityId);

        //获取用户点赞状态
        int userLikeStatus = likeService.findUserLikeStatus(user.getId(), entityType, entityId);

        Map<String, Object> map = new HashMap<>();
        map.put("entityLikeCount", entityLikeCount);
        map.put("userLikeStatus", userLikeStatus);
        return CommunityUtil.getJSONString(0, null, map);
    }
}
