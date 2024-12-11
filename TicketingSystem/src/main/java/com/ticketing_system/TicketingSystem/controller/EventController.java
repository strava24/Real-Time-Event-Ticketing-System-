package com.ticketing_system.TicketingSystem.controller;

import com.ticketing_system.TicketingSystem.DTO.EventDTO;
import com.ticketing_system.TicketingSystem.model.Event;
import com.ticketing_system.TicketingSystem.service.EventService;
import com.ticketing_system.TicketingSystem.service.VendorService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RequestMapping("/api/events")
@RestController
@CrossOrigin(origins = "http://localhost:4209")
public class EventController {

    private static final Logger logger = LogManager.getLogger(EventController.class);

    @Autowired
    private EventService eventService;
    @Autowired
    private VendorService vendorService;
    @Autowired
    private Event event;

    @GetMapping
    public ResponseEntity<List<EventDTO>> getAllEvents() {

        List<EventDTO> events = eventService.getAllEvents();

        if (events.isEmpty())
            return new ResponseEntity<>(HttpStatus.NOT_IMPLEMENTED);
        else
            return new ResponseEntity<>(events, HttpStatus.OK);

    }

    /**
     * End point is to create an event for the vendor
     * @param eventDTO - Event Data Transfer Object
     * @return - Event ID of the created event along with a status code
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

    /**
     * End point to register an event with image details, which will be rendered on the UI
     * @param eventDTO - Event Data Transfer Object
     * @param imageFile - Image file
     * @return - returns registered event with a status code if succeeded
     */
    @PostMapping("create/image")
    public ResponseEntity<?> createEventWithImage(@RequestPart EventDTO eventDTO, @RequestPart MultipartFile imageFile) {

        try {

            Event event = this.eventService
                    .createEvent(eventDTO, imageFile);

            EventDTO event1 = eventService.getEvenDTOByID(event.getEventID());

            if (event1 != null) {
                logger.info("Retrieved event with ID: E{}", event.getEventID());
                return new ResponseEntity<>(event1, HttpStatus.OK);
            } else {
                return new ResponseEntity<>(HttpStatus.NOT_ACCEPTABLE);
            }

        } catch (Exception e) {
            logger.error(e);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    /**
     * End point to update the event on a DB with an image
     * @param eventDTO - Event Data Transfer Object
     * @param imageFile - Image file
     * @return - returns registered event with a status code if succeeded
     */
    @PutMapping("update/image")
    public ResponseEntity<?> updateEventWithImage(@RequestPart EventDTO eventDTO, @RequestPart MultipartFile imageFile) {
        try {

            Event event = this.eventService
                    .updateEvent(eventDTO, imageFile);

            EventDTO event1 = eventService.getEvenDTOByID(event.getEventID());

            if (event1 != null) {
                logger.info("Updated event with ID: E{}", event.getEventID());
                return new ResponseEntity<>(event1, HttpStatus.OK);
            } else {
                return new ResponseEntity<>(HttpStatus.NOT_ACCEPTABLE);
            }

        } catch (Exception e) {
            logger.error(e);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Method to get an event by ID
     * @param id - event ID
     * @return - Event Data Transfer Object with status code if successful
     */
    @GetMapping("/{id}")
    public ResponseEntity<EventDTO> getEventDTOById(@PathVariable int id) {

        EventDTO event = eventService.getEvenDTOByID(id);

        if (event == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } else {
            return new ResponseEntity<>(event, HttpStatus.OK);
        }

    }

    /**
     * End point to get all the events hosted by a specific vendor
     * @param vendorID - vendor ID
     * @return - A list of events hosted by the target vendor along with a status code
     */
    @GetMapping("vendor/{vendorID}")
    public ResponseEntity<List<EventDTO>> getAllEventsByVendorID(@PathVariable int vendorID) {

        if (vendorService.getVendorByID(vendorID) == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } else {
            List<EventDTO> events = eventService.getAllEventsByVendorID(vendorID);

            if (events.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NOT_IMPLEMENTED);
            } else {
                return new ResponseEntity<>(events, HttpStatus.OK);
            }
        }
    }

    /**
     * End point to update event without image details,
     * Note that there is possibility to loose image data if the event is registered with an image already, handle with caution
     * @param eventDTO- Event Data Transfer Object
     * @return - A string with status code
     */
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

    /**
     * End point to delete an event
     * @param eventID - Event ID
     * @return - A string with a status code
     */
    @DeleteMapping("/delete/{eventID}")
    public ResponseEntity<String> deleteEvent(@PathVariable int eventID) {

        Event event =  eventService.getEventByID(eventID);

        if (event != null) {
            eventService.deleteEvent(event);
            return new ResponseEntity<>("Success", HttpStatus.OK);
        } else
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);

    }

    /**
     * End point to get image of an event
     * @param eventID - Event ID
     * @return - The raw data of the image
     */
    @GetMapping("{eventID}/image")
    public ResponseEntity<byte[]> getImageByEventID(@PathVariable int eventID) {
        Event event = eventService.getEventByID(eventID);

        if (event != null) {
            byte[] imageFile = event.getImageData();

            if (imageFile != null) {
                return ResponseEntity.ok()
                        .contentType(MediaType.valueOf(event.getImageType()))
                        .body(imageFile);
            } else
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);

        } else
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

}
