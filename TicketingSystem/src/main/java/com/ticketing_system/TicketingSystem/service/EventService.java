package com.ticketing_system.TicketingSystem.service;

import com.ticketing_system.TicketingSystem.DTO.EventDTO;
import com.ticketing_system.TicketingSystem.model.Event;
import com.ticketing_system.TicketingSystem.model.Vendor;
import com.ticketing_system.TicketingSystem.repository.EventRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class EventService {

    @Autowired
    private EventRepository eventRepo;
    @Autowired
    private VendorService vendorService;

    public List<EventDTO> getAllEvents() {
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
                    event.getLocation());
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
            return new Event(eventDTO.getEventName(), eventDTO.getDate().toString(), vendor, eventDTO.getLocation());
        }

    }

//    public int getAvailableTicketsByID(int eventID) {
//        return eventRepo.findTotalTicketsByEvent(eventID);
//    }
}
