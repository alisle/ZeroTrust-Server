package com.zerotrust.oauth.repository;

import com.zerotrust.oauth.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@SuppressWarnings("unused")
@RepositoryRestResource(exported = false)
public interface UserRepository extends JpaRepository<User, String> {

}
