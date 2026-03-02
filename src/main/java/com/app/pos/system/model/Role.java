package com.app.pos.system.model;

import com.app.pos.system.model.enums.RoleName;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor @AllArgsConstructor
@Table(name = "roles")
public class Role {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Short roleId;

    @Column(name = "name", nullable = false)
    @Enumerated(value = EnumType.STRING)
    private RoleName roleName;
}
