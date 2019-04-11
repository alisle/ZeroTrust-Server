package com.zerotrust.oauth.service;

import com.zerotrust.oauth.model.AuthorizedGrantTypes;
import com.zerotrust.oauth.model.OAuth2Client;
import com.zerotrust.oauth.model.Scope;

import java.util.Optional;

public interface OAuth2ClientService {
    Optional<OAuth2Client> getClient(String clientID);
    Optional<OAuth2Client> createClient(String clientID, String clientSecret, Scope[] scopes, AuthorizedGrantTypes[] grants);
    Optional<OAuth2Client> createRestClient(String clientID, String clientSecret);
}
