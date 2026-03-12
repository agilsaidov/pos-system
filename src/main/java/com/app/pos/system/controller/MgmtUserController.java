package com.app.pos.system.controller;

import com.app.pos.system.dto.response.CashierDetailsResponse;
import com.app.pos.system.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/mgmt/users")
@RequiredArgsConstructor
public class MgmtUserController {

    private final UserService userService;

    @GetMapping("/cashiers")
    public ResponseEntity<List<CashierDetailsResponse>> getCashiers(@RequestParam(required = true) Long storeId){
        return ResponseEntity.ok().body(userService.getCashiers(storeId));
    }
}
