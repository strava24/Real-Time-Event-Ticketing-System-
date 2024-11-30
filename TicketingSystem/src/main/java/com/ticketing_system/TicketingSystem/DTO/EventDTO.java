package com.ticketing_system.TicketingSystem.DTO;

// Data Transfer Object to prevent infinite recursion on the response
public class EventDTO {

    private int eventID;
    private String eventName;
    private int vendorID; // Only vendor ID instead of full Vendor object

    public EventDTO(int eventID, String eventName, int vendorID) {
        this.eventID = eventID;
        this.eventName = eventName;
        this.vendorID = vendorID;
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
}
