package com.ticketing_system.TicketingSystem.model;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class Ticket {

    private int ticketID;

    @Autowired
    private Event event;

    @Autowired
    public Ticket(Event event) {
        this.event = event;
    }

    public int getTicketID() {
        return ticketID;
    }

    public Event getEvent() {
        return event;
    }

    @Autowired
    public void setEvent(Event event) {
        this.event = event;
    }
}
