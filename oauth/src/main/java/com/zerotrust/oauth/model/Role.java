package com.zerotrust.oauth.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.springframework.security.core.GrantedAuthority;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.HashSet;
import java.util.Set;

@Data
@Entity
@Table(name = "role")
public class Role {
    @Id
    @NotNull
    @Column(name = "name", nullable = false)
    private String name;

    public Role() {}
    public Role(@NotNull String name) { this.name = name; }

    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @ManyToMany(fetch = FetchType.LAZY, cascade = {
            CascadeType.PERSIST,
            CascadeType.MERGE,
            CascadeType.DETACH,
    })
    @JoinTable(name="user_role",
            joinColumns = @JoinColumn(name = "role_name", referencedColumnName = "name"),
            inverseJoinColumns = @JoinColumn(name = "email", referencedColumnName = "email"))
    private Set<User> clients = new HashSet<>();

    public GrantedAuthority asGrantedAuthority() {
        return new GrantedAuthority() {
            @Override
            public String getAuthority() {
                return name;
            }
        };
    }
}
