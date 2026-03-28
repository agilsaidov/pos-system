package com.app.pos.system.auth.controller;

import com.app.pos.system.auth.dto.ForgetPasswordInitiateRequest;
import com.app.pos.system.auth.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PatchMapping("/forget-password/initiate")
    public ResponseEntity<String> forgetPasswordInitiate(@RequestBody @Valid ForgetPasswordInitiateRequest request){
        return ResponseEntity.ok().body(authService.forgetPasswordInitiate(request));
    }
}
