package com.zerotrust.rest.dto;

import lombok.Data;

@Data
public class NetworkDTO {
    private String name;
    private String description;
    private String network;
    private String mask;
}
