package com.notrust.server.web.controller;

import com.notrust.server.exception.AgentNotFoundException;
import com.notrust.server.model.Agent;
import com.notrust.server.model.dto.AgentOnlineDTO;
import com.notrust.server.service.AgentService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.UUID;

@RestController
@RequestMapping(value = "/agents")
@Slf4j
public class AgentController {
    private final AgentService agentService;

    public AgentController(AgentService agentService) {
        this.agentService = agentService;
    }

    @RequestMapping( value = "/online", method = RequestMethod.POST, produces = MediaTypes.HAL_JSON_VALUE)
    public ResponseEntity online(@Valid @RequestBody AgentOnlineDTO dto) {
        log.debug("REST Request to online agent: " + dto.getId());
        Agent agent = agentService.online(dto.getId(), dto.getName());

        return new ResponseEntity(HttpStatus.OK);
    }

    @RequestMapping(value = "/offline/{id}", method = RequestMethod.DELETE,  produces = MediaTypes.HAL_JSON_VALUE)
    public ResponseEntity offline(@PathVariable("id") UUID id) throws AgentNotFoundException {
        log.debug("REST Request to offline agent: " + id);
        agentService.offline(id);

        return new ResponseEntity(HttpStatus.OK);
    }
}
