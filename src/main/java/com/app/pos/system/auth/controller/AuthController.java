package com.app.pos.system.auth.controller;

import com.app.pos.system.auth.dto.ForgetPasswordInitiateRequest;
import com.app.pos.system.auth.dto.ForgetPasswordValidateRequest;
import com.app.pos.system.auth.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/forget-password/initiate")
    public ResponseEntity<String> forgetPasswordInitiate(@RequestBody @Valid ForgetPasswordInitiateRequest request){
        return ResponseEntity.ok().body(authService.forgetPasswordInitiate(request));
    }

    @PostMapping("/forget-password/validate")
    public ResponseEntity<Void> forgetPasswordValidate(@RequestBody @Valid ForgetPasswordValidateRequest request){
        authService.forgetPasswordValidate(request);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
