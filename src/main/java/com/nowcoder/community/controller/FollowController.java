package com.nowcoder.community.controller;

import com.nowcoder.community.entity.Event;
import com.nowcoder.community.entity.Page;
import com.nowcoder.community.entity.User;
import com.nowcoder.community.event.ProducerEvent;
import com.nowcoder.community.service.FollowService;
import com.nowcoder.community.service.UserService;
import com.nowcoder.community.util.CommunityConstant;
import com.nowcoder.community.util.CommunityUtil;
import com.nowcoder.community.util.HostHolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;
import java.util.Map;

/**
 * 关注的表现层
 */
@Controller
public class FollowController implements CommunityConstant {

    @Autowired
    private HostHolder hostHolder;

    @Autowired
    private FollowService followService;

    @Autowired
    private UserService userService;

    @Autowired
    private ProducerEvent producerEvent;

    /**
     * 关注
     * @param entityType
     * @param entityId
     * @return
     */
    @RequestMapping(path = "/follow", method = RequestMethod.POST)
    @ResponseBody
    public String follow(int entityType, int entityId){
        User user = hostHolder.getUser();
        followService.follow(user.getId(), entityType, entityId);

        //关注后，发送系统通知
        Event event = new Event()
                .setTopic(TOPIC_FOLLOW)
                .setUserId(user.getId())
                .setEntityType(entityType)
                .setEntityId(entityId)
                .setEntityUserId(entityId);
        producerEvent.findEvent(event);
        return CommunityUtil.getJSONString(0, "关注成功！");
    }

    /**
     * 取消关注
     * @param entityType
     * @param entityId
     * @return
     */
    @RequestMapping(path = "/unfollow", method = RequestMethod.POST)
    @ResponseBody
    public String unfollow(int entityType, int entityId){
        User user = hostHolder.getUser();
        followService.unfollow(user.getId(), entityType, entityId);
        return CommunityUtil.getJSONString(0, "取关成功！");
    }

    /**
     * 某用户关注的人
     * @param page
     * @param model
     * @return
     */
    @RequestMapping(path = "/followees/{userId}", method = RequestMethod.GET)
    public String getFollowees(@PathVariable("userId") int userId, Page page, Model model){
        //1. 获取某用户
        User user = userService.selectById(userId);
        if(user == null){
            throw new RuntimeException("用户不存在！");
        }
        model.addAttribute("user", user);

        //2. 获取某用户的关注
        //设置分页
        page.setLimit(5);
        page.setPath("/followees/" + userId);
        page.setRows((int)followService.findFolloweeCount(userId, ENTITY_TYPE_USER));
        List<Map<String, Object>> followees = followService.findFollowees(userId, page.getOffset(), page.getLimit());
        if(followees != null){
            for (Map map : followees) {
                User user1 = (User)map.get("user");
                //判断当前登录用户与关注列表的用户是否关注
                boolean hasFollowed = hasFollowed(user1.getId());
                map.put("hasFollowed", hasFollowed);
            }
        }
        model.addAttribute("followees", followees);

        return "/site/followee";
    }



    /**
     * 某用户关注的人
     * @param page
     * @param model
     * @return
     */
    @RequestMapping(path = "/followers/{userId}", method = RequestMethod.GET)
    public String getFollowers(@PathVariable("userId") int userId, Page page, Model model){
        //1. 获取某用户
        User user = userService.selectById(userId);
        if(user == null){
            throw new RuntimeException("用户不存在！");
        }
        model.addAttribute("user", user);

        //2. 获取某用户的关注
        //设置分页
        page.setLimit(5);
        page.setPath("/followers/" + userId);
        page.setRows((int)followService.findFolloweeCount(userId, ENTITY_TYPE_USER));
        List<Map<String, Object>> followers = followService.findFollowers(userId, page.getOffset(), page.getLimit());
        if(followers != null){
            for (Map map : followers) {
                User user1 = (User)map.get("user");
                //判断当前登录用户与关注列表的用户是否关注
                map.put("hasFollowed", hasFollowed(user1.getId()));
            }
        }
        model.addAttribute("followers", followers);

        return "/site/follower";
    }


    /**
     * 判断是否关注
     * @param userId
     * @return
     */
    private boolean hasFollowed(int userId){
        if(hostHolder.getUser() == null){
            return false;
        }
        return followService.hasFollowed(hostHolder.getUser().getId(), ENTITY_TYPE_USER, userId);
    }
}
