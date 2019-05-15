package com.zerotrust.links;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@EnableJpaRepositories("com.zerotrust.*")
@EntityScan("com.zerotrust.*")
@ComponentScan(basePackages = { "com.zerotrust" })
@SpringBootApplication
public class LinksServerApplication {
    public static void main(String[] args) {
        SpringApplication.run(LinksServerApplication.class, args);
    }
}
