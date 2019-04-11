package com.zerotrust.oauth.service.impl;

import com.zerotrust.oauth.model.Config;
import com.zerotrust.oauth.repository.ConfigRepository;
import com.zerotrust.oauth.service.ConfigService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Slf4j
@Service
public class ConfigServiceImpl implements ConfigService {
    private final ConfigRepository repository;
    private Config config;
    private boolean firstRun = false;


    public ConfigServiceImpl(ConfigRepository repository) {
        this.repository = repository;

        List<Config> configs = this.repository.findAll();
        if(configs.size() > 1) {
            log.error("multiple configs found, not a good sign!!");
        }  else if(configs.size() == 0) {
            log.error("no config found!");
        } else {
            this.config = configs.get(0);
            this.firstRun = config.isFirstRun();
            this.config.setFirstRun(false);
            this.config = repository.save(this.config);
        }
    }


    @Override
    public UUID getNodeUUID() {
        return config.getNode();
    }

    @Override
    public boolean isFirstRun() {
        return this.firstRun;
    }
}
