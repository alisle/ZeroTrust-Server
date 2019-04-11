package com.zerotrust.oauth.repository;


import com.zerotrust.oauth.model.Config;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.List;
import java.util.UUID;

@SuppressWarnings("unused")
@RepositoryRestResource(exported = false)
public interface ConfigRepository extends JpaRepository<Config, UUID> {
    List<Config> findAll();
}
