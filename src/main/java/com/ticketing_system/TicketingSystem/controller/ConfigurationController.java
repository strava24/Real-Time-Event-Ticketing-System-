package com.ticketing_system.TicketingSystem.controller;

import com.ticketing_system.TicketingSystem.model.Configuration;
import com.ticketing_system.TicketingSystem.service.ConfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/config")
public class ConfigurationController {

    @Autowired
    private ConfigService configService;

    @GetMapping
    public List<Configuration> getConfigurations() {
        return configService.getConfigurations();
    }

    @PostMapping("/configuration")
    public void addConfiguration(@RequestBody Configuration configuration) {
        configService.addConfiguration(configuration);
    }


}
