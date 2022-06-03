package com.nowcoder.community;

import com.nowcoder.community.Service.AlphaService;
import com.nowcoder.community.config.AlphaConfig;
import com.nowcoder.community.dao.AlphaDao;
import com.nowcoder.community.dao.AlphaDaoHibernateImpl;
import com.nowcoder.community.dao.AlphaDaoMyBaitsImpl;
import com.nowcoder.community.dao.UserMapper;
import com.nowcoder.community.entity.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;


@SpringBootTest(classes = {
        AlphaDao.class,
        AlphaDaoMyBaitsImpl.class,
        AlphaDaoHibernateImpl.class,
        AlphaService.class,
        AlphaConfig.class,
        UserMapper.class
})
@ContextConfiguration(classes = CommunityApplicationTests.class)
public class MapperTest {

    @Autowired
    private UserMapper userMapper;

    //这个测试类有问题，暂时没有解决
    @Test
    void testUserSql(){
        User user = userMapper.selectById(101);
        System.out.println(user.toString());
    }
}
