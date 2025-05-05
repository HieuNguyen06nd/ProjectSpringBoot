package com.hieunguyen.shopstorev2.service.impl;

import com.hieunguyen.shopstorev2.service.OtpService;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.Random;

@Service
@RequiredArgsConstructor
public class OtpServiceImpl implements OtpService {

    private final StringRedisTemplate redisTemplate;
    private final JavaMailSender mailSender;

    private static final Duration OTP_EXPIRATION = Duration.ofMinutes(5);

    @Override
    public void sendOtp(String email) {
        String otp = String.format("%06d", new Random().nextInt(999999));
        redisTemplate.opsForValue().set("otp:" + email, otp, OTP_EXPIRATION);

        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            helper.setTo(email);
            helper.setSubject("Mã xác thực OTP");
            helper.setText("OTP của bạn là: " + otp);
            mailSender.send(message);
        } catch (MessagingException e) {
            throw new RuntimeException("Gửi email thất bại", e);
        }
    }

    @Override
    public boolean verifyOtp(String email, String otp) {
        String key = "otp:" + email;
        String cachedOtp = redisTemplate.opsForValue().get(key);
        if (otp.equals(cachedOtp)) {
            redisTemplate.delete(key);
            return true;
        }
        return false;
    }
}
