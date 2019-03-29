package com.zerotrust.server.repository;

import com.zerotrust.server.model.IPAddress;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@SuppressWarnings("unused")
@RepositoryRestResource(exported = false)
public interface IPAddressRepository extends JpaRepository<IPAddress, Integer> {

}
