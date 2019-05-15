package com.zerotrust.links.repository;

import com.zerotrust.model.Network;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.List;
import java.util.UUID;

@SuppressWarnings("unused")
@RepositoryRestResource(exported = false)
public interface NetworkRepository extends JpaRepository<Network, UUID> {
    List<Network> findAll();
}
