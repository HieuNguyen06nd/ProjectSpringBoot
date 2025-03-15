package com.hieunguyen.service.impl;

import com.hieunguyen.service.EmailService;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class EmailServiceImpl implements EmailService {

    private final JavaMailSender mailSender;

    @Value("${app.verification.url}")
    private String verificationUrl;

    @Value("${spring.mail.username}")
    private String fromEmail;

    @Override
    public void sendVerificationCode(String to, String token) {
        try {
            log.info("Preparing verification email for: {}", to);

            String url = verificationUrl + "?token=" + token;
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);

            helper.setFrom(fromEmail);
            helper.setTo(to);
            helper.setSubject("Verify your email");
            helper.setText("<p>Click the link below to verify your email:</p><a href=\"" + url + "\">Verify Email</a>", true);

            mailSender.send(message);
            log.info("Verification email sent successfully to: {}", to);
        } catch (MessagingException e) {
            log.error("MessagingException: Failed to send email to {}: {}", to, e.getMessage());
            throw new RuntimeException("Failed to send verification email.", e);
        } catch (MailException e) {
            log.error("MailException: Failed to send email to {}: {}", to, e.getMessage());
            throw new RuntimeException("Email sending error. Please try again later.", e);
        } catch (Exception e) {
            log.error("Unexpected error occurred while sending email to {}: {}", to, e.getMessage());
            throw new RuntimeException("Unexpected error occurred while sending email.", e);
        }
    }

    @Override
    public void sendOtp(String to, String otp) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message);

            helper.setTo(to);
            helper.setSubject("Your OTP Code");
            helper.setText("Your OTP code is: " + otp + ". It will expire in 5 minutes.");

            mailSender.send(message);
        } catch (MessagingException e) {
            log.error("Failed to send OTP to {}", to, e);
        }
    }

}
