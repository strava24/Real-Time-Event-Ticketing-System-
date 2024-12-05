package com.ticketing_system.TicketingSystem.service;

import com.ticketing_system.TicketingSystem.DTO.EventDTO;
import com.ticketing_system.TicketingSystem.model.Event;
import com.ticketing_system.TicketingSystem.model.Vendor;
import com.ticketing_system.TicketingSystem.repository.EventRepository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class EventService {

    private static final Logger logger = LogManager.getLogger(EventService.class);

    @Autowired
    private EventRepository eventRepo;
    @Autowired
    private VendorService vendorService;

    public List<EventDTO> getAllEvents() {
        logger.info("Retrieving all events");
        return eventRepo.findAll().stream()
                .map(event -> new EventDTO(
                        event.getEventID(),
                        event.getEventName(),
                        event.getVendor().getVendorID(),
                        event.getEventDate().toString(),
                        event.getLocation()
                ))
                .collect(Collectors.toList());
    }

    public int createEvent(Event event) {
        eventRepo.save(event);
        event.getVendor().addNewEvent(event);
        logger.info("Created an event with ID: E{}", event.getEventID());
        return event.getEventID();
    }

    public int createEvent(String eventName, Vendor vendor, String date, String location) {
        Event event = new Event(eventName, date, vendor, location);
        eventRepo.save(event);
        vendor.addNewEvent(event);

        return event.getEventID();
    }

    public Event getEventByID(int eventID) {
        return eventRepo.findById(eventID).orElse(null);
    }

    public EventDTO getEvenDTOByID(int eventID) {
        Event event = eventRepo.findById(eventID).orElse(null);

        if (event == null) {
            return null;
        } else {

            return new EventDTO(
                    event.getEventID(),
                    event.getEventName(),
                    event.getVendor().getVendorID(),
                    event.getEventDate().toString(),
                    event.getLocation(),
                    event.getImageName(),
                    event.getImageType(),
                    event.getImageData());
        }

    }

    public int getEventIDByName(String eventName) {
        return eventRepo.findEventIDByEventName(eventName);
    }

    public Event convertToEventFromDTO(EventDTO eventDTO) {
        Vendor vendor = vendorService.getVendorByID(eventDTO.getVendorID());

        if (vendor == null) {
            return null;
        } else {
            return new Event(eventDTO.getEventName(), eventDTO.getDate().toString(), vendor, eventDTO.getLocation(), eventDTO.getImageName(), eventDTO.getImageData(), eventDTO.getImageType());
        }

    }

    public List<EventDTO> getAllEventsByVendorID(int vendorID) {

        logger.info("Retrieving all events by Vendor ID V{}", vendorID);

        return eventRepo.findByVendorId(vendorID).stream()
                .map(event -> new EventDTO(
                        event.getEventID(),
                        event.getEventName(),
                        event.getVendor().getVendorID(),
                        event.getEventDate().toString(),
                        event.getLocation()
                ))
                .collect(Collectors.toList());

    }

    public void updateEvent(Event event) {
        eventRepo.save(event);
    }

    public void deleteEvent(Event event) {
        eventRepo.delete(event);
    }

    public Event createEvent(EventDTO eventDTO, MultipartFile imageFile) throws IOException {
        eventDTO.setImageName(imageFile.getOriginalFilename());
        eventDTO.setImageType(imageFile.getContentType());
        eventDTO.setImageData(imageFile.getBytes());

        Event event = convertToEventFromDTO(eventDTO);
        System.out.println(event);
        logger.info("Created an event with image data");
        return eventRepo.save(event);

    }

//    public int getAvailableTicketsByID(int eventID) {
//        return eventRepo.findTotalTicketsByEvent(eventID);
//    }
}
