package com.zerotrust.rest.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.rest.core.config.RepositoryRestConfiguration;
import org.springframework.data.rest.core.mapping.ExposureConfiguration;
import org.springframework.data.rest.webmvc.config.RepositoryRestConfigurer;
import org.springframework.http.HttpMethod;

@Configuration
public class DataRestConfig implements RepositoryRestConfigurer {
    // Limit the functions we're exposing through the API.
    // Any updating / creation of objects should be handled through the controllers.
    @Override
    public void configureRepositoryRestConfiguration(RepositoryRestConfiguration config) {
        ExposureConfiguration exposureConfiguration = config.getExposureConfiguration();
        exposureConfiguration.withItemExposure(((metdata, httpMethods) -> httpMethods.disable(HttpMethod.PUT, HttpMethod.POST, HttpMethod.PATCH, HttpMethod.DELETE)))
                .withCollectionExposure((metdata, httpMethods) -> httpMethods.disable(HttpMethod.PUT, HttpMethod.POST, HttpMethod.PATCH, HttpMethod.DELETE));
    }
}