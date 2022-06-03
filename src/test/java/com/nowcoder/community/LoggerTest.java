package com.nowcoder.community;

import com.nowcoder.community.Service.AlphaService;
import com.nowcoder.community.config.AlphaConfig;
import com.nowcoder.community.dao.AlphaDao;
import com.nowcoder.community.dao.AlphaDaoHibernateImpl;
import com.nowcoder.community.dao.AlphaDaoMyBaitsImpl;
import com.nowcoder.community.dao.UserMapper;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;

import java.util.ArrayList;
import java.util.LinkedList;

@SpringBootTest(classes = {
        AlphaDao.class,
        AlphaDaoMyBaitsImpl.class,
        AlphaDaoHibernateImpl.class,
        AlphaService.class,
        AlphaConfig.class,
        UserMapper.class
})
@ContextConfiguration(classes = CommunityApplicationTests.class)
public class LoggerTest {

    private static final Logger logger = LoggerFactory.getLogger(LoggerTest.class);

    @Test
    void testLogger(){
        System.out.println(logger.getName());
        logger.debug("debug");
        logger.info("info");
        logger.warn("warn");
        logger.error("error");
    }

    @Test
    void testString(){
        String s = new String("Hello World");
        String t = "Hello";
        String s1 = new String("Hello World");
        String t1 = "Hello";
        System.out.println(s == s1);
        System.out.println(t == t1);
        StringBuffer stringBuffer = new StringBuffer("Hello World");
        StringBuilder stringBuilder = new StringBuilder("Hello World");
        s.concat(t);
        ArrayList<Object> objects = new ArrayList<>();
        new LinkedList<>()
    }
}
