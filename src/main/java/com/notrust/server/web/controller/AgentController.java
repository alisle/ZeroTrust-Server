package com.notrust.server.web.controller;

import com.notrust.server.exception.AgentNotFoundException;
import com.notrust.server.exception.InvalidIPAddress;
import com.notrust.server.mapper.IPMapper;
import com.notrust.server.model.Agent;
import com.notrust.server.model.IPAddress;
import com.notrust.server.model.ProcessCount;
import com.notrust.server.model.UserCount;
import com.notrust.server.model.dto.AgentOnlineDTO;
import com.notrust.server.model.dto.UpdateInterfacesDTO;
import com.notrust.server.service.AgentService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping(value = "/agents")
@Slf4j
public class AgentController {
    private final AgentService agentService;
    private final IPMapper ipMapper;

    public AgentController(AgentService agentService, IPMapper ipMapper) {
        this.agentService = agentService;
        this.ipMapper = ipMapper;
    }

    @RequestMapping( value = "/online", method = RequestMethod.POST, produces = MediaTypes.HAL_JSON_VALUE)
    public ResponseEntity online(@Valid @RequestBody AgentOnlineDTO dto) throws InvalidIPAddress  {
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

    @RequestMapping(value = "/search/users-source", method = RequestMethod.GET, produces = MediaTypes.HAL_JSON_VALUE)
    public ResponseEntity<List<UserCount>> getSourceUsers(@RequestParam("agent_id") UUID id)  throws AgentNotFoundException {
        log.debug("REST Request for source users:" + id);
        return new ResponseEntity<>(this.agentService.sourceUsersCount(id), null, HttpStatus.OK);
    }

    @RequestMapping(value = "/search/users-destination", method = RequestMethod.GET, produces = MediaTypes.HAL_JSON_VALUE)
    public ResponseEntity<List<UserCount>> getDestinationUsers(@RequestParam("agent_id") UUID id) throws AgentNotFoundException {
        log.debug("REST Request for source users:" + id);
        return new ResponseEntity<>(this.agentService.destinationUsersCount(id), null, HttpStatus.OK);
    }


    @RequestMapping(value = "/search/users", method = RequestMethod.GET, produces = MediaTypes.HAL_JSON_VALUE)
    public ResponseEntity<List<UserCount>> getUsers(@RequestParam("agent_id") UUID id) throws AgentNotFoundException {
        log.debug("REST Request for source users:" + id);
        List<UserCount> users = this.agentService.allUsersCount(id);
        return new ResponseEntity<>(users, null, HttpStatus.OK);
    }

    @RequestMapping(value = "/search/processes-source", method = RequestMethod.GET, produces = MediaTypes.HAL_JSON_VALUE)
    public ResponseEntity<List<ProcessCount>> getSourceProcesses(@RequestParam("agent_id") UUID id)  throws AgentNotFoundException {
        log.debug("REST Request for source users:" + id);
        return new ResponseEntity<>(this.agentService.sourceProcessCount(id), null, HttpStatus.OK);
    }

    @RequestMapping(value = "/search/processes-destination", method = RequestMethod.GET, produces = MediaTypes.HAL_JSON_VALUE)
    public ResponseEntity<List<ProcessCount>> getDestinationProcesses(@RequestParam("agent_id") UUID id) throws AgentNotFoundException {
        log.debug("REST Request for source users:" + id);
        return new ResponseEntity<>(this.agentService.destinationProcessCount(id), null, HttpStatus.OK);
    }

}
