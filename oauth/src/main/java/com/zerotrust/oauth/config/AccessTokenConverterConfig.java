package com.zerotrust.oauth.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.KeyStoreKeyFactory;

@Slf4j
@Configuration
public class AccessTokenConverterConfig {
    @Value("${com.zerotrust.oauth.jwt_keystore}")
    private String keyStore;

    @Value("${com.zerotrust.oauth.jwt_keystore_password}")
    private String password;

    @Value("${com.zerotrust.oauth.jwt_key_alias}")
    private String alias;

    @Bean
    public JwtAccessTokenConverter accessTokenConverter() {
        final JwtAccessTokenConverter converter = new JwtAccessTokenConverter();
        try {
            Resource resource = new FileSystemResource(keyStore);

            log.info("attempting to load " + resource.toString());
            if(!resource.exists()) {
                throw new RuntimeException("file not found!");
            }

            final KeyStoreKeyFactory keyStoreKeyFactory = new KeyStoreKeyFactory(resource, password.toCharArray());
            converter.setKeyPair(keyStoreKeyFactory.getKeyPair(alias));
        } catch (Exception ex) {
            log.error("unable to create converter!");
        }

        return converter;
    }

}
