package com.nowcoder.community;

import com.nowcoder.community.dao.UserMapper;
import com.nowcoder.community.entity.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(classes = CommunityApplication.class)
public class MapperTest {

    @Autowired
    private UserMapper userMapper;

    //这个测试类有问题，暂时没有解决--20220604已解决，原因是因为@SpringBootTest(classes = CommunityApplication.class)
    //里面的启动类得CommunityApplication.class
    @Test
    void testUserSql(){
        User user = userMapper.selectById(101);
        System.out.println(user.toString());
    }
}
