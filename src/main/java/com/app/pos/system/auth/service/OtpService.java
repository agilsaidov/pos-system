package com.app.pos.system.auth.service;

import com.app.pos.system.exception.BadRequestException;
import com.app.pos.system.exception.GoneException;
import com.app.pos.system.exception.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.util.Random;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class OtpService {

    private final RedisTemplate<String, Object> redisTemplate;

    private final SecureRandom random = new SecureRandom();

    private static final String OTP_PREFIX = "otp:";
    private static final long OTP_TTL = 3;

    public String storeOtp(String email){
        String otp = String.valueOf(random.nextInt(900000) + 100000);
        String key = OTP_PREFIX + email;

        redisTemplate.opsForValue().set(key, otp, OTP_TTL, TimeUnit.MINUTES);

        return otp;
    }


    public boolean verifyOtp(String email, String otp){
        String key = OTP_PREFIX + email;
        Object storedOtp = redisTemplate.opsForValue().get(key);

        if(storedOtp == null){
            throw new GoneException("OTP_EXPIRED", "OTP has expired");
        }

        if(!storedOtp.toString().equals(otp)){
            throw new BadRequestException("INVALID_OTP", "OTP is not valid");
        }

        redisTemplate.delete(key);
        return true;
    }
}
