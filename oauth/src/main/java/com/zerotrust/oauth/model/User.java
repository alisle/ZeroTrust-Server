package com.zerotrust.oauth.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Data
@Entity
@Table(name = "user")
public class User {
    @Id
    @NotNull
    @Column(name = "email", nullable = false)
    private String email;

    @NotNull
    @Column(name = "password", nullable = false)
    private String password;

    @NotNull
    @Column(name = "expired", nullable =  false)
    private boolean expired = false;

    @NotNull
    @Column(name = "account_locked", nullable = false)
    private boolean locked = false;

    @NotNull
    @Column(name = "credentials_expired", nullable = false)
    private boolean credentialsExpired = false;

    @NotNull
    @Column(name = "enabled", nullable = false)
    private boolean enabled = true;

    @EqualsAndHashCode.Exclude
    @ManyToMany(fetch = FetchType.EAGER, cascade = {
            CascadeType.PERSIST,
            CascadeType.MERGE,
            CascadeType.DETACH
    })
    @JoinTable(name="user_role",
            joinColumns = @JoinColumn(name = "email", referencedColumnName = "email"),
            inverseJoinColumns = @JoinColumn(name = "role_name", referencedColumnName = "name"))
    private Set<Role> roles = new HashSet<>();


    public UserDetails asUserDetails() {
        return new UserDetails() {
            @Override
            public Collection<? extends GrantedAuthority> getAuthorities() {
                return roles.stream().map(role -> role.asGrantedAuthority()).collect(Collectors.toSet());
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
                return !expired;
            }

            @Override
            public boolean isAccountNonLocked() {
                return !locked;
            }

            @Override
            public boolean isCredentialsNonExpired() {
                return !credentialsExpired;
            }

            @Override
            public boolean isEnabled() {
                return enabled;
            }
        };
    }
}
