package com.zerotrust.rest.web.controller;


import com.zerotrust.model.entity.AgentCount;
import com.zerotrust.rest.dto.NetworkDTO;
import com.zerotrust.rest.exception.InvalidNetworkException;
import com.zerotrust.rest.exception.InvalidNetworkMaskException;
import com.zerotrust.rest.repository.NetworkRepository;
import com.zerotrust.rest.service.NetworkService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.hateoas.MediaTypes;

import javax.validation.Valid;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping(value = "/api/networks")
@Slf4j
public class NetworkController {
    private final NetworkRepository repository;
    private final NetworkService service;

    public NetworkController(NetworkRepository repository, NetworkService service) {
        this.repository = repository;
        this.service = service;
    }


// These are hacks around the issue with the repository and agentcounts.

    @PreAuthorize("hasAuthority('networks_read')")
    @RequestMapping(value = "/search/count-active-destination-connections", method = RequestMethod.GET, produces = MediaTypes.HAL_JSON_VALUE)
    public ResponseEntity<List<AgentCount>> activeDestinationConnections(@RequestParam("network_id") UUID id) {
        return new ResponseEntity<>(repository.activeDestinationConnections(id), null, HttpStatus.OK);
    }

    @PreAuthorize("hasAuthority('networks_read')")
    @RequestMapping(value = "/search/count-active-source-connections", method = RequestMethod.GET, produces = MediaTypes.HAL_JSON_VALUE)
    public ResponseEntity<List<AgentCount>> activeSourceConnections(@RequestParam("network_id") UUID id) {
        return new ResponseEntity<>(repository.activeSourceConnections(id), null, HttpStatus.OK);
    }

    @PreAuthorize("hasAuthority('networks_write')")
    @RequestMapping(value = "", method = RequestMethod.POST, produces = MediaTypes.HAL_JSON_VALUE)
    public ResponseEntity post(@Valid @RequestBody NetworkDTO dto) throws InvalidNetworkException, InvalidNetworkMaskException {
        service.add(dto);
        return new ResponseEntity(HttpStatus.OK);
    }
}
