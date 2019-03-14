package com.zerotrust.server.model.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class UpdateInterfacesDTO {
    @JsonProperty("interfaces")
    private String[] interfaces;
}
