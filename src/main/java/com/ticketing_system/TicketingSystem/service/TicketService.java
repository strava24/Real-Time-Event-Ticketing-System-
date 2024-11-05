package com.ticketing_system.TicketingSystem.service;

import com.ticketing_system.TicketingSystem.model.Event;
import com.ticketing_system.TicketingSystem.model.Ticket;
import com.ticketing_system.TicketingSystem.model.TicketPool;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
public class TicketService {

    @Async // Allows the method to run in a separate thread
    public void produceTickets(Event event, TicketPool ticketPool) {
        int totalTickets = event.getTotalTickets();
        for (int i = 0; i < totalTickets; i++) {
            Ticket ticket = new Ticket(); // Create a new ticket instance
            ticketPool.addTicket(ticket); // Add the ticket to the pool
            System.out.println("Produced ticket for event: " + event.getEventName() + " - Ticket ID: " + ticket.getTicketID());
            try {
                Thread.sleep(1000); // Sleep for 1 second after producing a ticket
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt(); // Restore interrupted status
                System.out.println("Ticket production interrupted for event: " + event.getEventName());
            }
        }
    }
}
