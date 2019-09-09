package com.se231.onlineedu.Service;

import com.se231.onlineedu.model.VerificationToken;
import com.se231.onlineedu.service.EmailSenderService;
import com.se231.onlineedu.serviceimpl.EmailSenderServiceImpl;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertEquals;

/**
 *
 * @author yuxuanLiu
 * @date 2019/07/22
 */
@SpringBootTest
@ActiveProfiles("test")
@RunWith(SpringRunner.class)
public class EmailSenderServiceImplTest {
    @TestConfiguration
    static class EmailSenderServiceConfig{
        @Bean
        public EmailSenderService emailSenderService(){
            return new EmailSenderServiceImpl();
        }
    }

    @Autowired
    private EmailSenderService emailSenderService;

    @Rule
    public SmtpServerRule smtpServerRule = new SmtpServerRule(2525);

    @Test
    public void sendEmail() throws MessagingException {
        emailSenderService.sendVerificationEmail("no-reply@memorynotfound.com", new VerificationToken());
        MimeMessage[] receivedMessages = smtpServerRule.getMessages();
        assertEquals(1, receivedMessages.length);
        MimeMessage current = receivedMessages[0];
        assertThat(current.getSubject()).isEqualTo("验证码服务,请在20分钟内完成验证");
    }

    @Test
    public void sendNotification() throws MessagingException {
        emailSenderService.sendSensitiveWordsDetectedWords("dfghj@dfgh.com","1","1","1");
        MimeMessage[] receivedMessages = smtpServerRule.getMessages();
        assertEquals(1, receivedMessages.length);
        MimeMessage current = receivedMessages[0];
        assertThat(current.getSubject()).isEqualTo("处理违规言论");
    }

    @Test
    public void sendNote() throws MessagingException {
        emailSenderService.sendNotification("asv@adv.com","1");
        MimeMessage[] receivedMessages = smtpServerRule.getMessages();
        assertEquals(1, receivedMessages.length);
        MimeMessage current = receivedMessages[0];
        assertThat(current.getSubject()).isEqualTo("作业发布通知");
    }


}
