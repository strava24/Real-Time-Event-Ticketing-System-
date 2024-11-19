package com.ticketing_system.TicketingSystem.service;

import com.ticketing_system.TicketingSystem.model.Ticket;
import com.ticketing_system.TicketingSystem.repository.TicketRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TicketService {

    @Autowired
    TicketRepository ticketRepository;

    public void addTicket(Ticket ticket) {
        if (ticket == null) {
            System.out.println("Ticket is null");
        } else {
            ticketRepository.save(ticket);
        }


    }

}
