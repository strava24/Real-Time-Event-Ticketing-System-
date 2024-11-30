package com.ticketing_system.TicketingSystem.controller;

import com.ticketing_system.TicketingSystem.DTO.EventDTO;
import com.ticketing_system.TicketingSystem.model.Event;
import com.ticketing_system.TicketingSystem.service.EventService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/api/events")
@RestController
@CrossOrigin(origins = "http://localhost:4209")
public class EventController {

    @Autowired
    private EventService eventService;

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

    @GetMapping("/{id}")
    public ResponseEntity<EventDTO> getEventDTOById(@PathVariable int id) {

        EventDTO event = eventService.getEvenDTOByID(id);

        if (event == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } else {
            return new ResponseEntity<>(event, HttpStatus.OK);
        }

    }

    @GetMapping("vendor/{vendorID}")
    public ResponseEntity<List<EventDTO>> getAllEventsByVendorID(@PathVariable int vendorID) {
        List<EventDTO> events = eventService.getAllEventsByVendorID(vendorID);

        if (events.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } else {
            return new ResponseEntity<>(events, HttpStatus.OK);
        }
    }

    @PutMapping("/update")
    public ResponseEntity<String> updateEvent(@RequestBody EventDTO eventDTO) {

        Event event = eventService.getEventByID(eventDTO.getEventID());

        if (event != null) {
            event = eventService.convertToEventFromDTO(eventDTO);
            event.setEventID(eventDTO.getEventID());
            eventService.updateEvent(event);
            return new ResponseEntity<>("Success", HttpStatus.OK);
        } else
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @DeleteMapping("/delete/{eventID}")
    public ResponseEntity<String> deleteEvent(@PathVariable int eventID) {

        Event event =  eventService.getEventByID(eventID);

        if (event != null) {
            eventService.deleteEvent(event);
            return new ResponseEntity<>("Success", HttpStatus.OK);
        } else
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);

    }


}
