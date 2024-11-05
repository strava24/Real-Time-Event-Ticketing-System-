package com.ticketing_system.TicketingSystem.controller;

import com.ticketing_system.TicketingSystem.exception.EventNotFoundException;
import com.ticketing_system.TicketingSystem.model.Event;
import com.ticketing_system.TicketingSystem.model.TicketPool;
import com.ticketing_system.TicketingSystem.service.EventService;
import com.ticketing_system.TicketingSystem.service.TicketService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/ticket-pool")
public class TicketPoolController {

    @Autowired
    private EventService eventService;

    @Autowired
    private TicketService ticketService;

    @PostMapping("/create-pool")
    public void createTicketPool(@RequestBody TicketPool ticketPool) throws EventNotFoundException {

        Event event = eventService.getEventByID(ticketPool.getEvent().getEventID());

        if (event != null)
            ticketPool.getEvent().addTicketPool(ticketPool, ticketService);
        else
            throw new EventNotFoundException("Event not found");

    }

    //    /**
//     * To create an event we should get the ticket pool details from the user.
//     * Since ticket pool is connected to the event, we can return the event after an event is created
//     * @param ticketPool a ticket pool is necessary when creating an event
//     * @return the event we created
//     */
//    @PostMapping("/create")
//    public Event createEvent(@RequestBody TicketPool ticketPool) {
//        ticketPool.getEvent().addTicketPool(ticketPool, ticketService); // Adding a ticket pool and starting production
//        return ticketPool.getEvent(); // Return or save as needed
//    }

}
