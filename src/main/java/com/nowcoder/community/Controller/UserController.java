package com.nowcoder.community.Controller;

import com.nowcoder.community.Service.UserService;
import com.nowcoder.community.annotation.LoginRequired;
import com.nowcoder.community.entity.User;
import com.nowcoder.community.util.CommunityUtil;
import com.nowcoder.community.util.HostHolder;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.multipart.MultipartFile;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.Map;

@Controller
@RequestMapping("/user")
public class UserController {

    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    @Autowired
    private UserService userService;

    @Autowired
    private HostHolder hostHolder;

    @Value("${server.servlet.context-path}")
    private String contextPath;

    @Value("${community.path.domain}")
    private String domain;

    @Value("${community.path.upload}")
    private String uploadPath;

    /**
     * 获取账号设置页面
     * @return
     */
    @LoginRequired
    @RequestMapping(path = "/setting", method = RequestMethod.GET)
    public String getSettingPage(){
        return "/site/setting";
    }

    /**
     * 图片上传
     * @param headerImage
     * @param model
     * @return
     */
    @LoginRequired
    @RequestMapping(path = "/upload", method = RequestMethod.POST)
    public String uploadHeader(MultipartFile headerImage, Model model){
        if(headerImage == null){
            model.addAttribute("error","您还没有选择图片!");
            return "/site/setting";
        }
        //获取文件名
        String fileName = headerImage.getOriginalFilename();
        //获取文件后缀名
        String suffix = fileName.substring(fileName.lastIndexOf("."));
        if(StringUtils.isBlank(suffix)){
            model.addAttribute("error","文件的格式不正确!");
        }
        //随机生成的数值拼接成新的文件名
        fileName = CommunityUtil.generateUUID() + suffix;
        File file = new File(uploadPath + "/" + fileName);
        try {
            //存储文件
            headerImage.transferTo(file);
        } catch (IOException e) {
            logger.error("上传文件失败:" + e.getMessage());
            throw new RuntimeException("上传文件失败,服务器发生异常!", e);
        }
        //获取用户信息
        User user = hostHolder.getUser();
        //http://localhost:8080/community/user/header/xxx.png
        String headerUrl = domain + contextPath + "/user/header/" + fileName;
        //更新数据库用户头像的路径
        userService.updateByHeaderUrl(user.getId(), headerUrl);

        return "redirect:/index";
    }

    /**
     * 获取头像
     * @param fileName
     * @param response
     */
    @RequestMapping(path = "/header/{fileName}", method = RequestMethod.GET)
    public void getHeader(@PathVariable("fileName") String fileName, HttpServletResponse response){
        //服务器路径
        fileName = uploadPath + "/" +  fileName;
        //获取文件后缀名
        String suffix = fileName.substring(fileName.lastIndexOf("."));
        //返回响应数据，是返回网页(text/html)，字符串还是图片(image/png)等
        response.setContentType("image/" + suffix);
        try (
                FileInputStream fis = new FileInputStream(fileName);
                OutputStream os = response.getOutputStream()
        ) {
            byte[] buffer = new byte[1024];
            int b = 0;
            while ((b = fis.read(buffer)) != -1) {
                os.write(buffer, 0, b);
            }
        } catch (IOException e) {
            logger.error("读取头像失败: " + e.getMessage());
        }
    }


    /**
     * 修改密码
     * @param password
     * @param model
     * @return
     */
    @LoginRequired
    @RequestMapping(path = "/updatePassword", method = RequestMethod.POST)
    public String updatePassword(@CookieValue("ticket") String ticket, String oldPassword, String password, Model model){
        //获取用户信息
        User user = hostHolder.getUser();
        //更新数据库用户密码
        Map<String, Object> map = userService.updateByPassword(user, oldPassword, password);
        //修改密码成功
        if (map == null || map.isEmpty()){
            //退出用户，需要重新登录
            userService.logout(ticket);
            model.addAttribute("msg","修改密码成功，请重新登录!");
            model.addAttribute("target","/login");
            return "/site/operate-result";
        }else{
            //返回信息
            model.addAttribute("oldPasswordMsg", map.get("oldPasswordMsg"));
            model.addAttribute("passwordMsg", map.get("passwordMsg"));
            return "/site/setting";
        }
    }
}
