package com.nowcoder.community;

import com.nowcoder.community.dao.LoginTicketMapper;
import com.nowcoder.community.dao.UserMapper;
import com.nowcoder.community.entity.LoginTicket;
import com.nowcoder.community.entity.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Date;

@SpringBootTest(classes = CommunityApplication.class)
public class MapperTest {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private LoginTicketMapper loginTicketMapper;

    //这个测试类有问题，暂时没有解决--20220604已解决，原因是因为@SpringBootTest(classes = CommunityApplication.class)
    //里面的启动类得CommunityApplication.class
    @Test
    void testUserSql(){
        User user = userMapper.selectById(101);
        System.out.println(user.toString());
    }

    @Test
    void testInsertLoginTicketSql(){
        LoginTicket loginTicket = new LoginTicket();
        loginTicket.setUserId(1);
        loginTicket.setTicket("abv");
        loginTicket.setStatus(0);
        loginTicket.setExpired(new Date(System.currentTimeMillis() + 1000 * 60 * 10));//10分钟
        loginTicketMapper.insertLoginTicket(loginTicket);
    }

    @Test
    void testLoginTicketSql(){
        LoginTicket loginTicket = loginTicketMapper.selectByTicket("abv");
        System.out.println(loginTicket.toString());

        loginTicketMapper.updateStatus("abv", 1);
        loginTicket = loginTicketMapper.selectByTicket("abv");
        System.out.println(loginTicket.toString());
    }
}
