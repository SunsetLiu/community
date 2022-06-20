package com.nowcoder.community;

import com.nowcoder.community.Service.AlphaService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(classes = CommunityApplication.class)
public class TransactionTest {

    @Autowired
    private AlphaService alphaService;

    @Test
    public void save1(){
        alphaService.save1();
    }

    @Test
    public void save2(){
        alphaService.save2();
    }
}
