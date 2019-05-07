package com.zerotrust.links.web.controller;

import com.zerotrust.links.dto.ConnectionCloseDTO;
import com.zerotrust.links.dto.ConnectionOpenDTO;
import com.zerotrust.links.exception.InsertFailedException;
import com.zerotrust.links.exception.NoConnectionFoundException;
import com.zerotrust.links.service.ConnectionService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping(value = "/connections")
@Slf4j
public class ConnectionController {
    private final ConnectionService connectionService;

    public ConnectionController(ConnectionService connectionService) {
        this.connectionService = connectionService;
    }

    @RequestMapping(value = "/open", method = RequestMethod.POST, produces = MediaTypes.HAL_JSON_VALUE)
    public ResponseEntity open(@Valid @RequestBody ConnectionOpenDTO dto) throws InsertFailedException {
        log.debug("REST Request to open connection: " + dto.getId());
        connectionService.open(dto).orElseThrow(() -> new InsertFailedException());
        return new ResponseEntity(HttpStatus.OK);
    }

    @RequestMapping(value = "/close", method = RequestMethod.POST, produces = MediaTypes.HAL_JSON_VALUE)
    public ResponseEntity close(@Valid @RequestBody ConnectionCloseDTO dto) throws NoConnectionFoundException {
        log.debug("REST Request to close connection: " + dto.getId());
        connectionService.close(dto).orElseThrow(() -> new NoConnectionFoundException());
        return new ResponseEntity(HttpStatus.OK);
    }
}
