package com.ticketing_system.TicketingSystem.controller;

import com.ticketing_system.TicketingSystem.model.Configuration;
import com.ticketing_system.TicketingSystem.service.ConfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/config")
@CrossOrigin(origins = "http://localhost:4209")
public class ConfigurationController {

    @Autowired
    private ConfigService configService;

    @GetMapping
    public List<Configuration> getConfigurations() {
        return configService.getConfigurations();
    }

    @PostMapping("/configuration")
    public ResponseEntity<String> addConfiguration(@RequestBody Configuration configuration) {
        // Making sure that maxTicketCapacity is greater than or equal to totalTickets
        if (configuration.getMaxTicketCapacity() > configuration.getTotalTickets()) {
            return new ResponseEntity<>("Invalid input, total number of tickets should be greater than the maximum amount of tickets allowed for a ticket pool", HttpStatus.BAD_REQUEST);
        } else {
            configService.addConfiguration(configuration);
            return new ResponseEntity<>("Added configuration to start simulation!", HttpStatus.OK);
        }
    }

    @GetMapping("/configuration/{id}")
    public Configuration getConfiguration(@PathVariable int id) {
        return configService.getConfiguration(id);
    }

//    @PutMapping("/configuration")
//    public ResponseEntity<String> updateConfiguration(@RequestBody Configuration configuration) {
//        Configuration existingConfiguration = configService.updateConfiguration(configuration);
//
//        if (existingConfiguration == null) {
//            return new ResponseEntity<>("Invalid Action", HttpStatus.NOT_FOUND);
//        } else {
//            return new ResponseEntity<>("Updated Configuration!", HttpStatus.OK);
//        }
//    }

    @DeleteMapping("/configuration/{id}")
    public ResponseEntity<String> deleteConfiguration(@PathVariable int id) {
        Configuration configuration = configService.getConfiguration(id);

        if (configuration == null) {
            return new ResponseEntity<>("Invalid Action", HttpStatus.NOT_FOUND);
        } else {
            configService.deleteConfiguration(id);
            return new ResponseEntity<>("Deleted Configuration!", HttpStatus.OK);
        }
    }


}
