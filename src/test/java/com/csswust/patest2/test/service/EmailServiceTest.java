package com.csswust.patest2.test.service;

import com.csswust.patest2.test.base.JunitBaseServiceDaoTest;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;

/**
 * Created by 972536780 on 2018/3/25.
 */
public class EmailServiceTest extends JunitBaseServiceDaoTest {
    @Autowired
    private JavaMailSender mailSender;

    @Test
    public void sendSimpleEmail() {
        SimpleMailMessage message = new SimpleMailMessage();//消息构造器
        message.setFrom("13198070503@163.com");//发件人
        message.setTo("972536780@qq.com");//收件人
        message.setSubject("实验室通知");//主题
        message.setText("明天下午来趟实验室");//正文
        mailSender.send(message);
        System.out.println("邮件发送完毕");
    }
}
