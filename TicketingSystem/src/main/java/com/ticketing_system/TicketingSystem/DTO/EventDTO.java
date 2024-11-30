package com.ticketing_system.TicketingSystem.DTO;

import com.ticketing_system.TicketingSystem.model.Event;
import com.ticketing_system.TicketingSystem.model.Vendor;

import java.time.LocalDate;

// Data Transfer Object to prevent infinite recursion on the response
public class EventDTO {

    private int eventID;
    private String eventName;
    private int vendorID; // Only vendor ID instead of full Vendor object
    private LocalDate date;
    private String location;

    public EventDTO(int eventID, String eventName, int vendorID, String date, String location) {
        this.eventID = eventID;
        this.eventName = eventName;
        this.vendorID = vendorID;
        this.date = LocalDate.parse(date);
        this.location = location;
    }

    public EventDTO(String eventName, int vendorID, String date, String location) {
        this.eventName = eventName;
        this.vendorID = vendorID;
        this.date = LocalDate.parse(date);
        this.location = location;
    }

    public EventDTO() {
    }

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

    public int getVendorID() {
        return vendorID;
    }

    public void setVendorID(int vendorID) {
        this.vendorID = vendorID;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

}
