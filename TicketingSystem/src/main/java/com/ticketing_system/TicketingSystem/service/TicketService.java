package com.ticketing_system.TicketingSystem.service;

import com.ticketing_system.TicketingSystem.model.Ticket;
import com.ticketing_system.TicketingSystem.repository.TicketRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.stereotype.Service;

@Service
public class TicketService {

    @Autowired
    TicketRepository ticketRepository;

    /**
     * Method to add a ticket on the Ticket table of DB
     * @param ticket - The ticket object to add on the DB
     * @return - if the ticket is added returns true
     */
    public boolean addTicket(Ticket ticket) {
        if (ticket == null) {
            System.out.println("Ticket is null");
            return false;
        } else {
            ticketRepository.save(ticket);
            return true;
        }
    }

    /**
     * Method to delete a ticket on the ticket DB
     * @param ticket - ticket object which should be deleted from the DB
     * @return - returns true if the ticket is deleted successfully
     */
    public boolean removeTicket(Ticket ticket) {
        if (ticket == null) {
            System.out.println("Ticket is null");
            return false;
        } else {
            try {
                ticketRepository.delete(ticket);
                return true;
            } catch (ObjectOptimisticLockingFailureException e) {
                System.out.println("Another thread has modified the system!!");
                return false;
            }
        }


    }
}
