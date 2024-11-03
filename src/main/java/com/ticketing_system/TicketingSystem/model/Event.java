package com.ticketing_system.TicketingSystem.model;

import jakarta.persistence.*;
import org.springframework.stereotype.Component;

@Entity
@Component
public class Event {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int eventID;
    private String eventName;
    private int ticketsSold; // Tickets sold for the show

    // Reference to foreign Key
    @ManyToOne
    @JoinColumn(name = "vendor_id") // This column holds the foreign key
    private Vendor vendor;

    public Event(String eventName, int ticketsSold) {
        this.eventName = eventName;
        this.ticketsSold = ticketsSold;
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
