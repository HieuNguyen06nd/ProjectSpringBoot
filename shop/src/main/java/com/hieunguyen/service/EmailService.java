package com.hieunguyen.service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.MailException;
import org.springframework.mail.MailSendException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmailService {
    private final JavaMailSender javaMailSender;

    public void sendVerificationOtpEmail(String userEmail, String otp, String subject, String text) throws MessagingException {
        try {
            // Tạo một MimeMessage mới
            MimeMessage mimeMessage = javaMailSender.createMimeMessage();

            // Sử dụng MimeMessageHelper để cấu hình các thông tin cần thiết cho email
            MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, "utf-8");

            mimeMessageHelper.setSubject(subject);
            mimeMessageHelper.setText(text + "\nYour OTP is: " + otp);  // Thêm OTP vào nội dung email
            mimeMessageHelper.setTo(userEmail);

            // Gửi email
            javaMailSender.send(mimeMessage);
        } catch (MailException e) {
            // Xử lý lỗi khi gửi mail
            throw new MailSendException("Failed to send email...", e);
        }
    }
}
