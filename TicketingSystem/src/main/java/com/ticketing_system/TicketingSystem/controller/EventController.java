package com.ticketing_system.TicketingSystem.controller;

import com.ticketing_system.TicketingSystem.model.Event;
import com.ticketing_system.TicketingSystem.model.Vendor;
import com.ticketing_system.TicketingSystem.service.EventService;
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
    private VendorService vendorService;

    @GetMapping
    public ResponseEntity<List<Event>> getAllEvents() {

        List<Event> events = eventService.getAllEvents();

        if (events.isEmpty())
            return new ResponseEntity<>(HttpStatus.NOT_IMPLEMENTED);
        else
            return new ResponseEntity<>(events, HttpStatus.OK);

    }

    /**
     * This method is to create an event for the vendor
     * @param event - the JSON body with the attribute values
     */
    @PostMapping("/create")
    public ResponseEntity<String> createEvent(@RequestBody Event event) {

        Vendor vendor = vendorService.getVendorByID(event.getVendor().getVendorID());

        if (vendor != null) {
            eventService.createEvent(event);
            return new ResponseEntity<>("Event created sucessfully!", HttpStatus.OK);
        }
        else
            return new ResponseEntity<>("There is no such Vendor!", HttpStatus.NOT_FOUND);

    }

    @GetMapping("/{eventID}/available-tickets")
    public ResponseEntity<Integer> getAvailableTicketsByID(@PathVariable int eventID) {

        Event event = eventService.getEventByID(eventID);

        if (event != null)
            return new ResponseEntity<>(eventService.getAvailableTicketsByID(eventID), HttpStatus.OK);
        else
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);

    }



}
