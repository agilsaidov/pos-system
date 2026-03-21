package com.app.pos.system.controller.admin;

import com.app.pos.system.dto.request.CreateUserRequest;
import com.app.pos.system.dto.response.UserResponse;
import com.app.pos.system.service.UserManagementService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin/users")
@RequiredArgsConstructor
public class AdminUserController {

    private final UserManagementService userManagementService;

    @PostMapping
    public ResponseEntity<UserResponse> createUser(@Valid @RequestBody CreateUserRequest request){
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(userManagementService.createUser(request));
    }

    @PatchMapping("/{id}/enable")
    public ResponseEntity<Void> disableUser(@PathVariable(name = "id") Long userId,
                                            @RequestParam(required = true) Boolean enabled){
        userManagementService.disableUser(userId, enabled);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
