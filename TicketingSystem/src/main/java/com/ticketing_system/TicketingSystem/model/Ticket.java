package com.ticketing_system.TicketingSystem.model;

import jakarta.persistence.*;
import org.springframework.stereotype.Component;

@Entity
@Component
public class Ticket {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int ticketID;

    // Reference to foreign Key
    @ManyToOne
    @JoinColumn(name = "pool_id") // This column holds the foreign key
    private TicketPool ticketPool;

    public Ticket(TicketPool ticketPool) {
        this.ticketPool = ticketPool;
    }

    public Ticket() {}
}
