package com.zerotrust.oauth.mapper;

import com.zerotrust.oauth.model.User;
import com.zerotrust.oauth.model.dto.UserDetailsDTO;
import org.springframework.stereotype.Service;

import java.util.stream.Collectors;

@Service
public class UserMapper {

    public UserDetailsDTO userToUserDetailsDTO(User user) {
        UserDetailsDTO dto = new UserDetailsDTO();
        dto.setEmail(user.getEmail());
        dto.setExpired(user.isExpired());
        dto.setLocked(user.isLocked());
        dto.setCredentailsExpired(user.isCredentialsExpired());
        dto.setEnabled(user.isEnabled());
        dto.getRoles().addAll(user.getRoles().stream().map(x -> x.getName()).collect(Collectors.toSet()));

        return dto;
    }
}
