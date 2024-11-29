package com.ticketing_system.TicketingSystem.model;

import jakarta.persistence.*;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Entity
@Component
public class Vendor{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int vendorID;
    private String vendorName;
    private String vendorEmail;
    private String vendorPassword;
    private int ticketsSold; // variable to keep count of the tickets sold

    @OneToMany(mappedBy = "vendor", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Event> hostedEvents;

    public Vendor(String vendorName, String vendorEmail, String vendorPassword) {
        this.vendorName = vendorName;
        this.vendorEmail = vendorEmail;
        this.vendorPassword = vendorPassword;
        hostedEvents = new ArrayList<Event>();
    }

    public Vendor() {
    }

    public int getVendorID() {
        return vendorID;
    }

    public String getVendorName() {
        return vendorName;
    }

    public void setVendorName(String vendorName) {
        this.vendorName = vendorName;
    }

    public String getVendorEmail() {
        return vendorEmail;
    }

    public void setVendorEmail(String vendorEmail) {
        this.vendorEmail = vendorEmail;
    }

    public List<Event> getHostedEvents() {
        return hostedEvents;
    }

    public void setHostedEvents(List<Event> hostedEvents) {
        this.hostedEvents = hostedEvents;
    }

    public String getVendorPassword() {
        return vendorPassword;
    }

    public void setVendorPassword(String vendorPassword) {
        this.vendorPassword = vendorPassword;
    }

    public TicketPool createNewEvent(Event event) {
        hostedEvents.add(event);
        // Creating a pool when an event is created
        return event.createTicketPool();

    }

    public int incrementTicketsSold() {
        this.ticketsSold++;
        return this.ticketsSold;
    }

}
