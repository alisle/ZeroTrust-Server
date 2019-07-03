package com.zerotrust.oauth.model.dto;

import lombok.Data;
import org.springframework.hateoas.core.Relation;

import java.util.HashSet;
import java.util.Set;

@Data
@Relation(value = "user", collectionRelation = "users")
public class UserDetailsDTO {
    private String email;
    private boolean expired;
    private boolean locked;
    private boolean credentailsExpired;
    private boolean enabled;
    private Set<String> roles = new HashSet<>();
}
