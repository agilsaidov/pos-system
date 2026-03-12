package com.app.pos.system.controller;

import com.app.pos.system.dto.response.CashierDetailsResponse;
import com.app.pos.system.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

    @GetMapping("/cashiers/{cashierId}")
    public ResponseEntity<CashierDetailsResponse> getCashier(
            @PathVariable Long cashierId,
            @RequestParam(required = true) Long storeId){
        return ResponseEntity.ok().body(userService.getCashier(cashierId, storeId));
    }
}
