package com.example.inventory_service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@RestController
@RefreshScope
public class ConfigController {

    @Autowired
    private InventoryConfig inventoryConfig;

    @Value("${spring.profiles.active:default}")
    private String activeProfile;

    @Value("${spring.cloud.config.uri:http://localhost:8888}")
    private String configServerUri;

    @GetMapping("/api/inventory/config")
    public Map<String, Object> getConfig() {
        return Map.of(
            "profile", activeProfile,
            "maxStock", inventoryConfig.getMaxStock(),
            "replenishThreshold", inventoryConfig.getReplenishThreshold()
        );
    }

    @GetMapping("/api/inventory/health")
    public Map<String, String> getHealth() {
        String configServerStatus = "disconnected";
        try {
            RestTemplate restTemplate = new RestTemplate();
            restTemplate.getForObject(configServerUri + "/actuator/health", String.class);
            configServerStatus = "connected";
        } catch (Exception e) {
            configServerStatus = "disconnected";
        }
        return Map.of(
            "status", "UP",
            "configServer", configServerStatus
        );
    }
}
