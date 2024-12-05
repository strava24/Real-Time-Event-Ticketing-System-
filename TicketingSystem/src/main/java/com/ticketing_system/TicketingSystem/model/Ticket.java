package com.ticketing_system.TicketingSystem.model;

import jakarta.persistence.*;
import org.springframework.stereotype.Component;

@Entity
@Component
public class Ticket {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int ticketID;

    @Version
    private int version; // Version field for optimistic locking

    // Reference to foreign Key
    @ManyToOne
    @JoinColumn(name = "pool_id") // This column holds the foreign key
    private TicketPool ticketPool;

    public Ticket(TicketPool ticketPool) {
        this.ticketPool = ticketPool;
    }

    public Ticket() {}

    public int getVersion() {
        return version;
    }

    public int getTicketID() {
        return ticketID;
    }

    public TicketPool getTicketPool() {
        return ticketPool;
    }
}