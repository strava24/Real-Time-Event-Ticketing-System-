package com.ticketing_system.TicketingSystem.DTO;

import java.time.LocalDate;
import java.util.Arrays;

// Data Transfer Object to prevent infinite recursion on the response
public class EventDTO {

    private int eventID;
    private String eventName;
    private int vendorID; // Only vendor ID instead of full Vendor object
    private LocalDate date;
    private String location;
    private String imageName;
    private String imageType;
    private byte[] imageData;

    public EventDTO(int eventID, String eventName, int vendorID, String date, String location) {
        this.eventID = eventID;
        this.eventName = eventName;
        this.vendorID = vendorID;
        this.date = LocalDate.parse(date);
        this.location = location;
    }

//    public EventDTO(String eventName, int vendorID, String date, String location) {
//        this.eventName = eventName;
//        this.vendorID = vendorID;
//        this.date = LocalDate.parse(date);
//        this.location = location;
//    }
//
//    public EventDTO(String eventName, LocalDate date, int vendorID, String location, String imageName, String imageType, byte[] imageData) {
//        this.eventName = eventName;
//        this.vendorID = vendorID;
//        this.date = date;
//        this.location = location;
//        this.imageName = imageName;
//        this.imageType = imageType;
//        this.imageData = imageData;
//    }

    public EventDTO() {
    }

    public EventDTO(int eventID, String eventName, int vendorID, String string, String location, String imageName, String imageType, byte[] imageData) {
        this.eventID = eventID;
        this.eventName = eventName;
        this.vendorID = vendorID;
        this.date = LocalDate.parse(string);
        this.location = location;
        this.imageName = imageName;
        this.imageType = imageType;
        this.imageData = imageData;

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

    public String getImageName() {
        return imageName;
    }

    public void setImageName(String imageName) {
        this.imageName = imageName;
    }

    public String getImageType() {
        return imageType;
    }

    public void setImageType(String imageType) {
        this.imageType = imageType;
    }

    public byte[] getImageData() {
        return imageData;
    }

    public void setImageData(byte[] imageData) {
        this.imageData = imageData;
    }

    @Override
    public String toString() {
        return "EventDTO{" +
                "eventID=" + eventID +
                ", eventName='" + eventName + '\'' +
                ", vendorID=" + vendorID +
                ", date=" + date +
                ", location='" + location + '\'' +
                ", imageName='" + imageName + '\'' +
                ", imageType='" + imageType + '\'' +
                '}';
    }
}
