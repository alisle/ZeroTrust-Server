package com.zerotrust.links.dto;


import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class UpdateInterfacesDTO {
    @JsonProperty("interfaces")
    private String[] interfaces;
}
