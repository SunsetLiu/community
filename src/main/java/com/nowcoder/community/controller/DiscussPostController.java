package com.nowcoder.community.controller;

import com.nowcoder.community.service.CommentService;
import com.nowcoder.community.service.DiscussPostService;
import com.nowcoder.community.service.LikeService;
import com.nowcoder.community.service.UserService;
import com.nowcoder.community.entity.Comment;
import com.nowcoder.community.entity.DiscussPost;
import com.nowcoder.community.entity.Page;
import com.nowcoder.community.entity.User;
import com.nowcoder.community.util.CommunityConstant;
import com.nowcoder.community.util.CommunityUtil;
import com.nowcoder.community.util.HostHolder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.*;


/**
 * 贴子详情的表现层
 */
@Controller
@RequestMapping("/discuss")
public class DiscussPostController implements CommunityConstant {

    private static final Logger logger = LoggerFactory.getLogger(DiscussPostController.class);

    @Autowired
    private DiscussPostService discussPostService;

    @Autowired
    private HostHolder hostHolder;

    @Autowired
    private UserService userService;

    @Autowired
    private CommentService commentService;

    @Autowired
    private LikeService likeService;

    /**
     * 发布贴子
     * @param title
     * @param content
     * @return
     */
    @RequestMapping(path = "/add", method = RequestMethod.POST)
    @ResponseBody
    public String addDiscussPost(String title, String content){
        User user = hostHolder.getUser();
        if(user == null){
            return CommunityUtil.getJSONString(403, "您还未登录！");
        }
        DiscussPost discussPost = new DiscussPost();
        discussPost.setUserId(user.getId());
        discussPost.setTitle(title);
        discussPost.setContent(content);
        discussPost.setCreateTime(new Date());
        discussPostService.addDiscussPost(discussPost);
        // 报错的情况,将来统一处理
        return CommunityUtil.getJSONString(0, "发布成功！");
    }

    /**
     * 查询贴子详情
     * @param id
     * @param model
     * @return
     */
    @RequestMapping(path = "/detailed/{id}", method = RequestMethod.GET)
    public String selectDiscussPostById(@PathVariable("id") int id, Model model, Page page){
        //贴子
        DiscussPost discussPost = discussPostService.selectDiscussPostById(id);
        model.addAttribute("discussPost",discussPost);
        //贴子的作者
        User user = userService.selectById(discussPost.getUserId());
        model.addAttribute("user",user);
        //20220714添加贴子的点赞start
        //添加点赞的数量
        model.addAttribute("likeCount",likeService.findEntityLikeCount(ENTITY_TYPE_POST, discussPost.getId()));
        //添加点赞的状态
        if(hostHolder.getUser()!=null){
            model.addAttribute("likeStatus",likeService.findUserLikeStatus(hostHolder.getUser().getId(), ENTITY_TYPE_POST, discussPost.getId()));
        }else {
            model.addAttribute("likeStatus",0);
        }
        //20220714添加贴子的点赞end

        //评论的分页查询
        page.setLimit(5);
        page.setPath("/discuss/detailed/" + id);
        page.setRows(discussPost.getCommentCount());
        List<Comment> comments = commentService.findCommentsByEntity(discussPost.getId(),
                ENTITY_TYPE_POST, page.getOffset(), page.getLimit());

        //评论列表的VO
        List<Map<String,Object>> commentsVO = new ArrayList<>();
        if(comments != null){
            for (Comment comment: comments) {
                //评论的VO：包括评论、用户、回复
                Map<String,Object> commentVO = new HashMap<>();
                //评论的内容
                commentVO.put("comment",comment);
                //评论的用户信息
                commentVO.put("user",userService.selectById(comment.getUserId()));
                //20220714评论的点赞数start
                //添加点赞的数量
                commentVO.put("likeCount",likeService.findEntityLikeCount(ENTITY_TYPE_COMMENT, comment.getId()));
                //添加点赞的状态
                if(hostHolder.getUser()!=null){
                    commentVO.put("likeStatus",likeService.findUserLikeStatus(hostHolder.getUser().getId(), ENTITY_TYPE_COMMENT, comment.getId()));
                }else {
                    commentVO.put("likeStatus",0);
                }

                //20220714评论的点赞数end
                //评论的回复
                    List<Comment> replys = commentService.findCommentsByEntity(comment.getId(),
                            ENTITY_TYPE_COMMENT, 0, Integer.MAX_VALUE);//回复不分页
                    //回复列表的VO
                    List<Map<String,Object>> replysVO = new ArrayList<>();
                    if(replys != null){
                        for (Comment reply: replys) {
                            //回复的VO：包括回复、用户、目标
                            Map<String,Object> replyVO = new HashMap<>();
                            //回复的内容
                            replyVO.put("reply", reply);
                            //回复的用户信息
                            replyVO.put("user",userService.selectById(reply.getUserId()));
                            //20220714回复的点赞数start
                            //添加点赞的数量
                            replyVO.put("likeCount",likeService.findEntityLikeCount(ENTITY_TYPE_COMMENT, reply.getId()));
                            //添加点赞的状态
                            if(hostHolder.getUser()!=null){
                                replyVO.put("likeStatus",likeService.findUserLikeStatus(hostHolder.getUser().getId(), ENTITY_TYPE_COMMENT, reply.getId()));
                            }else {
                                replyVO.put("likeStatus",0);
                            }
                            //20220714回复的点赞数end
                            //回复的目标
                            User target = reply.getTargetId() == 0 ? null : userService.selectById(reply.getTargetId());
                            replyVO.put("target", target);
                            //将replyVO添加进回复列表
                            replysVO.add(replyVO);
                        }
                    }
                commentVO.put("replysVO", replysVO);
                //回复的数量（因为页面需要显示回复数）
                commentVO.put("replyCount", commentService.findCountByEntity(comment.getId(),ENTITY_TYPE_COMMENT ));
                //将commentVO添加进评论列表
                commentsVO.add(commentVO);
            }
        }
        model.addAttribute("commentsVO",commentsVO);
        //评论的数据（因为页面需要显示评论数）
        model.addAttribute("commentCount",commentService.findCountByEntity(discussPost.getId(),ENTITY_TYPE_POST));
        return "/site/discuss-detail";
    }
}
