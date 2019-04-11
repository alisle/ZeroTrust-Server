package com.zerotrust.oauth.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.HashSet;
import java.util.Set;

@Data
@Entity
@Table(name="oauth_scope")
public class Scope {
    @Id
    @NotNull
    @Column(name="name", nullable = false)
    private String name;

    public Scope() {
    }

    public Scope(@NotNull String name) {
        this.name = name;
    }

    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @ManyToMany(fetch = FetchType.LAZY, cascade = {
            CascadeType.PERSIST,
            CascadeType.MERGE,
            CascadeType.DETACH
    })
    @JoinTable(name="oauth_client_detail_scope",
            joinColumns = @JoinColumn(name = "scope_name", referencedColumnName = "name"),
            inverseJoinColumns = @JoinColumn(name = "client_id", referencedColumnName = "client_id"))
    private Set<OAuth2Client> clients = new HashSet<>();
}
