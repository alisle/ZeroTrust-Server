package com.zerotrust.rest.model.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.zerotrust.rest.model.Protocol;
import lombok.Data;

import java.time.Instant;
import java.util.UUID;

@Data
public class ConnectionCloseDTO {

    @JsonProperty("uuid")
    private UUID id;

    @JsonProperty("agent")
    private UUID agent;

    @JsonProperty("hash")
    private long hash;

    @JsonProperty("timestamp")
    private Instant timestamp;

    @JsonProperty("protocol")
    private Protocol protocol;

    @JsonProperty("sourceString")
    private String source;

    @JsonProperty("destinationString")
    private String destination;

    @JsonProperty("source_port")
    private int sourcePort;

    @JsonProperty("destination_port")
    private int destinationPort;
}
