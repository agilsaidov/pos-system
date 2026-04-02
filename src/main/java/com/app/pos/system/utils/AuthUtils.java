package com.app.pos.system.utils;

import com.app.pos.system.model.User;
import com.app.pos.system.model.enums.RoleName;
import com.app.pos.system.security.JwtUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class AuthUtils {

    private final JwtUtils jwtUtils;

    private Jwt getJwt(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if(authentication != null && authentication.getPrincipal() instanceof Jwt jwt){
            return jwt;
        }

        return null;
    }

    public List<String> getCurrentUserRoles(){
        Jwt jwt = getJwt();
        return (jwt != null) ? jwtUtils.extractRoles(jwt) : List.of();
    }

    public UUID getCurrentUserKeycloakId(){
        Jwt jwt = getJwt();
        if(jwt != null){
            String sub = jwtUtils.extractKeycloakId(jwt);
            return (sub != null) ? UUID.fromString(sub) : null;
        }
        return null;
    }

    public boolean isAdmin(){
        return getCurrentUserRoles().contains(RoleName.ADMIN.name());
    }

    public boolean isManager(){
        return getCurrentUserRoles().contains(RoleName.MANAGER.name());
    }

    public boolean isCashier(){
        return getCurrentUserRoles().contains(RoleName.CASHIER.name());
    }
}
