package com.zerotrust.rest.web.controller;

import com.zerotrust.rest.model.Agent;
import com.zerotrust.rest.exception.AgentNotFoundException;
import com.zerotrust.rest.service.AgentService;
import com.zerotrust.rest.model.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping(value = "/agents")
@Slf4j
public class AgentController {
    private final AgentService agentService;

    public AgentController(AgentService agentService) {
        this.agentService = agentService;
    }

    @PreAuthorize("hasAuthority('agent_read')")
    @RequestMapping( value = "", method = RequestMethod.GET, produces = MediaTypes.HAL_JSON_VALUE)
    public ResponseEntity<Page<Agent>> all(Pageable pageable) {
        log.debug("REST request to get all agents");
        return new ResponseEntity<>(this.agentService.getPage(pageable), null, HttpStatus.OK);
    }

    @PreAuthorize("hasAuthority('agent_read')")
    @RequestMapping(value = "/search/users-source", method = RequestMethod.GET, produces = MediaTypes.HAL_JSON_VALUE)
    public ResponseEntity<List<UserCount>> getSourceUsers(@RequestParam("agent_id") UUID id)  throws AgentNotFoundException {
        log.debug("REST Request for source users:" + id);
        return new ResponseEntity<>(this.agentService.sourceUsersCount(id), null, HttpStatus.OK);
    }

    @PreAuthorize("hasAuthority('agent_read')")
    @RequestMapping(value = "/search/users-destination", method = RequestMethod.GET, produces = MediaTypes.HAL_JSON_VALUE)
    public ResponseEntity<List<UserCount>> getDestinationUsers(@RequestParam("agent_id") UUID id) throws AgentNotFoundException {
        log.debug("REST Request for source users:" + id);
        return new ResponseEntity<>(this.agentService.destinationUsersCount(id), null, HttpStatus.OK);
    }

    @PreAuthorize("hasAuthority('agent_read')")
    @RequestMapping(value = "/search/users", method = RequestMethod.GET, produces = MediaTypes.HAL_JSON_VALUE)
    public ResponseEntity<List<UserCount>> getUsers(@RequestParam("agent_id") UUID id) throws AgentNotFoundException {
        log.debug("REST Request for source users:" + id);
        List<UserCount> users = this.agentService.allUsersCount(id);
        return new ResponseEntity<>(users, null, HttpStatus.OK);
    }

    @PreAuthorize("hasAuthority('agent_read')")
    @RequestMapping(value = "/search/processes-source", method = RequestMethod.GET, produces = MediaTypes.HAL_JSON_VALUE)
    public ResponseEntity<List<ProcessCount>> getSourceProcesses(@RequestParam("agent_id") UUID id)  throws AgentNotFoundException {
        log.debug("REST Request for source users:" + id);
        return new ResponseEntity<>(this.agentService.sourceProcessCount(id), null, HttpStatus.OK);
    }

    @PreAuthorize("hasAuthority('agent_read')")
    @RequestMapping(value = "/search/processes-destination", method = RequestMethod.GET, produces = MediaTypes.HAL_JSON_VALUE)
    public ResponseEntity<List<ProcessCount>> getDestinationProcesses(@RequestParam("agent_id") UUID id) throws AgentNotFoundException {
        log.debug("REST Request for source users:" + id);
        return new ResponseEntity<>(this.agentService.destinationProcessCount(id), null, HttpStatus.OK);
    }

    @PreAuthorize("hasAuthority('agent_read')")
    @RequestMapping(value = "/search/count-incoming-connections", method = RequestMethod.GET, produces = MediaTypes.HAL_JSON_VALUE)
    public ResponseEntity<List<AgentCount>>  countIncomingConnections() {
        return new ResponseEntity<>(this.agentService.countIncomingConnections(), null, HttpStatus.OK);
    }

    @PreAuthorize("hasAuthority('agent_read')")
    @RequestMapping(value = "/search/count-outgoing-connections", method = RequestMethod.GET, produces = MediaTypes.HAL_JSON_VALUE)
    public ResponseEntity<List<AgentCount>>  countOutgoingConnections() {
        return new ResponseEntity<>(this.agentService.countOutgoingConnections(), null, HttpStatus.OK);
    }

    @PreAuthorize("hasAuthority('agent_read')")
    @RequestMapping(value = "/{id}", method = RequestMethod.GET, produces = MediaTypes.HAL_JSON_VALUE)
    public ResponseEntity<Agent> get(@PathVariable("id") UUID id) throws AgentNotFoundException {
        log.debug("REST Request for agent " + id);
        Agent agent = agentService.get(id).orElseThrow(() -> new AgentNotFoundException());
        return new ResponseEntity<>(agent, null, HttpStatus.OK);
    }


    @PreAuthorize("hasAuthority('agent_read')")
    @RequestMapping(value = "/search/total-alive-agents", method = RequestMethod.GET, produces = MediaTypes.HAL_JSON_VALUE)
    public ResponseEntity<Long> totalAliveAgents() {
        log.debug("REST Request for total alive agents");
        return new ResponseEntity<>(agentService.totalAliveAgents(), null, HttpStatus.OK);
    }

    @PreAuthorize("hasAuthority('agent_read')")
    @RequestMapping(value = "/search/total-agents", method = RequestMethod.GET, produces = MediaTypes.HAL_JSON_VALUE)
    public ResponseEntity<Long> totalAgents() {
        log.debug("REST Request for total agents");
        return new ResponseEntity<>(agentService.totalAgents(), null, HttpStatus.OK);
    }
}
