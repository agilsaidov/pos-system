package com.app.pos.system.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor @AllArgsConstructor
@Data
@Table(name = "user_roles")
public class UserRole {

    @EmbeddedId
    private UserRoleId userRoleId;

    @MapsId("userId")
    @JoinColumn(name = "user_id")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    private User user;

    @MapsId("roleId")
    @JoinColumn(name = "role_id")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    private Role role;
}
