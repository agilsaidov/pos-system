package com.app.pos.system.service;

import com.app.pos.system.dto.request.CreateUserRequest;
import com.app.pos.system.dto.request.UpdateUserRequest;
import com.app.pos.system.dto.response.UserResponse;
import com.app.pos.system.exception.AlreadyExistsException;
import com.app.pos.system.exception.BadRequestException;
import com.app.pos.system.exception.ForbiddenException;
import com.app.pos.system.exception.NotFoundException;
import com.app.pos.system.mapper.UserMapper;
import com.app.pos.system.model.Role;
import com.app.pos.system.model.User;
import com.app.pos.system.model.UserRole;
import com.app.pos.system.model.UserRoleId;
import com.app.pos.system.model.enums.RoleName;
import com.app.pos.system.repo.RoleRepository;
import com.app.pos.system.repo.UserRepository;
import com.app.pos.system.repo.UserRoleRepository;
import com.app.pos.system.specification.UserSpec;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserManagementService {

    private final UserRepository userRepository;
    private final UserRoleRepository userRoleRepository;
    private final RoleRepository roleRepository;
    private final KeycloakService keycloakService;
    private final UserMapper userMapper;

    public UserResponse createUser(CreateUserRequest request){

        UUID keycloakId = keycloakService.createUser(request);

        try {
            User user = new User();
            user.setKeycloakId(keycloakId);
            user.setUsername(request.getUsername());
            user.setFullName(request.getFirstName() + " " + request.getLastName());
            user.setEnabled(true);

            User savedUser = userRepository.save(user);

            Role role = roleRepository.findByRoleName(request.getRole())
                    .orElseThrow(() -> new NotFoundException("ROLE_NOT_FOUND", "Role not found"));

            UserRole userRole = new UserRole();

            userRole.setUserRoleId(new UserRoleId(savedUser.getUserId(), role.getRoleId()));
            userRole.setUser(savedUser);
            userRole.setRole(role);
            userRoleRepository.save(userRole);

            return userMapper.toUserResponse(savedUser);

        }catch (RuntimeException e){
            keycloakService.deleteUser(keycloakId.toString());
            throw new RuntimeException("Failed to create user");
        }
    }


    public Page<UserResponse> getUsers(Long userId, String search, RoleName role, Boolean enabled, int page, int size){

        if (role == RoleName.ADMIN) {
            throw new BadRequestException("INVALID_ROLE_FILTER", "Cannot filter users by ADMIN role");
        }

        Pageable pageable = PageRequest.of(page,size);

        return userRepository.findAll(UserSpec.withFilters(userId, search, role, enabled), pageable)
                .map(userMapper::toUserResponse);

    }


    public void disableUser(Long userId, Boolean enable){

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("USER_NOT_FOUND", "User with id " + userId + " not found"));

        boolean isAdmin = user.getUserRoles().stream().anyMatch(ur -> ur.getRole().getRoleName().equals(RoleName.ADMIN));

        if(isAdmin){
            throw new ForbiddenException("FORBIDDEN_ACTION", "Cannot disable an admin user");
        }

        keycloakService.enableUser(user.getKeycloakId().toString(), enable);

        user.setEnabled(enable);
        userRepository.save(user);
    }


    public UserResponse updateUser(Long userId, UpdateUserRequest request){
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("USER_NOT_FOUND", "User with id " + userId + " not found"));

        boolean isAdmin = user.getUserRoles().stream().anyMatch(ur -> ur.getRole().getRoleName().equals(RoleName.ADMIN));

        if (isAdmin){
            throw new ForbiddenException("FORBIDDEN_ACTION", "Cannot update an admin user");
        }

        if(!user.getUsername().equals(request.getUsername()) && userRepository.existsUserByUsername(request.getUsername())){
            throw new AlreadyExistsException("USERNAME_ALREADY_EXISTS", "This username is taken by another user");
        }

        keycloakService.updateUser(user.getKeycloakId().toString(), request);

        try {
            user.setUsername(request.getUsername());
            user.setFullName(request.getFirstName() + " " + request.getLastName());
            return userMapper.toUserResponse(userRepository.save(user));

        }catch (RuntimeException e){
            rollbackKeycloakUpdate(user);
            throw new RuntimeException("Failed to update user");
        }
    }

    private void rollbackKeycloakUpdate(User originalUser){
        UpdateUserRequest rollback = new UpdateUserRequest();
        rollback.setUsername(originalUser.getUsername());

        String[] nameParts = originalUser.getFullName().split(" ", 2);
        rollback.setFirstName(nameParts[0]);
        rollback.setLastName(nameParts.length > 1 ? nameParts[1] : "");
        keycloakService.updateUser(originalUser.getKeycloakId().toString(), rollback);
    }

}
