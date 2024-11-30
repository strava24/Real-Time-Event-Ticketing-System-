package com.ticketing_system.TicketingSystem.controller;

import com.ticketing_system.TicketingSystem.DTO.EventDTO;
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
    public ResponseEntity<List<EventDTO>> getAllEvents() {

        List<EventDTO> events = eventService.getAllEvents();

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
    public ResponseEntity<Integer> createEvent(@RequestBody EventDTO eventDTO) {

        Event event = eventService.convertToEventFromDTO(eventDTO);

        if (event != null) {
            int eventID = eventService.createEvent(event);

            return new ResponseEntity<>(eventID, HttpStatus.OK);
        }
        else
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);

    }

    @GetMapping("{id}")
    public ResponseEntity<EventDTO> getEventDTOById(@PathVariable int id) {

        EventDTO event = eventService.getEvenDTOByID(id);

        if (event == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } else {
            return new ResponseEntity<>(event, HttpStatus.OK);
        }

    }


}
