package com.zerotrust.oauth.service;

import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public interface ConfigService {
    UUID getNodeUUID();
    boolean isFirstRun();
}
