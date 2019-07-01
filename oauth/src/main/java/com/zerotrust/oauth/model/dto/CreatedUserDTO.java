package com.zerotrust.oauth.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CreatedUserDTO {
    private String email;
    private String password;
}
