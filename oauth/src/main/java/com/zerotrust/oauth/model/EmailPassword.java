package com.zerotrust.oauth.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EmailPassword {
    private String email;

    @JsonProperty("password")
    private String unencryptedPassword;
}
