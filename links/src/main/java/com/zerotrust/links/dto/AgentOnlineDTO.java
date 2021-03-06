package com.zerotrust.links.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.UUID;

@Data
public class AgentOnlineDTO {

    @JsonProperty("uuid")
    private UUID id;

    @JsonProperty("name")
    private String name;

    @JsonProperty("interfaces")
    private String[] interfaces;

}
