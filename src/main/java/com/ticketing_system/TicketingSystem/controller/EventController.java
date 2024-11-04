package com.ticketing_system.TicketingSystem.controller;

import com.ticketing_system.TicketingSystem.model.Event;
import com.ticketing_system.TicketingSystem.service.EventService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequestMapping("/api/events")
@RestController
public class EventController {

    private EventService eventService;

    @GetMapping
    public ResponseEntity<List<Event>> getAllEvents() {

        List<Event> events = eventService.getAllEvents();

        if (events.isEmpty())
            return new ResponseEntity<>(HttpStatus.NOT_IMPLEMENTED);
        else
            return new ResponseEntity<>(events, HttpStatus.OK);

    }


}
