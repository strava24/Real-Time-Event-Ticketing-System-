package com.ticketing_system.TicketingSystem.controller;

import com.ticketing_system.TicketingSystem.model.Configuration;
import com.ticketing_system.TicketingSystem.service.EventService;
import com.ticketing_system.TicketingSystem.service.TicketPoolService;
import com.ticketing_system.TicketingSystem.service.VendorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/ticket-pool")
public class TicketPoolController {

    @Autowired
    private EventService eventService;

    @Autowired
    private VendorService vendorService;

    @Autowired
    private TicketPoolService ticketPoolService;

//    @PostMapping("/create-pool")
//    public ResponseEntity<String> createTicketPool(@RequestBody DummyTicketPool dummyTicketPool) {
//        Event event = eventService.getEventByID(dummyTicketPool.getEvent().getEventID());
//
//        if (event != null) {
//
//            // Checking if the current pool can be created
//            int ticketCount =  eventService.getAvailableTicketsByID(event.getEventID());
//
//            if (event.getTotalTickets() - ticketCount >= dummyTicketPool.getTotalTickets()) {
//                ticketPoolService.createTicketPool(dummyTicketPool);
//                dummyTicketPool.getEvent().addTicketPool(dummyTicketPool, vendorService);
//                return new ResponseEntity<>("Successfully created Ticket Pool!", HttpStatus.OK);
//            } else
//                return new ResponseEntity<>("This ticket pool cannot be created, as it is exceeding the capacity of the event", HttpStatus.NOT_FOUND);
//
//        }
//        else
//            return new ResponseEntity<>("There is no such Event!", HttpStatus.NOT_FOUND) ;
//    }

    /**
     * This endpoint is called to create a ticket pool under A.I. Inc. vendor
     * @param configuration - the automatic configuration from the user
     * @return the id of A.I vendor
     */
    @PostMapping("/create")
    public ResponseEntity<Integer> createTicketPool(@RequestBody Configuration configuration) {

        int aiVendorID = ticketPoolService.createTicketPool(configuration);
        return new ResponseEntity<>(aiVendorID , HttpStatus.CREATED);

    }


}
