package com.zerotrust.server.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ProcessCount {
    @JsonProperty("process")
    String process;

    @JsonProperty("count")
    long count;
}
