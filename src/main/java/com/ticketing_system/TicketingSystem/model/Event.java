package com.ticketing_system.TicketingSystem.model;

import org.springframework.stereotype.Component;

@Component
public class Event {

    private int eventID;
    private String eventName;
    private int ticketsSold; // Tickets sold for the show

    public Event(int eventID, String eventName) {
        this.eventID = eventID;
        this.eventName = eventName;
    }

    public Event() {}

    public int getEventID() {
        return eventID;
    }

    public void setEventID(int eventID) {
        this.eventID = eventID;
    }

    public String getEventName() {
        return eventName;
    }

    public void setEventName(String eventName) {
        this.eventName = eventName;
    }

    public int getTicketsSold() {
        return ticketsSold;
    }

    @Override
    public String toString() {
        return "Event{" +
                "eventID=" + eventID +
                ", eventName='" + eventName + '\'' +
                '}';
    }
}
