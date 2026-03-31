package com.app.pos.system.utils;

import com.app.pos.system.model.User;
import com.app.pos.system.model.enums.RoleName;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AuthUtils {

    public User getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return (User) authentication.getPrincipal();
    }

    public boolean isAdmin(User user){
        return user.getUserRoles().stream()
                .anyMatch(ur -> ur.getRole().getRoleName().equals(RoleName.ADMIN));
    }
}
