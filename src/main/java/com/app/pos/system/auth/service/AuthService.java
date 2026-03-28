package com.app.pos.system.auth.service;

import com.app.pos.system.auth.dto.ForgetPasswordInitiateRequest;
import com.app.pos.system.email.EmailServiceImpl;
import com.app.pos.system.model.User;
import com.app.pos.system.repo.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final EmailServiceImpl emailService;
    private final OtpService otpService;

    public String forgetPasswordInitiate(ForgetPasswordInitiateRequest request){

        Optional<User> user = userRepository.findByEmail(request.getEmail());

        if(user.isEmpty()){
            return "If an account exists with this information, an OTP will be sent";
        }

        String email = user.get().getEmail();
        emailService.sendOtpWithAttachment(email, otpService.storeOtp(email));
        return "If an account exists with this information, an OTP will be sent";
    }
}
