package com.nowcoder.community;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

@SpringBootTest(classes = CommunityApplication.class)
public class MsTest {



    //这个测试类有问题，暂时没有解决--20220604已解决，原因是因为@SpringBootTest(classes = CommunityApplication.class)
    //里面的启动类得CommunityApplication.class
    @Test
    void testUserSql(){

        ArrayList<Object> objects = new ArrayList<>(100);
        HashMap<Object, Object> map = new HashMap<>();
        map.put("hh",1);
        map.put("hh",2);
        StringBuffer stringBuffer = new StringBuffer();
        StringBuilder stringBuilder = new StringBuilder();
        String s = new String();
        Integer i = 127;
        System.out.println(map.get("hh"));
    }

}
