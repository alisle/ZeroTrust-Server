package com.zerotrust.model.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UserCount {
    @JsonProperty("user")
    String user;

    @JsonProperty("count")
    long count;
}
