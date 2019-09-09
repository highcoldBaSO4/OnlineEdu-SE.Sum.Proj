package com.se231.onlineedu.service;

import com.se231.onlineedu.model.VerificationToken;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.stereotype.Service;

/**
 *
 */
@Service
public interface EmailSenderService {
    /**
     * @param email
     */
    void sendEmail(SimpleMailMessage email);

    /**
     * @param email
     * @param token
     * @return
     */
    String sendVerificationEmail(String email, VerificationToken token);

    /**
     * @param email
     */
    void sendSensitiveWordsDetectedWords(String email, String courseTitle, String sectionTitle, String username);

    /**
     * @param email
     */
    void sendNotification(String email, String courseTitle);
}
