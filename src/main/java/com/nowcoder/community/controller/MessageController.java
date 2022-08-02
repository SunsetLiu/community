package com.nowcoder.community.controller;

import com.alibaba.fastjson.JSONObject;
import com.nowcoder.community.entity.Message;
import com.nowcoder.community.entity.Page;
import com.nowcoder.community.service.MessageService;
import com.nowcoder.community.service.UserService;
import com.nowcoder.community.util.CommunityConstant;
import com.nowcoder.community.util.HostHolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.util.HtmlUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class MessageController implements CommunityConstant {
    @Autowired
    private MessageService messageService;

    @Autowired
    private HostHolder hostHolder;

    @Autowired
    private UserService userService;

    /**
     * 查询系统通知列表
     * @param model
     * @return
     */
    @RequestMapping(path = "/notice/list", method = RequestMethod.GET)
    public String getNoticeList(Model model){
        //1. 查询评论
        Message comment = messageService.findLatestNotice(hostHolder.getUser().getId(), TOPIC_COMMENT);

        if(comment !=null){
            Map<String, Object> commentVO = new HashMap<>();
            commentVO.put("comment", comment);
            //把转义还原
            String s = HtmlUtils.htmlUnescape(comment.getContent());
            Map<String, Object> data = JSONObject.parseObject(s, HashMap.class);
            commentVO.put("user", userService.selectById((Integer)data.get("userId")));
            commentVO.put("entityId",data.get("entityId"));
            commentVO.put("entityType",data.get("entityType"));
            commentVO.put("discussPostId",data.get("discussPostId"));
            //查询评论数
            int noticeCount = messageService.findNoticeCount(hostHolder.getUser().getId(), TOPIC_COMMENT);
            commentVO.put("noticeCount",noticeCount);
            //查询未读数
            int noticeUnreadCount = messageService.findNoticeUnreadCount(hostHolder.getUser().getId(), TOPIC_COMMENT);
            commentVO.put("noticeUnreadCount",noticeUnreadCount);
            model.addAttribute("commentVO", commentVO);
        }



        //2. 查询点赞
        Message like = messageService.findLatestNotice(hostHolder.getUser().getId(), TOPIC_LIKE);

        if(like !=null){
            Map<String, Object> likeVO = new HashMap<>();
            likeVO.put("like", like);
            //把转义还原
            String s = HtmlUtils.htmlUnescape(like.getContent());
            Map<String, Object> data = JSONObject.parseObject(s, HashMap.class);
            likeVO.put("user", userService.selectById((Integer)data.get("userId")));
            likeVO.put("entityId",data.get("entityId"));
            likeVO.put("entityType",data.get("entityType"));
            likeVO.put("discussPostId",data.get("discussPostId"));
            //查询评论数
            int noticeCount = messageService.findNoticeCount(hostHolder.getUser().getId(), TOPIC_LIKE);
            likeVO.put("noticeCount",noticeCount);
            //查询未读数
            int noticeUnreadCount = messageService.findNoticeUnreadCount(hostHolder.getUser().getId(), TOPIC_LIKE);
            likeVO.put("noticeUnreadCount",noticeUnreadCount);
            model.addAttribute("likeVO", likeVO);
        }



        //3. 查询关注
        Message follow = messageService.findLatestNotice(hostHolder.getUser().getId(), TOPIC_FOLLOW);

        if(follow !=null){
            Map<String, Object> followVO = new HashMap<>();
            followVO.put("follow", follow);
            //把转义还原
            String s = HtmlUtils.htmlUnescape(follow.getContent());
            Map<String, Object> data = JSONObject.parseObject(s, HashMap.class);
            followVO.put("user", userService.selectById((Integer)data.get("userId")));
            followVO.put("entityId",data.get("entityId"));
            followVO.put("entityType",data.get("entityType"));
            //查询评论数
            int noticeCount = messageService.findNoticeCount(hostHolder.getUser().getId(), TOPIC_FOLLOW);
            followVO.put("noticeCount",noticeCount);
            //查询未读数
            int noticeUnreadCount = messageService.findNoticeUnreadCount(hostHolder.getUser().getId(), TOPIC_FOLLOW);
            followVO.put("noticeUnreadCount",noticeUnreadCount);
            model.addAttribute("followVO", followVO);
        }


        // 查询未读消息数量
        int noticeUnreadCount = messageService.findNoticeUnreadCount(hostHolder.getUser().getId(), null);
        model.addAttribute("noticeUnreadCount", noticeUnreadCount);

        return "site/notice";
    }

    /**
     * 查询系统通知列表的详情信息
     * @param model
     * @param page
     * @param topic
     * @return
     */
    @RequestMapping(path = "/notice/detail/{topic}", method = RequestMethod.GET)
    public String getNoticeDetail(Model model, Page page, @PathVariable("topic") String topic){
        //设置分页信息
        page.setRows(messageService.findNoticeCount(hostHolder.getUser().getId(), topic));
        page.setPath("/notice/detail/" + topic);
        page.setLimit(5);

        //拼接页面显示的信息
        List<Message> notices = messageService.findNotices(hostHolder.getUser().getId(), topic, page.getOffset(), page.getLimit());
        List<Map<String, Object>> noticesVO = new ArrayList<>();
        for (Message notice: notices) {
            Map<String, Object> noticeVO = new HashMap<>();
            noticeVO.put("notice", notice);
            String s = HtmlUtils.htmlUnescape(notice.getContent());
            Map<String, Object> data = JSONObject.parseObject(s, HashMap.class);
            noticeVO.put("user", userService.selectById((Integer)data.get("userId")));
            noticeVO.put("entityId",data.get("entityId"));
            noticeVO.put("entityType",data.get("entityType"));
            noticeVO.put("discussPostId",data.get("discussPostId"));
            noticesVO.add(noticeVO);
            //查询系统信息的作者
            noticeVO.put("fromUser", userService.selectById(notice.getFromId()));
        }
        model.addAttribute("noticesVO", noticesVO);

        //更新读取状态
        List<Integer> messageIds = getMessageIds(notices);
        if(!messageIds.isEmpty()){
            messageService.readMessage(messageIds);
        }

        return "site/notice-detail";
    }


    /**
     * 获取消息的主键列表
     * @param messages
     * @return
     */
    private List<Integer> getMessageIds(List<Message> messages) {
        List<Integer> ids = new ArrayList<>();
        if(messages != null){
            for(Message message : messages){
                if(message.getStatus() != 1 )
                ids.add(message.getId());
            }
        }
        return ids;
    }
}
