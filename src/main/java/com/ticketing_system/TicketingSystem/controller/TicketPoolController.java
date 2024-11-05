package com.ticketing_system.TicketingSystem.controller;

import com.ticketing_system.TicketingSystem.exception.EventNotFoundException;
import com.ticketing_system.TicketingSystem.model.Event;
import com.ticketing_system.TicketingSystem.model.TicketPool;
import com.ticketing_system.TicketingSystem.service.EventService;
import com.ticketing_system.TicketingSystem.service.TicketPoolService;
import com.ticketing_system.TicketingSystem.service.TicketService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/ticket-pool")
public class TicketPoolController {

    @Autowired
    private EventService eventService;

    @Autowired
    private TicketService ticketService;

    @Autowired
    private TicketPoolService ticketPoolService;

    @PostMapping("/create-pool")
    public ResponseEntity<String> createTicketPool(@RequestBody TicketPool ticketPool) {
        Event event = eventService.getEventByID(ticketPool.getEvent().getEventID());

        if (event != null) {

            // Checking if the current pool can be created
            int ticketCount =  eventService.getAvailableTicketsByID(event.getEventID());

            if (event.getMaxTicketCapacity() - ticketCount >= ticketPool.getTotalTickets()) {
                ticketPoolService.createTicketPool(ticketPool);
                ticketPool.getEvent().addTicketPool(ticketPool, ticketService);
                return new ResponseEntity<>("Successfully created Ticket Pool!", HttpStatus.OK);
            } else
                return new ResponseEntity<>("This ticket pool cannot be created, as it is exceeding the capacity of the event", HttpStatus.NOT_FOUND);

        }
        else
            return new ResponseEntity<>("There is no such Event!", HttpStatus.NOT_FOUND) ;
    }


}
