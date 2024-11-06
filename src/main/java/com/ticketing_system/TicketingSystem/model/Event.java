package com.ticketing_system.TicketingSystem.model;

import com.ticketing_system.TicketingSystem.service.VendorService;
import jakarta.persistence.*;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Entity
@Component
public class Event{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int eventID;
    private String eventName;
    private int maxTicketCapacity; // Total tickets allocated for this event

//    @OneToMany(mappedBy = "event", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
//    private List<Ticket> ticketsSold; // Tickets sold for the show

    // Reference to foreign Key
    @ManyToOne
    @JoinColumn(name = "vendor_id") // This column holds the foreign key
    private Vendor vendor;

    // An event can have many ticket pools
    @OneToMany(mappedBy = "event", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<TicketPool> ticketPools = new ArrayList<>();

    public Event(String eventName, int maxTicketCapacity, Vendor vendor) {
        this.eventName = eventName;
        this.maxTicketCapacity = maxTicketCapacity;
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

//    public List<Ticket> getTicketsSold() {
//        return ticketsSold;
//    }

    public int getMaxTicketCapacity() {
        return maxTicketCapacity;
    }

    public void setMaxTicketCapacity(int maxTicketCapacity) {
        this.maxTicketCapacity = maxTicketCapacity;
    }

    public Vendor getVendor() {
        return vendor;
    }

    @Override
    public String toString() {
        return "Event{" +
                "eventID=" + eventID +
                ", eventName='" + eventName + '\'' +
                '}';
    }

    /**
     * Method to add a new ticket pool
     * @param ticketPool - an instance of ticket pool for the current thread
     */
    public void addTicketPool(TicketPool ticketPool, VendorService vendorService) {
        ticketPools.add(ticketPool);
        vendorService.produceTickets(this, ticketPool); // Start ticket production
    }

//    @Override
//    public void run() {
////        produceTickets();
//    }
//
//    private void produceTickets() {
////        for (int i = 0; i < totalTickets; i++) {
////
////            @Autowired
////            Ticket ticket; // Create a new ticket
////            try {
////                ticketPool.addTicket(ticket); // Add the ticket to the pool
////                System.out.println("Produced ticket for event: " + name + " - Ticket ID: " + ticket.getId());
////                Thread.sleep(1000); // Sleep for 1 second after producing a ticket
////            } catch (InterruptedException e) {
////                Thread.currentThread().interrupt(); // Restore interrupted status
////                System.out.println("Ticket production interrupted for event: " + name);
////            }
////        }
//    }

}
