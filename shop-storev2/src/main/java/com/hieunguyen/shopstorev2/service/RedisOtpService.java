package com.hieunguyen.shopstorev2.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.Random;

@Service
@RequiredArgsConstructor
public class RedisOtpService {

    private final StringRedisTemplate redisTemplate;

    public String generateOtp(String email) {
        String otp = String.format("%06d", new Random().nextInt(999999));
        redisTemplate.opsForValue().set("otp:" + email, otp, Duration.ofMinutes(5));
        return otp;
    }

    public boolean verifyOtp(String email, String otp) {
        String key = "otp:" + email;
        String value = redisTemplate.opsForValue().get(key);
        if (value != null && value.equals(otp)) {
            redisTemplate.delete(key);
            return true;
        }
        return false;
    }
}