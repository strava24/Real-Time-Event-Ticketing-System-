package com.ticketing_system.TicketingSystem.service;

import com.ticketing_system.TicketingSystem.model.Configuration;
import com.ticketing_system.TicketingSystem.repository.ConfigurationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ConfigService {

    @Autowired
    ConfigurationRepository configRepo;

    public List<Configuration> getConfigurations() {
        return configRepo.findAll();
    }

    public void addConfiguration(Configuration configuration) {
        configRepo.save(configuration);
    }

//    public Configuration updateConfiguration(Configuration configuration) {
//        return configRepo.save(configuration);
//    }

    public void deleteConfiguration(int id) {
        configRepo.deleteById(id);
    }

    public Configuration getConfiguration(int id) {
        return configRepo.findById(id).orElse(null); // Returning null if the configuration does not exist
    }
}
