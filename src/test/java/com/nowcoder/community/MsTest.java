package com.nowcoder.community;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.Date;

@SpringBootTest(classes = CommunityApplication.class)
public class MsTest {



    //这个测试类有问题，暂时没有解决--20220604已解决，原因是因为@SpringBootTest(classes = CommunityApplication.class)
    //里面的启动类得CommunityApplication.class
    @Test
    void testUserSql(){

        ArrayList<Object> objects = new ArrayList<>(100);
    }

}