package com.ticketing_system.TicketingSystem.model;

import jakarta.persistence.*;
import org.springframework.stereotype.Component;

@Entity
@Component
public class Event{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int eventID;
    private String eventName;
    private int totalTickets; // Total tickets allocated for this event
    private int maxTicketCapacity;

//    @OneToMany(mappedBy = "event", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
//    private List<Ticket> ticketsSold; // Tickets sold for the show

    // Reference to foreign Key
    @ManyToOne
    @JoinColumn(name = "vendor_id") // This column holds the foreign key
    private Vendor vendor;

    @Transient // To restrict ticket pool from becoming a column in the DB
    private TicketPool ticketPool;

//    // An event can have many ticket pools
//    @OneToMany(mappedBy = "event", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
////    private List<DummyTicketPool> dummyTicketPools = new ArrayList<>();

    public Event(String eventName, int totalTickets, Vendor vendor, int maxTicketCapacity) {
        this.eventName = eventName;
        this.totalTickets = totalTickets;
        this.vendor = vendor;
        this.maxTicketCapacity = maxTicketCapacity;
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

    public int getTotalTickets() {
        return totalTickets;
    }

    public void setTotalTickets(int maxTicketCapacity) {
        this.totalTickets = maxTicketCapacity;
    }

    public Vendor getVendor() {
        return vendor;
    }

    public int getMaxTicketCapacity() {
        return maxTicketCapacity;
    }

    public void setMaxTicketCapacity(int maxTicketCapacity) {
        this.maxTicketCapacity = maxTicketCapacity;
    }

    @Override
    public String toString() {
        return "Event{" +
                "eventID=" + eventID +
                ", eventName='" + eventName + '\'' +
                '}';
    }

    public void createTicketPool() {

        this.ticketPool = new TicketPool(maxTicketCapacity, totalTickets);

        System.out.println("Created new ticket pool");

    }

//    /**
//     * Method to add a new ticket pool
//     * @param dummyTicketPool - an instance of ticket pool for the current thread
//     */
//    public void addTicketPool(DummyTicketPool dummyTicketPool, VendorService vendorService) {
//        dummyTicketPools.add(dummyTicketPool);
//        vendorService.produceTickets(this, dummyTicketPool); // Start ticket production
//    }

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
