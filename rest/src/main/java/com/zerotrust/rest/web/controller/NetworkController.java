package com.zerotrust.rest.web.controller;


import com.zerotrust.model.AgentCount;
import com.zerotrust.rest.repository.NetworkRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.hateoas.MediaTypes;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping(value = "/api/networks")
@Slf4j
public class NetworkController {
    private final NetworkRepository repository;

    public NetworkController(NetworkRepository repository) {
        this.repository = repository;
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
}
