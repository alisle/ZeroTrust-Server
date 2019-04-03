package com.zerotrust.server.web.controller;


import com.zerotrust.server.model.AgentCount;
import com.zerotrust.server.service.NetworkService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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

    @RequestMapping(value = "/search/count-active-destination-connections", method = RequestMethod.GET, produces = MediaTypes.HAL_JSON_VALUE)
    public ResponseEntity<List<AgentCount>> countActiveDestinationConnections(@RequestParam("network_id") UUID id) {
        return new ResponseEntity<>(service.countActiveDestinationConnections(id), null, HttpStatus.OK);
    }


    @RequestMapping(value = "/search/count-active-source-connections", method = RequestMethod.GET, produces = MediaTypes.HAL_JSON_VALUE)
    public ResponseEntity<List<AgentCount>> countActiveSourceConnections(@RequestParam("network_id") UUID id) {
        return new ResponseEntity<>(service.countActiveSourceConnections(id), null, HttpStatus.OK);
    }
}
