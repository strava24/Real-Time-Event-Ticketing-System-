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

    public List<EventDTO> getAllEvents() {
        return eventRepo.findAll().stream()
                .map(event -> new EventDTO(
                        event.getEventID(),
                        event.getEventName(),
                        event.getVendor().getVendorID()
                ))
                .collect(Collectors.toList());
    }

    public void createEvent(Event event) {
        eventRepo.save(event);
    }

    public int createEvent(String eventName, Vendor vendor, String date) {
        Event event = new Event(eventName, date, vendor);
        eventRepo.save(event);

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
            return new EventDTO(event.getEventID(), event.getEventName(), event.getVendor().getVendorID());
        }

    }

    public int getEventIDByName(String eventName) {
        return eventRepo.findEventIDByEventName(eventName);
    }

//    public int getAvailableTicketsByID(int eventID) {
//        return eventRepo.findTotalTicketsByEvent(eventID);
//    }
}
