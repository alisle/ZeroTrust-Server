package com.zerotrust.rest.web.controller;

import com.zerotrust.rest.exception.InvalidCredentialsException;
import com.zerotrust.rest.dto.LoginRequestDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.*;
import org.springframework.http.client.support.BasicAuthenticationInterceptor;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import javax.annotation.PostConstruct;
import javax.validation.Valid;

@RestController
@RequestMapping(value = "/api/auth")
@Slf4j
public class LoginController {
    @Value("${com.zerotrust.oauth.server}")
    private String server;

    @Value("${com.zerotrust.oauth.server-port}")
    private int port;


    @Value("${com.zeortrust.oauth.rest-client-id}")
    private String rest_client_id;

    @Value("${com.zerotrust.oauth.rest-client-secret}")
    private String rest_client_secret;

    private final RestTemplate restTemplate;

    public LoginController(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @PostConstruct
    public void setup() {
        restTemplate.getInterceptors().add(new BasicAuthenticationInterceptor(rest_client_id, rest_client_secret));
    }


    private String postTokenRequest(String username, String password)  throws InvalidCredentialsException {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

            MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
            map.add("grant_type", "password");
            map.add("username", username);
            map.add("password", password);

            HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(map, headers);
            ResponseEntity<String> responseEntity = restTemplate.postForEntity(
                    "http://" + server + ":" + port + "/oauth/token",
                    request,
                    String.class);

            return responseEntity.getBody();
        } catch (Exception ex) {
            throw new InvalidCredentialsException();
        }

    }

    @RequestMapping( value = "", method = RequestMethod.POST, produces = MediaTypes.HAL_JSON_VALUE)
    public ResponseEntity<String> login( @Valid @RequestBody LoginRequestDTO dto) throws InvalidCredentialsException {
        String email = dto.getEmail();
        String password = dto.getPassword();
        String response = postTokenRequest(email, password);

        return new ResponseEntity<>(response, null, HttpStatus.OK);
    }
}
