package com.zerotrust.oauth.model.dto;

import lombok.Data;

@Data
public class NewUserDTO {
    private String email;
    private String roles[];
}
