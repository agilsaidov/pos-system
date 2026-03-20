package com.app.pos.system.service;

import com.app.pos.system.dto.request.CreateUserRequest;
import com.app.pos.system.dto.response.UserResponse;
import com.app.pos.system.exception.DuplicateException;
import com.app.pos.system.mapper.UserMapper;
import com.app.pos.system.model.User;
import com.app.pos.system.repo.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserManagementService {

    private final UserRepository userRepository;
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

            return userMapper.toUserResponse(userRepository.save(user));

        }catch (RuntimeException e){
            keycloakService.deleteUser(keycloakId.toString());
            throw new RuntimeException("Failed to create user");
        }
    }

}
