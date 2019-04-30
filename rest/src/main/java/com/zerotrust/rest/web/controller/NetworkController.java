package com.zerotrust.rest.web.controller;


import com.zerotrust.rest.model.AgentCount;
import com.zerotrust.rest.model.Network;
import com.zerotrust.rest.service.NetworkService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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
@RequestMapping(value = "/networks")
@Slf4j
public class NetworkController {
    private final NetworkService service;

    public NetworkController(NetworkService service) {
        this.service = service;
    }

    @PreAuthorize("#oauth2.hasScope('read') and hasRole('networks_read')")
    @RequestMapping( value = "", method = RequestMethod.GET, produces = MediaTypes.HAL_JSON_VALUE)
    public ResponseEntity<Page<Network>> all(Pageable pageable) {
        log.debug("REST request for all connection links");
        return new ResponseEntity<>(this.service.getPage(pageable), null, HttpStatus.OK);
    }

    @PreAuthorize("#oauth2.hasScope('read') and hasRole('networks_read')")
    @RequestMapping(value = "/search/count-active-destination-connections", method = RequestMethod.GET, produces = MediaTypes.HAL_JSON_VALUE)
    public ResponseEntity<List<AgentCount>> countActiveDestinationConnections(@RequestParam("network_id") UUID id) {
        return new ResponseEntity<>(service.countActiveDestinationConnections(id), null, HttpStatus.OK);
    }

    @PreAuthorize("#oauth2.hasScope('read') and hasRole('networks_read')")
    @RequestMapping(value = "/search/count-active-source-connections", method = RequestMethod.GET, produces = MediaTypes.HAL_JSON_VALUE)
    public ResponseEntity<List<AgentCount>> countActiveSourceConnections(@RequestParam("network_id") UUID id) {
        return new ResponseEntity<>(service.countActiveSourceConnections(id), null, HttpStatus.OK);
    }
}
