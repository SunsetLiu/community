package com.nowcoder.community.controller;

import com.nowcoder.community.service.DiscussPostService;
import com.nowcoder.community.service.LikeService;
import com.nowcoder.community.service.UserService;
import com.nowcoder.community.entity.DiscussPost;
import com.nowcoder.community.entity.Page;
import com.nowcoder.community.entity.User;
import com.nowcoder.community.util.CommunityConstant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 网站首页的表现层
 */
@Controller
public class HomeController implements CommunityConstant {

    @Autowired
    private UserService userService;
    @Autowired
    private DiscussPostService discussPostService;

    @Autowired
    private LikeService likeService;

    /**
     * 获取首页
     * @param model
     * @param page
     * @return
     */
    @RequestMapping(path = "/index", method = RequestMethod.GET)
    public String getIndexPage(Model model , Page page){
        //方法调用前，SpringMVC会自动实例化Model和Page，并将Page注入到Model
        //所以在thymeleaf中可以直接访问Page对象中的数据
        page.setRows(discussPostService.selectDiscussPostRows(0));
        page.setPath("/index");

        List<DiscussPost> list = discussPostService.selectDiscussPosts(0,page.getCurrent() , page.getLimit());
        List<Map<String,Object>> discussPosts = new ArrayList<>();
        for(DiscussPost discussPost:list){
            Map<String,Object> map = new HashMap<>();
            map.put("post",discussPost);
            User user = userService.selectById(discussPost.getUserId());
            System.out.println(user.toString());
            map.put("user",user);

            //20220714添加点赞的数量start
            map.put("likeCount",likeService.findEntityLikeCount(ENTITY_TYPE_POST,discussPost.getId()));
            //20220714添加点赞的数量end
            discussPosts.add(map);
        }
        model.addAttribute("discussPosts",discussPosts);
        return "/index";
    }

    /**
     * 获取错误页面
     * @return
     */
    @RequestMapping(path = "/error", method = RequestMethod.GET)
    public String getErrorPage(){
        return "/error/500";
    }
}
