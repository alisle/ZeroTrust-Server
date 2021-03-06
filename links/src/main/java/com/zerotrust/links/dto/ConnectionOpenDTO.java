package com.zerotrust.links.dto;


import com.fasterxml.jackson.annotation.JsonProperty;
import com.zerotrust.model.entity.Protocol;
import lombok.Data;

import java.time.Instant;
import java.util.UUID;

@Data
public class ConnectionOpenDTO {
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
