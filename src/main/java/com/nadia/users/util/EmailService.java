package com.nadia.users.util;

import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.AllArgsConstructor;


@AllArgsConstructor
@Service
public class EmailService implements EmailSender {

    private final JavaMailSender mailSender;

    @Override
    public void sendEmail(String to, String body) {
        try {
            MimeMessage mimeMessage = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, "utf-8");
            helper.setText(body, true);           
            helper.setTo(to);
            helper.setSubject("Confirmez votre email");
            helper.setFrom("nalimam08@gmail.com"); 
            mailSender.send(mimeMessage);
        } catch (MessagingException e) {
            throw new IllegalStateException("Échec de l'envoi de l'email : " + e.getMessage());
        }
    }
}