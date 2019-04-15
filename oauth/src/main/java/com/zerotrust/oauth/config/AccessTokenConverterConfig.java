package com.zerotrust.oauth.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;

@Slf4j
@Configuration
public class AccessTokenConverterConfig {
    @Value("${environment.ZEROTRUST_SIGN_KEY:db5bc76a-d0dd-420d-bca4-a3178be18189}")
    private String key;

    @Bean
    public JwtAccessTokenConverter accessTokenConverter() {
        final JwtAccessTokenConverter converter = new JwtAccessTokenConverter();
        converter.setSigningKey(key);
        return converter;
    }

}
