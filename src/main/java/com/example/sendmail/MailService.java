package com.example.sendmail;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.File;

@Service
public class MailService {
    @Autowired
    JavaMailSender mailSender;

    @Value("${spring.mail.username}")
    private String sender;


    /**
     * 发送普通邮件
     * 简单邮件就是指邮件内容是一个普通的文本文档：
     * @param to 接收对象
     * @param subject 邮件主题
     * @param content 邮件内容
     */
    public void sendSimpleMail(String to, String subject, String content) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(sender);
        message.setTo(to);
        message.setSubject(subject);
        message.setText(content);
        mailSender.send(message);
    }


    /**
     * 发送带附件的邮件
     * 邮件的附件可以是图片，也可以是普通文件，都是支持的。
     * @param to 接收对象
     * @param subject 邮件主题
     * @param content 邮件内容
     * @param filePathList 附件文件路径
     * @throws MessagingException
     */
    public void sendFileMail(String to, String subject, String content, String[] filePathList) throws MessagingException {
        MimeMessage message = mailSender.createMimeMessage();;
        // true表示需要创建一个multipart message
        MimeMessageHelper helper = new MimeMessageHelper(message, true);
        helper.setFrom(sender);
        helper.setTo(to);
        helper.setSubject(subject);
        helper.setText(content, true);

        for (String filePath: filePathList) {
            FileSystemResource fileSystemResource = new FileSystemResource(new File(filePath));
            String fileName = fileSystemResource.getFilename();
            helper.addAttachment(fileName, fileSystemResource);
        }
        mailSender.send(message);
    }

    /**
     * 发送带图片的邮件
     * 也是基于html邮件发送，通过内嵌图片等静态资源，可以直接看到图片。
     * 这里的邮件 text 是一个 HTML 文本，里边涉及到的图片资源先用一个占位符占着，html里面图片的cid要和helper.addInline(srcId, fileSystemResource)srcId保持一致。
     * setText 之后，再通过 addInline 方法来添加图片资源。
     * @param to 接收对象
     * @param subject 邮件主题
     * @param content 邮件内容
     * @param imgPath 图片路径
     * @throws MessagingException
     */
    public void sendImageMail(String to, String subject, String content, String srcId,String imgPath) throws MessagingException {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);

        helper.setFrom(sender);
        helper.setTo(to);
        helper.setSubject(subject);
        helper.setText(content, true);
        FileSystemResource fileSystemResource = new FileSystemResource(new File(imgPath));
        helper.addInline(srcId, fileSystemResource);

        mailSender.send(message);
    }


    /**
     * 发送HTMl邮件
     * @param to
     * @param subject
     * @param content
     * @throws MessagingException
     */
    public void sendHtmlMail(String to, String subject, String content) throws MessagingException {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);
        helper.setFrom(sender);
        helper.setTo(to);
        helper.setSubject(subject);
        helper.setText(content, true);
        mailSender.send(message);
    }


}