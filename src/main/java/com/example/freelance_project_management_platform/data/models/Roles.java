package com.example.freelance_project_management_platform.data.models;

import com.example.freelance_project_management_platform.data.enums.Role;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Table;
import lombok.*;

@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "roles")
public class Roles extends BaseEntity {

    @Enumerated(EnumType.STRING)
    private Role name;
}
