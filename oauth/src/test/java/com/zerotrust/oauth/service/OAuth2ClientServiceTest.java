package com.zerotrust.oauth.service;

import com.zerotrust.oauth.AuthorizationServerApplication;
import com.zerotrust.oauth.model.AuthorizedGrantTypes;
import com.zerotrust.oauth.model.OAuth2Client;
import com.zerotrust.oauth.model.Scope;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.env.Environment;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.provider.ClientDetails;
import org.springframework.security.oauth2.provider.ClientDetailsService;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import java.util.UUID;

@RunWith(SpringRunner.class)
@WebAppConfiguration
@SpringBootTest(classes = AuthorizationServerApplication.class)
@ActiveProfiles("dev")
public class OAuth2ClientServiceTest {

    @Autowired
    private  OAuth2ClientService service;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Test
    public void testCreateRestClient() throws Exception {
        String id = UUID.randomUUID().toString();
        String password = "I am a password";

        OAuth2Client client = service.createRestClient(id, password).orElseThrow(() -> new RuntimeException());
        Assert.assertEquals(id, client.getClientId());
        Assert.assertTrue(passwordEncoder.matches(password, client.getClientSecret()));

        Assert.assertNotNull(service.getClient(id));
        Assert.assertEquals(client.getGrants(), service.getClient(id).get().getGrants());
        Assert.assertEquals(client.getScopes(), service.getClient(id).get().getScopes());
    }

    @Test
    public void testCreateClient() throws Exception {
        String clientId = UUID.randomUUID().toString();
        String password = UUID.randomUUID().toString();

        Scope[] scopes = new Scope[] {
                new Scope("read")
        };

        AuthorizedGrantTypes[] types = new AuthorizedGrantTypes[] {
                new AuthorizedGrantTypes("implicit")
        };

        OAuth2Client client = service.createClient(clientId, password, scopes, types).orElseThrow(() -> new RuntimeException());
        Assert.assertEquals(clientId, client.getClientId());
        Assert.assertTrue(passwordEncoder.matches(password, client.getClientSecret()));
        Assert.assertTrue(client.getScopes().contains(scopes[0]));
        Assert.assertTrue(client.getGrants().contains(types[0]));
    }

    @Test
    public void testLoadClientDetails() throws Exception {
        String clientId = UUID.randomUUID().toString();
        String secret = UUID.randomUUID().toString();

        OAuth2Client client = service.createRestClient(clientId, secret).orElseThrow(() -> new RuntimeException());
        ClientDetails clientDetails = client.asClientDetails();

        Assert.assertNotNull(clientDetails);
        Assert.assertEquals(client.getClientId(), clientDetails.getClientId());
        Assert.assertEquals(0, clientDetails.getResourceIds().size());
        Assert.assertTrue(clientDetails.isSecretRequired());
        Assert.assertEquals(client.getClientSecret(), clientDetails.getClientSecret());
        Assert.assertTrue(clientDetails.isScoped());

        Assert.assertEquals(client.getScopes().size(), clientDetails.getScope().size());
        client.getScopes().forEach(scope -> Assert.assertTrue(clientDetails.getScope().contains(scope.getName())));

        Assert.assertEquals(client.getGrants().size(), clientDetails.getAuthorizedGrantTypes().size());
        client.getGrants().forEach(grant -> clientDetails.getAuthorizedGrantTypes().contains(grant.getName()));

        Assert.assertEquals(client.getRedirectURIses().size(), clientDetails.getRegisteredRedirectUri().size());
        client.getRedirectURIses().forEach(uri -> clientDetails.getRegisteredRedirectUri().contains(uri.getUri()));

        Assert.assertEquals(client.getAccessTokenValidity(), clientDetails.getAccessTokenValiditySeconds().intValue());
        Assert.assertEquals(client.getRefreshTokenValididty(), clientDetails.getRefreshTokenValiditySeconds().intValue());
        Assert.assertEquals(client.isAutoApprove(), clientDetails.isAutoApprove(null));

    }


}
