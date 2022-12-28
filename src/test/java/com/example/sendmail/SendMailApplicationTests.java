package com.example.sendmail;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import javax.mail.MessagingException;

@SpringBootTest
class SendMailApplicationTests {

    @Autowired
    private MailService mailService;

    @Autowired
    private TemplateEngine templateEngine;

    @Test
    void sendSimpleMail() {
        mailService.sendSimpleMail("15159292762@163.com", "test", "aa12113456");
    }

    @Test
    void sendFileMail() throws MessagingException {
        String[] fileArry = new String[2];
        fileArry[0] = "/Users/key/Documents/My/入党/学习党史心得体会.txt";
        fileArry[1] = "/Users/key/Documents/My/入党/材料要求1.jpg";
        mailService.sendFileMail("15159292762@163.com", "test", "this is file mail", fileArry);
    }

    @Test
    void sendImageMail() throws MessagingException {
        String srcId = "p01";
        String content = "<p>hello这是一封测试邮件，这封邮件包含图片<img src='cid:"+srcId +"'/>";
        mailService.sendImageMail("15159292762@163.com", "test",content ,srcId,"/Users/key/Pictures/test.jpg");
    }

    @Test
    void sendHtmlMail() throws MessagingException {
        Context context = new Context();
        context.setVariable("username", "张三");
        context.setVariable("id", "123456");

        String content = templateEngine.process("EmailTemplate", context);
        mailService.sendHtmlMail("15159292762@163.com", "test",content );
    }
}
