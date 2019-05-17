package com.zerotrust.links.web.controller;

import com.zerotrust.links.dto.AgentOnlineDTO;
import com.zerotrust.links.dto.UpdateInterfacesDTO;
import com.zerotrust.links.exception.AgentNotFoundException;
import com.zerotrust.links.exception.InvalidIPAddress;
import com.zerotrust.links.mapper.IPMapper;
import com.zerotrust.links.service.AgentService;
import com.zerotrust.links.service.ConnectionService;
import com.zerotrust.model.entity.Agent;
import com.zerotrust.model.entity.IPAddress;
import lombok.extern.slf4j.Slf4j;
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Set;
import java.util.UUID;

@RestController
@RequestMapping(value = "/agents")
@Slf4j
public class AgentController {
    private final AgentService agentService;
    private final IPMapper ipMapper;
    private final ConnectionService connectionService;


    public AgentController(AgentService agentService, IPMapper ipMapper, ConnectionService connectionService) {
        this.agentService = agentService;
        this.ipMapper = ipMapper;
        this.connectionService = connectionService;
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

}
