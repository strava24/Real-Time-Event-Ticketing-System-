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

    private String imageName;
    private String imageType;
    @Lob
    private byte[] imageData;

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

    public Event(String eventName, String date, Vendor vendor, String location, String imageName, byte[] imageData, String imageType) {

        this.eventName = eventName;
        this.eventDate = LocalDate.parse(date);
        this.location = location;
        this.imageName = imageName;
        this.imageData = imageData;
        this.imageType = imageType;
        this.vendor = vendor;
        this.ticketPools = new ArrayList<>();

    }


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
        return "Event{" +
                "eventID=" + eventID +
                ", eventName='" + eventName + '\'' +
                ", eventDate=" + eventDate +
                ", location='" + location + '\'' +
                ", imageName='" + imageName + '\'' +
                ", imageType='" + imageType + '\'' +
                ", vendor=" + vendor +
                ", ticketPools=" + ticketPools +
                '}';
    }
}
