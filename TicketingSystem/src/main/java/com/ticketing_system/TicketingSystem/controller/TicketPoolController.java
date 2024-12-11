package com.ticketing_system.TicketingSystem.controller;

import com.ticketing_system.TicketingSystem.DTO.BuyTicketRequest;
import com.ticketing_system.TicketingSystem.DTO.SellTicketRequest;
import com.ticketing_system.TicketingSystem.DTO.TicketPoolDTO;
import com.ticketing_system.TicketingSystem.service.TicketPoolService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/ticket-pool")
@CrossOrigin(origins = "http://localhost:4209")
public class TicketPoolController {

    @Autowired
    private TicketPoolService ticketPoolService;

    private final SimpMessagingTemplate messagingTemplate;

    public TicketPoolController(TicketPoolService ticketPoolService, SimpMessagingTemplate messagingTemplate) {
        this.ticketPoolService = ticketPoolService;
        this.messagingTemplate = messagingTemplate;
    }

    /**
     * End point to get all the ticket pools of an event
     * @param eventID - Event ID
     * @return - A list of Ticket Pools along with a status code
     */
    @GetMapping("/{eventID}")
    public ResponseEntity<List<TicketPoolDTO>> getAllTicketPools(@PathVariable int eventID) {

        List<TicketPoolDTO> pools = ticketPoolService.getAllTicketPools(eventID);

        if (pools == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } else {
            return new ResponseEntity<>(pools, HttpStatus.OK);
        }

    }

    /**
     * End poin to create a Ticket Pool under a event ID
     * @param eventID - Event ID
     * @param poolName - Name for the pool
     * @param ticketPrice - Price of a ticket on the pool
     * @param maxTicketCapacity - Capacity / size of the pool
     * @param totalTickets - Total tickets
     * @return
     */
    @PostMapping("/create/{eventID}")
    public ResponseEntity<Map<String, Integer>> createTicketPool(@PathVariable int eventID, @RequestParam String poolName, @RequestParam int ticketPrice, @RequestParam int maxTicketCapacity, @RequestParam int totalTickets) {

        Map<String, Integer> details = ticketPoolService.createTicketPool(maxTicketCapacity, totalTickets, eventID, poolName, ticketPrice);

        if (details != null) {
            return new ResponseEntity<>(details , HttpStatus.CREATED);
        } else
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);

    }

    /**
     * End point to add a ticket to the pool
     * @param vendorId - vendor ID
     * @param poolID - Pool ID to add the ticket
     * @return - Message to address the action along with a status code
     */
    @GetMapping("{poolID}/sell-ticket/{vendorId}")
    public synchronized ResponseEntity<String> addTicket(@PathVariable int vendorId, @PathVariable int poolID) {

        boolean soldTicket =  ticketPoolService.addTicket(vendorId, poolID);

        if (soldTicket) {
            messagingTemplate.convertAndSend("/topic/tickets", "Ticket sold successfully!");
            return new ResponseEntity<>("Ticket added", HttpStatus.OK);

        } else {
            return new ResponseEntity<>("Ticket not added", HttpStatus.BAD_REQUEST);
        }

    }

    /**
     * End point to remove a ticket from a pool
     * @param poolID - pool ID of the pool
     * @param customerID - customer ID of the person trying to but a ticket
     * @return - Message to address the action along with a status code
     */
    @GetMapping("{poolID}/buy-ticket/{customerID}")
    public synchronized ResponseEntity<String> removeTicket(@PathVariable int poolID, @PathVariable int customerID) {

        boolean boughtTicket = ticketPoolService.removeTicket(poolID, customerID);

        if (boughtTicket) {
            messagingTemplate.convertAndSend("/topic/tickets", "Ticket bought successfully by " + customerID);
            return new ResponseEntity<>("Ticket bought", HttpStatus.OK);
        } else {
            return new ResponseEntity<>("Ticket not bought", HttpStatus.BAD_REQUEST);
        }

    }

    @MessageMapping("/sell-ticket")
    public void addTicket(SellTicketRequest request) {
        System.out.println("In the add Ticket method");
        boolean soldTicket = ticketPoolService.addTicket(request.getVendorId(), request.getPoolId());

        // Notify all subscribers of updated pool details
        if (soldTicket) {
            messagingTemplate.convertAndSend("/topic/tickets", "Ticket sold successfully!");
        } else {
            messagingTemplate.convertAndSend("/topic/tickets", "Failed to sell ticket.");
        }
    }

    @MessageMapping("/buy-ticket") // Incoming WebSocket message to "/app/buy-ticket"
    public void removeTicket(BuyTicketRequest request) {

        System.out.println("In the remove Ticket method");

        boolean boughtTicket = ticketPoolService.removeTicket(request.getPoolId(), request.getCustomerId());

        // Notify all subscribers of updated pool details
        if (boughtTicket) {
            messagingTemplate.convertAndSend("/topic/tickets", "Ticket bought successfully!");
        } else {
            messagingTemplate.convertAndSend("/topic/tickets", "Failed to buy ticket.");
        }

    }

}
