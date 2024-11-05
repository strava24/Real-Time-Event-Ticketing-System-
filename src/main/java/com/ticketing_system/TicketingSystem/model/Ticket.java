package com.ticketing_system.TicketingSystem.model;

import jakarta.persistence.*;
import org.springframework.stereotype.Component;

@Entity
@Component
public class Ticket {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int ticketID;

//    @ManyToOne
//    @JoinColumn(name = "event_id")
//    private Event event;

    // A customer can purchase multiple tickets
    @ManyToOne
    @JoinColumn(name = "customer_id") // This column holds the foreign key
    private Customer customer;

    @ManyToOne // A ticketPool can have multiple tickets
    @JoinColumn(name = "ticket_pool_id") // This column holds the foreign key
    private TicketPool ticketPool;

    public Ticket(Customer customer) {
        this.customer = customer;
    }

    public Ticket() {
    }

    public int getTicketID() {
        return ticketID;
    }
    public Customer getCustomer() {
        return customer;
    }

    public TicketPool getTicketPool() {
        return ticketPool;
    }
}
