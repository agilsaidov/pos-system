package com.app.pos.system.service;

import com.app.pos.system.dto.request.CreateUserRequest;
import com.app.pos.system.dto.request.UpdateUserRequest;
import com.app.pos.system.exception.DuplicateException;
import com.app.pos.system.model.enums.RoleName;
import jakarta.ws.rs.core.Response;
import lombok.RequiredArgsConstructor;
import org.keycloak.admin.client.resource.RealmResource;
import org.keycloak.admin.client.resource.UserResource;
import org.keycloak.admin.client.resource.UsersResource;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.keycloak.representations.idm.RoleRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class KeycloakService {

    private final UsersResource usersResource;
    private final RealmResource realmResource;

    public UUID createUser(CreateUserRequest request){

        UserRepresentation user = new UserRepresentation();
        user.setUsername(request.getUsername());
        user.setEmail(request.getEmail());
        user.setFirstName(request.getFirstName());
        user.setLastName(request.getLastName());
        user.setEnabled(request.getEnabled());
        user.setCredentials(List.of(createCredential(request.getPassword())));

        Response response = usersResource.create(user);

        if (response.getStatus() == 409) {
            throw new DuplicateException("USER_ALREADY_EXISTS",
                    "User with this username already exists");
        }

        if(response.getStatus() != 201){
            throw new RuntimeException("Failed to create user in Keycloak: " + response.getStatus());
        }

        String locationPath = response.getLocation().getPath();
        String keycloakId = locationPath.substring(locationPath.lastIndexOf('/') + 1);

        assignRole(keycloakId, request.getRole());

        return UUID.fromString(keycloakId);
    }


    public void deleteUser(String keycloakId){
        usersResource.get(keycloakId).remove();
    }


    public void enableUser(String keycloakId, Boolean enable){
        UserResource userResource = usersResource.get(keycloakId);
        UserRepresentation user = userResource.toRepresentation();
        user.setEnabled(enable);
        userResource.update(user);
    }


    public void updateUser(String keycloakId, UpdateUserRequest request){
        UserResource userResource = usersResource.get(keycloakId);
        UserRepresentation user = userResource.toRepresentation();
        user.setUsername(request.getUsername());
        user.setFirstName(request.getFirstName());
        user.setLastName(request.getLastName());

        userResource.update(user);
    }


    public void changePassword(String keycloakId, String password){
        UserResource userResource = usersResource.get(keycloakId);
        UserRepresentation user = userResource.toRepresentation();
        user.setCredentials(List.of(createCredential(password)));

        userResource.update(user);
    }


    private void assignRole(String keycloakId, RoleName roleName){

        RoleRepresentation role = realmResource.roles()
                .get(roleName.name())
                .toRepresentation();
        usersResource.get(keycloakId)
                .roles()
                .realmLevel()
                .add(List.of(role));
    }



    private CredentialRepresentation createCredential(String password) {
        CredentialRepresentation credential = new CredentialRepresentation();
        credential.setType(CredentialRepresentation.PASSWORD);
        credential.setValue(password);
        credential.setTemporary(false);
        return credential;
    }
}
