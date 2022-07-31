package com.nowcoder.community;

import com.nowcoder.community.util.MailClient;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

/**
 * 邮件的测试类
 */
@SpringBootTest(classes = CommunityApplication.class)
public class MailTest {

    @Autowired
    private MailClient mailClient;

    @Autowired
    private TemplateEngine templateEngine;

    @Test
    public void testTextMail(){
        mailClient.sendMail("2957450566@qq.com", "测试发邮件","hhhhhh");
    }

    @Test
    public void testHtmlMail(){
        Context context = new Context();
        context.setVariable("username", "张三");

        String process = templateEngine.process("/mail/dome", context);
        System.out.println(process);

        mailClient.sendMail("2957450566@qq.com", "测试发Html邮件",process);
    }
}
