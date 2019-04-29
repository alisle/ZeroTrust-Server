package com.zerotrust.rest.web.controller;

import com.zerotrust.rest.exception.AgentNotFoundException;
import com.zerotrust.rest.exception.InvalidIPAddress;
import com.zerotrust.rest.mapper.IPMapper;
import com.zerotrust.rest.model.dto.AgentOnlineDTO;
import com.zerotrust.rest.model.dto.UpdateInterfacesDTO;
import com.zerotrust.rest.service.AgentService;
import com.zerotrust.rest.model.*;
import com.zerotrust.rest.service.ConnectionService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@RestController
@RequestMapping(value = "/agents")
@Slf4j
public class AgentController {
    private final ConnectionService connectionService;
    private final AgentService agentService;
    private final IPMapper ipMapper;

    public AgentController(ConnectionService connectionService, AgentService agentService, IPMapper ipMapper) {
        this.connectionService = connectionService;
        this.agentService = agentService;
        this.ipMapper = ipMapper;
    }

    @RequestMapping( value = "/online", method = RequestMethod.POST, produces = MediaTypes.HAL_JSON_VALUE)
    public ResponseEntity online(@Valid @RequestBody AgentOnlineDTO dto) throws InvalidIPAddress {
        log.debug("REST Request to online agent: " + dto.getId());
        Agent agent = agentService.online(dto.getId(), dto.getName());
        agentService.updateIPs(agent, ipMapper.convertStrings(dto.getInterfaces()));
        return new ResponseEntity(HttpStatus.OK);
    }

    @RequestMapping(value = "/offline/{id}", method = RequestMethod.DELETE,  produces = MediaTypes.HAL_JSON_VALUE)
    public ResponseEntity offline(@PathVariable("id") UUID id) throws AgentNotFoundException {
        log.debug("REST Request to offline agent: " + id);
        agentService.updateIPs(id, new IPAddress[] {});
        agentService.offline(id);

        return new ResponseEntity(HttpStatus.OK);
    }

    @RequestMapping(value = "/{id}/interfaces", method = RequestMethod.POST, produces = MediaTypes.HAL_JSON_VALUE)
    public ResponseEntity updateInterfaces(@PathVariable("id") UUID id, @Valid @RequestBody UpdateInterfacesDTO dto) throws AgentNotFoundException, InvalidIPAddress {
        log.debug("REST Request to update interfaces for agent: " + id);
        agentService.updateIPs(id, ipMapper.convertStrings(dto.getInterfaces()));
        return new ResponseEntity(HttpStatus.OK);
    }

    @RequestMapping( value = "/{id}/alive-connections", method = RequestMethod.POST, produces = MediaTypes.HAL_JSON_VALUE)
    public ResponseEntity aliveConnections(@PathVariable("id") UUID id, @Valid @RequestBody Set<Long> connections)  {

        log.debug("REST update to alive connections from : " + id);
        connectionService.validateConnections(id, connections);
        return new ResponseEntity(HttpStatus.OK);
    }

    @PreAuthorize("#oauth2.hasScope('read') and hasRole('agent_read')")
    @RequestMapping(value = "/search/users-source", method = RequestMethod.GET, produces = MediaTypes.HAL_JSON_VALUE)
    public ResponseEntity<List<UserCount>> getSourceUsers(@RequestParam("agent_id") UUID id)  throws AgentNotFoundException {
        log.debug("REST Request for source users:" + id);
        return new ResponseEntity<>(this.agentService.sourceUsersCount(id), null, HttpStatus.OK);
    }

    @PreAuthorize("#oauth2.hasScope('read') and hasRole('agent_read')")
    @RequestMapping(value = "/search/users-destination", method = RequestMethod.GET, produces = MediaTypes.HAL_JSON_VALUE)
    public ResponseEntity<List<UserCount>> getDestinationUsers(@RequestParam("agent_id") UUID id) throws AgentNotFoundException {
        log.debug("REST Request for source users:" + id);
        return new ResponseEntity<>(this.agentService.destinationUsersCount(id), null, HttpStatus.OK);
    }

    @PreAuthorize("#oauth2.hasScope('read') and hasRole('agent_read')")
    @RequestMapping(value = "/search/users", method = RequestMethod.GET, produces = MediaTypes.HAL_JSON_VALUE)
    public ResponseEntity<List<UserCount>> getUsers(@RequestParam("agent_id") UUID id) throws AgentNotFoundException {
        log.debug("REST Request for source users:" + id);
        List<UserCount> users = this.agentService.allUsersCount(id);
        return new ResponseEntity<>(users, null, HttpStatus.OK);
    }

    @PreAuthorize("#oauth2.hasScope('read') and hasRole('agent_read')")
    @RequestMapping(value = "/search/processes-source", method = RequestMethod.GET, produces = MediaTypes.HAL_JSON_VALUE)
    public ResponseEntity<List<ProcessCount>> getSourceProcesses(@RequestParam("agent_id") UUID id)  throws AgentNotFoundException {
        log.debug("REST Request for source users:" + id);
        return new ResponseEntity<>(this.agentService.sourceProcessCount(id), null, HttpStatus.OK);
    }

    @PreAuthorize("#oauth2.hasScope('read') and hasRole('agent_read')")
    @RequestMapping(value = "/search/processes-destination", method = RequestMethod.GET, produces = MediaTypes.HAL_JSON_VALUE)
    public ResponseEntity<List<ProcessCount>> getDestinationProcesses(@RequestParam("agent_id") UUID id) throws AgentNotFoundException {
        log.debug("REST Request for source users:" + id);
        return new ResponseEntity<>(this.agentService.destinationProcessCount(id), null, HttpStatus.OK);
    }

    @PreAuthorize("#oauth2.hasScope('read') and hasRole('agent_read')")
    @RequestMapping(value = "/search/count-incoming-connections", method = RequestMethod.GET, produces = MediaTypes.HAL_JSON_VALUE)
    public ResponseEntity<List<AgentCount>>  countIncomingConnections() {
        return new ResponseEntity<>(this.agentService.countIncomingConnections(), null, HttpStatus.OK);
    }

    @PreAuthorize("#oauth2.hasScope('read') and hasRole('agent_read')")
    @RequestMapping(value = "/search/count-outgoing-connections", method = RequestMethod.GET, produces = MediaTypes.HAL_JSON_VALUE)
    public ResponseEntity<List<AgentCount>>  countOutgoingConnections() {
        return new ResponseEntity<>(this.agentService.countOutgoingConnections(), null, HttpStatus.OK);
    }

    @PreAuthorize("#oauth2.hasScope('read') and hasRole('agent_read')")
    @RequestMapping(value = "/{id}", method = RequestMethod.POST, produces = MediaTypes.HAL_JSON_VALUE)
    public ResponseEntity<Agent> get(@PathVariable("id") UUID id) throws AgentNotFoundException {
        log.debug("REST Request for agent " + id);
        Agent agent = agentService.get(id).orElseThrow(() -> new AgentNotFoundException());
        return new ResponseEntity<>(agent, null, HttpStatus.OK);
    }
}
