package com.zerotrust.rest.web.controller;

import com.zerotrust.model.entity.AgentCount;
import com.zerotrust.model.entity.ProcessCount;
import com.zerotrust.model.entity.UserCount;
import com.zerotrust.rest.exception.AgentNotFoundException;
import com.zerotrust.rest.repository.AgentRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping(value = "/api/agents")
@Slf4j
public class AgentController {
    private final AgentRepository agentRepository;

    public AgentController(AgentRepository agentRepository) {
        this.agentRepository = agentRepository;
    }

    @PreAuthorize("hasAuthority('agents_read')")
    @RequestMapping(value = "/search/users-source", method = RequestMethod.GET, produces = MediaTypes.HAL_JSON_VALUE)
    public ResponseEntity<List<UserCount>> getSourceUsers(@RequestParam("agent_id") UUID id)  throws AgentNotFoundException {
        log.debug("REST Request for source users:" + id);
        return new ResponseEntity<>(this.agentRepository.countSourceUsername(id), null, HttpStatus.OK);
    }

    @PreAuthorize("hasAuthority('agents_read')")
    @RequestMapping(value = "/search/users-destination", method = RequestMethod.GET, produces = MediaTypes.HAL_JSON_VALUE)
    public ResponseEntity<List<UserCount>> getDestinationUsers(@RequestParam("agent_id") UUID id) throws AgentNotFoundException {
        log.debug("REST Request for source users:" + id);
        return new ResponseEntity<>(this.agentRepository.countDestinationUsername(id), null, HttpStatus.OK);
    }

    @PreAuthorize("hasAuthority('agents_read')")
    @RequestMapping(value = "/search/users", method = RequestMethod.GET, produces = MediaTypes.HAL_JSON_VALUE)
    public ResponseEntity<List<UserCount>> getUsers(@RequestParam("agent_id") UUID id) throws AgentNotFoundException {
        log.debug("REST Request for source users:" + id);
        List<UserCount> users = this.agentRepository.countUsername(id);
        return new ResponseEntity<>(users, null, HttpStatus.OK);
    }

    @PreAuthorize("hasAuthority('agents_read')")
    @RequestMapping(value = "/search/processes-source", method = RequestMethod.GET, produces = MediaTypes.HAL_JSON_VALUE)
    public ResponseEntity<List<ProcessCount>> getSourceProcesses(@RequestParam("agent_id") UUID id)  throws AgentNotFoundException {
        log.debug("REST Request for source users:" + id);
        return new ResponseEntity<>(this.agentRepository.countSourceProcess(id), null, HttpStatus.OK);
    }

    @PreAuthorize("hasAuthority('agents_read')")
    @RequestMapping(value = "/search/processes-destination", method = RequestMethod.GET, produces = MediaTypes.HAL_JSON_VALUE)
    public ResponseEntity<List<ProcessCount>> getDestinationProcesses(@RequestParam("agent_id") UUID id) throws AgentNotFoundException {
        log.debug("REST Request for source users:" + id);
        return new ResponseEntity<>(this.agentRepository.countDestinationProcess(id), null, HttpStatus.OK);
    }

    @PreAuthorize("hasAuthority('agents_read')")
    @RequestMapping(value = "/search/count-incoming-connections", method = RequestMethod.GET, produces = MediaTypes.HAL_JSON_VALUE)
    public ResponseEntity<List<AgentCount>>  countIncomingConnections() {
        return new ResponseEntity<>(this.agentRepository.countIncomingConnections(), null, HttpStatus.OK);
    }

    @PreAuthorize("hasAuthority('agents_read')")
    @RequestMapping(value = "/search/count-outgoing-connections", method = RequestMethod.GET, produces = MediaTypes.HAL_JSON_VALUE)
    public ResponseEntity<List<AgentCount>>  countOutgoingConnections() {
        return new ResponseEntity<>(this.agentRepository.countOutgoingConnections(), null, HttpStatus.OK);
    }

}
