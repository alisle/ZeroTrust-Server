package com.zerotrust.oauth.service;

import com.zerotrust.oauth.AuthorizationServerApplication;
import com.zerotrust.oauth.model.Config;
import com.zerotrust.oauth.repository.ConfigRepository;
import com.zerotrust.oauth.service.ConfigService;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import java.util.List;
import java.util.UUID;

@RunWith(SpringRunner.class)
@WebAppConfiguration
@SpringBootTest(classes = AuthorizationServerApplication.class)
@ActiveProfiles("dev")
public class ConfigServiceTest {

    @Autowired
    private ConfigService configService;

    @Autowired
    private ConfigRepository configRepository;

    @Test
    public void testGetUUID() throws Exception {
        UUID uuid = configService.getNodeUUID();
        Assert.assertNotNull(uuid);
    }

    @Test
    public void testIsFirstRun() throws Exception {
        Assert.assertTrue(configService.isFirstRun());
        Assert.assertTrue(configService.isFirstRun());
    }

    @Test
    public void testIsNotFirstRun() throws Exception {
        List<Config> configs = configRepository.findAll();
        Assert.assertFalse(configs.get(0).isFirstRun());
    }

}
