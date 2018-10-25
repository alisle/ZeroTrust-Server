package com.notrust.server.model.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.time.Instant;
import java.util.UUID;

@Data
public class ConnectionOpenDTO {
    @JsonProperty("uuid")
    private UUID id;

    @JsonProperty("hash")
    private long hash;

    @JsonProperty("timestamp")
    private Instant timestamp;

    @JsonProperty("protocol")
    private String protocol;

    @JsonProperty("source")
    private String source;

    @JsonProperty("destination")
    private String destination;

    @JsonProperty("source_port")
    private int sourcePort;

    @JsonProperty("destination_port")
    private int destinationPort;

    @JsonProperty("username")
    private String username;

    @JsonProperty("uid")
    private int uid;

    @JsonProperty("program_details")
    private ProgramDTO program;
}
