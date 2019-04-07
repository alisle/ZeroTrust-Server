package com.zerotrust.rest.model;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.UUID;

@Data
@AllArgsConstructor
public class AgentCount {
    private String agent;
    private UUID uuid;
    private long count;
}
