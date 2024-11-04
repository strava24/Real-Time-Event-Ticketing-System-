package com.ticketing_system.TicketingSystem.model;

import jakarta.persistence.*;
import org.springframework.stereotype.Component;

import java.util.List;

@Entity
@Component
public class Event {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int eventID;
    private String eventName;
    private int totalTickets; // Total tickets allocated for this event

    @OneToMany(mappedBy = "event", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Ticket> ticketsSold; // Tickets sold for the show

    // Reference to foreign Key
    @ManyToOne
    @JoinColumn(name = "vendor_id") // This column holds the foreign key
    private Vendor vendor;

    public Event(String eventName, int totalTickets, Vendor vendor) {
        this.eventName = eventName;
        this.totalTickets = totalTickets;
        this.vendor = vendor;
    }

    public Event() {}

    public int getEventID() {
        return eventID;
    }

    public String getEventName() {
        return eventName;
    }

    public void setEventName(String eventName) {
        this.eventName = eventName;
    }

    public List<Ticket> getTicketsSold() {
        return ticketsSold;
    }

    public int getTotalTickets() {
        return totalTickets;
    }

    public void setTotalTickets(int totalTickets) {
        this.totalTickets = totalTickets;
    }

    public Vendor getVendor() {
        return vendor;
    }

    public void setVendor(Vendor vendor) {
        this.vendor = vendor;
    }

    @Override
    public String toString() {
        return "Event{" +
                "eventID=" + eventID +
                ", eventName='" + eventName + '\'' +
                '}';
    }
}
