package com.example.freelance_project_management_platform.data.models;

import com.example.freelance_project_management_platform.data.enums.Skill;
import com.example.freelance_project_management_platform.data.enums.TechnologyStack;
import com.example.freelance_project_management_platform.data.enums.UserStatus;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "users")
public class User extends BaseEntity implements UserDetails {

    @Column(nullable = false)
    private String name;

    @Column(unique = true, nullable = false)
    private String email;

    @Column(nullable = false)
    private String password;

    private Long createdAt;

    private Long updatedAt;

    @Enumerated(EnumType.STRING)
    private UserStatus status;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "users_roles",
            joinColumns = @JoinColumn(name = "user_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "role_id", referencedColumnName = "id")
    )
    private Set<Roles> roles = new HashSet<>();

    private String verificationCode;

    private Boolean enabled;

    private LocalDateTime verificationCodeExpirationTime;

    //! Freelancer fields

    //? Freelancer skills
    @Enumerated(EnumType.STRING)
    private Set<Skill> skills = new HashSet<>();

    //? Freelancer technology stack
    @Enumerated(EnumType.STRING)
    private Set<TechnologyStack> technologyStacks = new HashSet<>();

    //? Freelancer experience in years
    private Integer experience;

    //? Freelancer rating
    private Double rating;

    //? Freelancer availability
    private Boolean isAvailable;

    //? Freelancer hourly rate
    private Double hourlyRate;

    //! Client fields

    //? Client company name
    private String companyName;

    //? Client projects posted
    private Integer projectsPosted;

    //? Client rating
    private Double clientRating;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return roles.stream().map(role -> new SimpleGrantedAuthority("ROLE_" + role.getName())).collect(Collectors.toList());
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
