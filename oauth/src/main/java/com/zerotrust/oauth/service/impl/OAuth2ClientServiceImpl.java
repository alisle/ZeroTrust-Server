package com.zerotrust.oauth.service.impl;

import com.zerotrust.oauth.model.AuthorizedGrantTypes;
import com.zerotrust.oauth.model.OAuth2Client;
import com.zerotrust.oauth.model.Scope;
import com.zerotrust.oauth.repository.OAuth2ClientRepository;
import com.zerotrust.oauth.service.ConfigService;
import com.zerotrust.oauth.service.OAuth2ClientService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.core.env.Environment;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.validation.constraints.NotNull;
import java.util.*;

@Slf4j
@Service
public class OAuth2ClientServiceImpl implements OAuth2ClientService {
    private final ConfigService configService;
    private final Environment environment;
    private final OAuth2ClientRepository repository;
    private final PasswordEncoder passwordEncoder;

    @Value("${com.zeortrust.oauth.rest-client-id}")
    private String rest_client_id;

    @Value("${com.zerotrust.oauth.rest-client-secret}")
    private String rest_client_secret;

    public OAuth2ClientServiceImpl(ConfigService configService, Environment environment, OAuth2ClientRepository repository, PasswordEncoder passwordEncoder) {
        this.configService = configService;
        this.environment = environment;
        this.repository = repository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public Optional<OAuth2Client> getClient(String clientID) {
        return repository.findById(clientID);
    }

    public Optional<OAuth2Client> createClient(@NotNull String clientID, @NotNull String clientSecret, @NotNull Scope[] scopes, @NotNull AuthorizedGrantTypes[] grants) {

        OAuth2Client details = new OAuth2Client();
        details.setClientId(clientID);
        details.setClientSecret(passwordEncoder.encode(clientSecret));
        details.setScopes(new HashSet<>(Arrays.asList(scopes)));
        details.setGrants(new HashSet<>(Arrays.asList(grants)));
        details.setAutoApprove(true);
        details.setRefreshTokenValididty(2592000);
        details.setAccessTokenValidity(3600);

        details = repository.save(details);
        log.info("Creating a new client: " + details);

        return Optional.ofNullable(details);
    }

    public Optional<OAuth2Client> createRestClient(@NotNull String clientID, @NotNull String clientSecret) {
        return createClient(
                clientID,
                clientSecret,
                new Scope[] { new Scope("read"),  new Scope("write") },
                new AuthorizedGrantTypes[] {
                        new AuthorizedGrantTypes("password"),
                        new AuthorizedGrantTypes("authorization_code" ),
                        new AuthorizedGrantTypes("refresh_token" ),
                        new AuthorizedGrantTypes("client_credentials" )
                }
        );

    }

    @EventListener
    public void onApplicationEvent(final ApplicationReadyEvent event) {
        // We need to check if this is the first run, if it is pick up the clientId and Secret and make a client details for the REST project
        if(this.configService.isFirstRun()) {
            createRestClient(rest_client_id, rest_client_secret);
        }
    }

}
