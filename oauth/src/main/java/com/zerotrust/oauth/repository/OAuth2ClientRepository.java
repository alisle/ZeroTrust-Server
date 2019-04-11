package com.zerotrust.oauth.repository;

import com.zerotrust.oauth.model.OAuth2Client;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@SuppressWarnings("unused")
@RepositoryRestResource(exported = false)
public interface OAuth2ClientRepository extends JpaRepository<OAuth2Client, String> {

}
