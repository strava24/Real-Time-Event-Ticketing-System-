package com.ticketing_system.TicketingSystem.service;

import com.ticketing_system.TicketingSystem.model.Ticket;
import com.ticketing_system.TicketingSystem.repository.TicketRepository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.stereotype.Service;

@Service
public class TicketService {

    private static final Logger logger = LogManager.getLogger(TicketService.class);

    @Autowired
    TicketRepository ticketRepository;

    /**
     * Method to add a ticket on the Ticket table of DB
     * @param ticket - The ticket object to add on the DB
     * @return - if the ticket is added returns true
     */
    public boolean addTicket(Ticket ticket) {
        if (ticket == null) {
            logger.error("Ticket is null : Cannot sell!");
            return false;
        } else {
            logger.info("Ticket added : T{} into pool : P{}", ticket.getTicketID(), ticket.getTicketPool().getPoolID());
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
            logger.error("Ticket is null : Cannot buy!");
            return false;
        } else {
            try {
                logger.info("Ticket removed : T{} from pool : P{}", ticket.getTicketID(), ticket.getTicketPool().getPoolID());
                ticketRepository.delete(ticket);
                return true;
            } catch (ObjectOptimisticLockingFailureException e) {
                logger.error("Another thread has modified the system!! : {}", String.valueOf(e));

                return false;
            }
        }


    }
}
