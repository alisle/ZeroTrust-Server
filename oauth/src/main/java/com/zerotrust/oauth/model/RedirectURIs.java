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
@Table(name="oauth_redirect_uri")
public class RedirectURIs {
    @Id
    @NotNull
    @Column(name="uri", nullable = false)
    private String uri;

    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @ManyToMany(fetch = FetchType.LAZY, cascade = {
            CascadeType.PERSIST,
            CascadeType.MERGE,
            CascadeType.DETACH
    })
    @JoinTable(name="oauth_client_detail_redirect_uri",
            joinColumns = @JoinColumn(name = "uri", referencedColumnName = "uri"),
            inverseJoinColumns = @JoinColumn(name = "client_id", referencedColumnName = "client_id"))
    private Set<OAuth2Client> clients = new HashSet<>();
}
