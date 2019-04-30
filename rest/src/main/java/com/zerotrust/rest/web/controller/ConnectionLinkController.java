package com.zerotrust.rest.web.controller;

import com.zerotrust.rest.exception.ConnectionLinkNotFoundException;
import com.zerotrust.rest.model.ConnectionLink;
import com.zerotrust.rest.service.ConnectionLinkService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.query.Param;
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;


@Slf4j
@RestController
@RequestMapping(value = "connection_links")
public class ConnectionLinkController {
    private final ConnectionLinkService connectionLinkService;

    public ConnectionLinkController(ConnectionLinkService connectionLinkService) {
        this.connectionLinkService = connectionLinkService;
    }

    @PreAuthorize("#oauth2.hasScope('read') and hasRole('connections_read')")
    @RequestMapping( value = "", method = RequestMethod.GET, produces = MediaTypes.HAL_JSON_VALUE)
    public ResponseEntity<Page<ConnectionLink>> all(Pageable pageable) {
        log.debug("REST request for all connection links");
        return new ResponseEntity<>(this.connectionLinkService.getPage(pageable), null, HttpStatus.OK);
    }


    @PreAuthorize("#oauth2.hasScope('read') and hasRole('connections_read')")
    @RequestMapping(value = "/{id}", method = RequestMethod.GET, produces = MediaTypes.HAL_JSON_VALUE)
    public ResponseEntity<ConnectionLink> get(@PathVariable("id") UUID id) throws ConnectionLinkNotFoundException {
        log.debug("REST Request for connection link " + id);
        ConnectionLink link = connectionLinkService.get(id).orElseThrow(() -> new ConnectionLinkNotFoundException());
        return new ResponseEntity<>(link, null, HttpStatus.OK);
    }

    @PreAuthorize("#oauth2.hasScope('read') and hasRole('connections_read')")
    @RequestMapping(value = "/search/agent-connections", method = RequestMethod.GET, produces = MediaTypes.HAL_JSON_VALUE)
    public ResponseEntity<Page<ConnectionLink>> getAgentLinks(Pageable pageable, @RequestParam("agent_id") UUID id)  {
        log.debug("REST Request for connection link " + id);
        return new ResponseEntity<>(connectionLinkService.getByAgentConnections(pageable, id), null, HttpStatus.OK);
    }

    @PreAuthorize("#oauth2.hasScope('read') and hasRole('connections_read')")
    @RequestMapping(value = "/search/source-destination-agent-id", method = RequestMethod.GET, produces = MediaTypes.HAL_JSON_VALUE)
    public ResponseEntity<Page<ConnectionLink>> getLinksBetweenSourceAgentAndDestinationAgent(Pageable pageable,@RequestParam("source_agent_id") UUID source, @RequestParam("destination_agent_id") UUID destination) {
        log.debug("REST Request for connection link " + source + ":" + destination);
        return new ResponseEntity<>(connectionLinkService.getLinksBetweenSourceAgentAndDestinationAgent(pageable, source, destination), null, HttpStatus.OK);
    }

    @PreAuthorize("#oauth2.hasScope('read') and hasRole('connections_read')")
    @RequestMapping(value = "/search/source-agent-id-destination-ip", method = RequestMethod.GET, produces = MediaTypes.HAL_JSON_VALUE)
    public ResponseEntity<Page<ConnectionLink>> getLinksBetweenSourceAgentAndDestinationIP(Pageable pageable,@RequestParam("source_agent_id") UUID source, @RequestParam("destination_address") String destination) {
        log.debug("REST Request for connection link " + source + ":" + destination);
        return new ResponseEntity<>(connectionLinkService.getLinksBetweenSourceAgentAndDestinationIP(pageable, source, destination), null, HttpStatus.OK);
    }

    @PreAuthorize("#oauth2.hasScope('read') and hasRole('connections_read')")
    @RequestMapping(value = "/search/source-ip-destination-agent-id", method = RequestMethod.GET, produces = MediaTypes.HAL_JSON_VALUE)
    public ResponseEntity<Page<ConnectionLink>> getLinksBetweenSourceIPAndDestinationAgent(Pageable pageable,  @RequestParam("source_address") String source, @RequestParam("destination_agent_id") UUID destination) {
        log.debug("REST Request for connection link " + source + ":" + destination);
        return new ResponseEntity<>(connectionLinkService.getLinksbetweenSourceIPAndDestinationAgent(pageable, source, destination), null, HttpStatus.OK);
    }

    @PreAuthorize("#oauth2.hasScope('read') and hasRole('connections_read')")
    @RequestMapping(value = "/search/active-source-agent-id", method = RequestMethod.GET, produces = MediaTypes.HAL_JSON_VALUE)
    public ResponseEntity<Page<ConnectionLink>> getActiveLinksFromSourceAgent(Pageable pageable, @RequestParam("source_agent_id") UUID source) {
        log.debug("REST Request for active connection link " + source);
        return new ResponseEntity<>(connectionLinkService.getActiveLinksFromSourceAgent(pageable, source), null, HttpStatus.OK);
    }

    @PreAuthorize("#oauth2.hasScope('read') and hasRole('connections_read')")
    @RequestMapping(value = "/search/active-destination-agent-id", method = RequestMethod.GET, produces = MediaTypes.HAL_JSON_VALUE)
    public ResponseEntity<Page<ConnectionLink>> getActiveLinksFromDestinationAgent(Pageable pageable, @RequestParam("destination_agent_id") UUID destination) {
        log.debug("REST Request for active connection link " + destination);
        return new ResponseEntity<>(connectionLinkService.getActiveLinksFromDestinationAgent(pageable, destination), null, HttpStatus.OK);
    }

    @PreAuthorize("#oauth2.hasScope('read') and hasRole('connections_read')")
    @RequestMapping(value = "/search/active", method = RequestMethod.GET, produces = MediaTypes.HAL_JSON_VALUE)
    public ResponseEntity<Page<ConnectionLink>> getAllActiveLinks(Pageable pageable) {
        log.debug("REST Request for active connection links");
        return new ResponseEntity<>(connectionLinkService.getAllActiveLinks(pageable), null, HttpStatus.OK);
    }

    @PreAuthorize("#oauth2.hasScope('read') and hasRole('connections_read')")
    @RequestMapping(value = "/search/total-connection-links", method = RequestMethod.GET, produces = MediaTypes.HAL_JSON_VALUE)
    public ResponseEntity<Long> getTotalLinks(Pageable pageable) {
        log.debug("REST Request for active connection links");
        return new ResponseEntity<>(connectionLinkService.getTotalLinks(), null, HttpStatus.OK);
    }

    @PreAuthorize("#oauth2.hasScope('read') and hasRole('connections_read')")
    @RequestMapping(value = "/search/total-alive-connection-links", method = RequestMethod.GET, produces = MediaTypes.HAL_JSON_VALUE)
    public ResponseEntity<Long> getTotalActiveLinks(Pageable pageable) {
        log.debug("REST Request for active connection links");
        return new ResponseEntity<>(connectionLinkService.getTotalActiveLinks(), null, HttpStatus.OK);
    }

}
