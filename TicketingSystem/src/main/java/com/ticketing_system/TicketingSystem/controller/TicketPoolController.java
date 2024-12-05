package com.ticketing_system.TicketingSystem.controller;

import com.ticketing_system.TicketingSystem.DTO.TicketPoolDTO;
import com.ticketing_system.TicketingSystem.service.TicketPoolService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/ticket-pool")
@CrossOrigin(origins = "http://localhost:4209")
public class TicketPoolController {

    @Autowired
    private TicketPoolService ticketPoolService;

    @GetMapping("/{eventID}")
    public ResponseEntity<List<TicketPoolDTO>> getAllTicketPools(@PathVariable int eventID) {

        List<TicketPoolDTO> pools = ticketPoolService.getAllTicketPools(eventID);

        if (pools == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } else {
            return new ResponseEntity<>(pools, HttpStatus.OK);
        }

    }

    @PostMapping("/create/{eventID}")
    public ResponseEntity<Map<String, Integer>> createTicketPool(@PathVariable int eventID, @RequestParam String poolName, @RequestParam int ticketPrice, @RequestParam int maxTicketCapacity, @RequestParam int totalTickets) {

        Map<String, Integer> details = ticketPoolService.createTicketPool(maxTicketCapacity, totalTickets, eventID, poolName, ticketPrice);

        if (details != null) {
            return new ResponseEntity<>(details , HttpStatus.CREATED);
        } else
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);

    }

    @GetMapping("{poolID}/sell-ticket/{vendorId}")
    public synchronized ResponseEntity<String> addTicket(@PathVariable int vendorId, @PathVariable int poolID) {

        boolean soldTicket =  ticketPoolService.addTicket(vendorId, poolID);

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
