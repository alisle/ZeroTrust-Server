package com.zerotrust.server.repository;

import com.zerotrust.server.model.Network;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.data.rest.core.annotation.RestResource;

import java.util.List;
import java.util.UUID;

@SuppressWarnings("unused")
@RepositoryRestResource(exported = true, path = "networks", collectionResourceRel = "networks", itemResourceRel = "network")
public interface NetworkRepository extends JpaRepository<Network, UUID> {

    @RestResource(exported = true)
    Page<Network> findAll(Pageable pageable);

    @RestResource(exported = false)
    List<Network> findAll();

    @RestResource(exported = false)
    List<Network> findByAddress(int address);


}
