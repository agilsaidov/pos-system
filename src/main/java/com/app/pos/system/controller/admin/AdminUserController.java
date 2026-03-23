package com.app.pos.system.controller.admin;

import com.app.pos.system.dto.request.CreateUserRequest;
import com.app.pos.system.dto.request.UpdateUserRequest;
import com.app.pos.system.dto.response.UserResponse;
import com.app.pos.system.model.enums.RoleName;
import com.app.pos.system.service.UserManagementService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin/users")
@RequiredArgsConstructor
public class AdminUserController {

    private final UserManagementService userManagementService;

    @GetMapping
    public ResponseEntity<Page<UserResponse>> getUsers(@RequestParam(required = false) Long userId,
                                                       @RequestParam(required = false) String search,
                                                       @RequestParam(required = false) RoleName role,
                                                       @RequestParam(required = false) Boolean enabled,
                                                       @RequestParam(defaultValue = "0") int page,
                                                       @RequestParam(defaultValue = "10") int size){
        return ResponseEntity.ok().body(userManagementService.getUsers(userId, search, role, enabled, page, size));
    }


    @PostMapping
    public ResponseEntity<UserResponse> createUser(@Valid @RequestBody CreateUserRequest request){
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(userManagementService.createUser(request));
    }

    @PatchMapping("/{id}/enable")
    public ResponseEntity<Void> disableUser(@PathVariable(name = "id") Long userId,
                                            @RequestParam Boolean enabled){
        userManagementService.disableUser(userId, enabled);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @PutMapping
    public ResponseEntity<UserResponse> updateResponse(@RequestParam Long userId,
                                                       @RequestBody UpdateUserRequest request){
        return ResponseEntity.ok().body(userManagementService.updateUser(userId, request));
    }
}
