package com.ticketing_system.TicketingSystem.model;

import jakarta.persistence.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Entity
@Component
public class Ticket {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int ticketID;

    @ManyToOne
    @JoinColumn(name = "event_id")
    private Event event;

    @ManyToOne
    @JoinColumn(name = "customer_id") // This column holds the foreign key
    private Customer customer;

    public Ticket(Event event, Customer customer) {
        this.event = event;
        this.customer = customer;
    }

    public Ticket() {
    }

    public int getTicketID() {
        return ticketID;
    }

    public Event getEvent() {
        return event;
    }

    @Autowired
    public void setEvent(Event event) {
        this.event = event;
    }
}
