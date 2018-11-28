package com.notrust.server.model.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.UUID;

@Data
public class AgentOnlineDTO {
    @JsonProperty("uuid")
    private UUID id;

    @JsonProperty("name")
    private String name;
}
