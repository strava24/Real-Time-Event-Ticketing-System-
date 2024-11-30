package com.ticketing_system.TicketingSystem.model;

import jakarta.persistence.*;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Component
public class Event{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int eventID;
    private String eventName;
    private LocalDate eventDate;
    private String location;

    // Reference to foreign Key
    @ManyToOne
    @JoinColumn(name = "vendor_id") // This column holds the foreign key
    private Vendor vendor;

    @OneToMany(mappedBy = "event", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<TicketPool> ticketPools;

    public Event(String eventName, String eventDate, Vendor vendor, String location) {
        this.eventName = eventName;
        this.eventDate = LocalDate.parse(eventDate);
        this.location = location;
        this.vendor = vendor;
        this.ticketPools = new ArrayList<>();
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

    public void setEventID(int eventID) {
        this.eventID = eventID;
    }

    public Vendor getVendor() {
        return vendor;
    }

    public List<TicketPool> getTicketPools() {
        return ticketPools;
    }

    @Override
    public String toString() {
        return "Event{" +
                "eventID=" + eventID +
                ", eventName='" + eventName + '\'' +
                '}';
    }

    public TicketPool createTicketPool(String poolName, int ticketPrice, int maxTicketCapacity, int totalTickets) {

        TicketPool newTicketPool = new TicketPool(poolName, ticketPrice, maxTicketCapacity, totalTickets, this);

        this.ticketPools.add(newTicketPool);
        System.out.println("Created new ticket pool");

        return newTicketPool;

    }

    public LocalDate getEventDate() {
        return eventDate;
    }

    public void setEventDate(LocalDate eventDate) {
        this.eventDate = eventDate;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }
}
