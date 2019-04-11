package com.zerotrust.rest.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.provider.token.RemoteTokenServices;

@Configuration
public class TokenServicesConfig extends ResourceServerConfigurerAdapter {
    @Override
    public void configure(final HttpSecurity http) throws Exception {
        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED)
                .and().authorizeRequests().anyRequest().permitAll();
    }

    @Primary
    @Bean
    public RemoteTokenServices tokenServices() {
        final RemoteTokenServices tokenServices = new RemoteTokenServices();
        tokenServices.setCheckTokenEndpointUrl("http://zerotrust-oauth:8080/oath/check_token");
        tokenServices.setClientId("clientPassword");
        tokenServices.setClientSecret("secret");

        return tokenServices;
    }


}
