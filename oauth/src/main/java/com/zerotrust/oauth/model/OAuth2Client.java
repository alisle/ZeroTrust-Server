package com.zerotrust.oauth.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.provider.ClientDetails;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.*;
import java.util.stream.Collectors;

@Data
@Entity
@Table(name = "oauth_client_detail")
public class OAuth2Client {
    @Id
    @NotNull
    @Column(name="client_id", nullable = false)
    private String clientId;

    @NotNull
    @Column(name="client_secret", nullable = false)
    private String clientSecret;

    @Column(name="access_token_validity")
    private int accessTokenValidity;

    @Column(name="refresh_token_validity")
    private int refreshTokenValididty;

    @Column(name="additional_information")
    private String additionInformation;

    @Column(name = "auto_approve")
    private boolean autoApprove;

    @EqualsAndHashCode.Exclude
    @ManyToMany(fetch = FetchType.EAGER, cascade = {
            CascadeType.PERSIST,
            CascadeType.MERGE,
            CascadeType.DETACH
    })
    @JoinTable(name="oauth_client_detail_scope",
            joinColumns = @JoinColumn(name = "client_id", referencedColumnName = "client_id"),
            inverseJoinColumns = @JoinColumn(name = "scope_name", referencedColumnName = "name"))
    private Set<Scope> scopes = new HashSet<>();

    @EqualsAndHashCode.Exclude
    @ManyToMany(fetch = FetchType.EAGER, cascade = {
            CascadeType.PERSIST,
            CascadeType.MERGE,
            CascadeType.DETACH
    })
    @JoinTable(name="oauth_client_detail_authorized_grant_type",
            joinColumns = @JoinColumn(name = "client_id", referencedColumnName = "client_id"),
            inverseJoinColumns = @JoinColumn(name = "grant_name", referencedColumnName = "name"))
    private Set<AuthorizedGrantTypes> grants = new HashSet<>();


    @EqualsAndHashCode.Exclude
    @ManyToMany(fetch = FetchType.EAGER, cascade = {
            CascadeType.PERSIST,
            CascadeType.MERGE,
            CascadeType.DETACH
    })
    @JoinTable(name="oauth_client_detail_redirect_uri",
            joinColumns = @JoinColumn(name = "client_id", referencedColumnName = "client_id"),
            inverseJoinColumns = @JoinColumn(name = "uri", referencedColumnName = "uri"))
    private Set<RedirectURIs> redirectURIses = new HashSet<>();

    public ClientDetails asClientDetails() {
        return new ClientDetails() {
            @Override
            public String getClientId() {
                return clientId;
            }

            @Override
            public Set<String> getResourceIds() {
                return new HashSet<>();
            }

            @Override
            public boolean isSecretRequired() {
                return true;
            }

            @Override
            public String getClientSecret() {
                return clientSecret;
            }

            @Override
            public boolean isScoped() {
                return true;
            }

            @Override
            public Set<String> getScope() {
                return scopes.stream().map( scope -> scope.getName()).collect(Collectors.toSet());
            }

            @Override
            public Set<String> getAuthorizedGrantTypes() {
                return grants.stream().map(grant -> grant.getName()).collect(Collectors.toSet());
            }

            @Override
            public Set<String> getRegisteredRedirectUri() {
                return redirectURIses.stream().map(uri -> uri.getUri()).collect(Collectors.toSet());
            }

            @Override
            public Collection<GrantedAuthority> getAuthorities() {
                return new ArrayList<>();
            }

            @Override
            public Integer getAccessTokenValiditySeconds() {
                return accessTokenValidity;
            }

            @Override
            public Integer getRefreshTokenValiditySeconds() {
                return refreshTokenValididty;
            }

            @Override
            public boolean isAutoApprove(String scope) {
                return autoApprove;
            }

            @Override
            public Map<String, Object> getAdditionalInformation() {
                return new HashMap<>();
            }
        };
    }
}
