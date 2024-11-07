package com.ticketing_system.TicketingSystem.service;

import com.ticketing_system.TicketingSystem.model.Event;
import com.ticketing_system.TicketingSystem.repository.EventRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EventService {

    @Autowired
    private EventRepository eventRepo;

    public List<Event> getAllEvents() {
        return eventRepo.findAll();
    }

    public void createEvent(Event event) {
        eventRepo.save(event);
    }

    public Event getEventByID(int eventID) {
        return eventRepo.findById(eventID).orElse(null);
    }

    public int getAvailableTicketsByID(int eventID) {
        return eventRepo.findTotalTicketsByEvent(eventID);
    }
}
