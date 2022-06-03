package com.nowcoder.community.Controller;

import com.nowcoder.community.Service.UserService;
import com.nowcoder.community.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/user") //设置访问路径
public class UserController {
    @Autowired
    private UserService userService;

    @RequestMapping("/select")
    @ResponseBody
    public String selectById(){
        User user = userService.selectById(101);
        System.out.println(user.toString());
        return "hhhh";
    }
}
