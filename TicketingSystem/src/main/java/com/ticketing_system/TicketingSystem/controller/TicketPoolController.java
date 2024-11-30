package com.ticketing_system.TicketingSystem.controller;

import com.ticketing_system.TicketingSystem.DTO.TicketPoolDTO;
import com.ticketing_system.TicketingSystem.model.Configuration;
import com.ticketing_system.TicketingSystem.service.TicketPoolService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/ticket-pool")
public class TicketPoolController {

    @Autowired
    private TicketPoolService ticketPoolService;

    @GetMapping("{eventID}")
    public ResponseEntity<List<TicketPoolDTO>> getAllTicketPools(@PathVariable int eventID) {

        List<TicketPoolDTO> pools = ticketPoolService.getAllTicketPools(eventID);

        if (pools == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } else {
            return new ResponseEntity<>(pools, HttpStatus.OK);
        }

    }

    /**
     * @param configuration - the automatic configuration from the user
     * @param eventID
     * @return
     */
    @PostMapping("/create/{eventID}")
    public ResponseEntity<Map<String, Integer>> createTicketPool(@RequestBody Configuration configuration, @PathVariable int eventID, @RequestParam String poolName, @RequestParam int ticketPrice) {

        Map<String, Integer> details = ticketPoolService.createTicketPool(configuration, eventID, poolName, ticketPrice);

        if (details != null) {
            return new ResponseEntity<>(details , HttpStatus.CREATED);
        } else
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);

    }

    @GetMapping("{poolID}/sell-ticket/{aiVendorId}")
    public synchronized ResponseEntity<String> addTicket(@PathVariable int aiVendorId, @PathVariable int poolID) {

        boolean soldTicket =  ticketPoolService.addTicket(aiVendorId, poolID);

        if (soldTicket) {
            return new ResponseEntity<>("Ticket added", HttpStatus.OK);
        } else {
            return new ResponseEntity<>("Ticket not added", HttpStatus.BAD_REQUEST);
        }

    }

    @GetMapping("{poolID}/buy-ticket/{customerID}")
    public synchronized ResponseEntity<String> removeTicket(@PathVariable int poolID, @PathVariable int customerID) {

        boolean boughtTicket = ticketPoolService.removeTicket(poolID, customerID);

        if (boughtTicket) {
            return new ResponseEntity<>("Ticket bought", HttpStatus.OK);
        } else {
            return new ResponseEntity<>("Ticket not bought", HttpStatus.BAD_REQUEST);
        }

    }




}
