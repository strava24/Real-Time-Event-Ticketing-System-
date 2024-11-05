package com.ticketing_system.TicketingSystem.controller;

import com.ticketing_system.TicketingSystem.exception.VendorNotFoundExpecption;
import com.ticketing_system.TicketingSystem.model.Event;
import com.ticketing_system.TicketingSystem.model.Vendor;
import com.ticketing_system.TicketingSystem.service.EventService;
import com.ticketing_system.TicketingSystem.service.TicketService;
import com.ticketing_system.TicketingSystem.service.VendorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/api/events")
@RestController
public class EventController {

    @Autowired
    private EventService eventService;

    @Autowired
    private TicketService ticketService;

    @Autowired
    private VendorService vendorService;

    @GetMapping
    public ResponseEntity<List<Event>> getAllEvents() {

        List<Event> events = eventService.getAllEvents();

        if (events.isEmpty())
            return new ResponseEntity<>(HttpStatus.NOT_IMPLEMENTED);
        else
            return new ResponseEntity<>(events, HttpStatus.OK);

    }

    @PostMapping("/create")
    public void createEvent(@RequestBody Event event) throws VendorNotFoundExpecption {

        Vendor vendor = vendorService.getVendorByID(event.getVendor().getVendorID());

        if (vendor != null)
            eventService.createEvent(event);
        else
            throw new VendorNotFoundExpecption("There is no such Vendor!");
    }



}
