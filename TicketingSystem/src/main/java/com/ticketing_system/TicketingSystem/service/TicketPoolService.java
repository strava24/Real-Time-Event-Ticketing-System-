package com.ticketing_system.TicketingSystem.service;

import com.ticketing_system.TicketingSystem.DTO.TicketPoolDTO;
import com.ticketing_system.TicketingSystem.model.*;
import com.ticketing_system.TicketingSystem.repository.TicketPoolRepository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class TicketPoolService {

    private static final Logger logger = LogManager.getLogger(TicketPoolService.class);

    @Autowired
    VendorService vendorService;

    @Autowired
    TicketPoolRepository ticketPoolRepository;

    @Autowired
    private EventService eventService;

    @Autowired
    private TicketService ticketService;

    @Autowired
    private CustomerService customerService;

    /**
     * This method is to create a ticket pool for automatic events
     *
     * @param configuration - The configuration details from the CLI to create the pool
     * @return
     */
    public Map<String, Integer> createTicketPool(int maxTicketCapacity, int totalTickets, int eventID, String poolName, int ticketPrice) {
        Event event = eventService.getEventByID(eventID);

        if (event != null) {

            Vendor vendor = eventService.getEventByID(eventID).getVendor();

            if (vendor != null) {
                TicketPool ticketPool =  vendor.createNewTicketPool(event, maxTicketCapacity, totalTickets, poolName, ticketPrice);
                saveTicketPool(ticketPool);

                logger.info("Created Ticket Pool with ID : P{}", ticketPool.getPoolID());

                Map<String, Integer> map = new HashMap<>();
                map.put("poolID", ticketPool.getPoolID());

                return map; // Creating an event
            } else return null;

        } else {
            return null;
        }
    }

    public void saveTicketPool(TicketPool ticketPool) {
        ticketPoolRepository.save(ticketPool);
    }


    /**
     * Method handling the business logic to add a ticket to a pool
     * @param vendorID
     * @return
     */
    public synchronized boolean addTicket(int vendorID, int poolId) {

        Vendor vendor = vendorService.getVendorByID(vendorID);
        TicketPool ticketPool = findTicketPoolByID(poolId);

        if (vendor != null && ticketPool != null) {
            Ticket ticket =  ticketPool.addTicket();

            if (ticketService.addTicket(ticket))  {
                int count =  vendor.incrementTicketsSold();
                logger.info("{} has sold {} tickets", vendor.getVendorName(), count);
            }
            return ticketService.addTicket(ticket);

        } else
            return false;

    }

    public synchronized TicketPool getTicketPoolByID(int id) {
        return ticketPoolRepository.findById(id).orElse(null);
    }

    /**
     * Method to remove a ticket from a pool
     * @param poolID - The pool from which the user is trying to remove the ticket
     * @param customerID - The id of the customer trying to buy the ticket
     * @return returns true if the ticket is removed successfully else returns false
     */
    public boolean removeTicket(int poolID, int customerID) {

        Customer customer = customerService.getCustomerByID(customerID);
        TicketPool ticketPool = findTicketPoolByID(poolID);

        if (customer != null && ticketPool != null) {

            Ticket ticket = ticketPool.removeTicket();

            boolean removed = ticketService.removeTicket(ticket);

            if (removed) {
                logger.info("{} has bought {} tickets", customer.getCustomerName(), customer.incrementBoughtTickets());
            }
            return removed;

        } else
            return false;
    }

    public TicketPool findTicketPoolByID(int id) {
        return ticketPoolRepository.findById(id).orElse(null);
    }

    public List<TicketPoolDTO> getAllTicketPools(int eventID) {

        List<TicketPool> ticketPools = ticketPoolRepository.findByEventId(eventID);

        if (ticketPools.isEmpty()) {
            return null;
        } else {

            logger.info("Retrieving all the ticket pools from E{}", eventID);

            return ticketPools.stream()
                    .map(ticketPool -> new TicketPoolDTO(
                            ticketPool.getPoolID(),
                            ticketPool.getPoolName(),
                            ticketPool.getMaxTicketCapacity(),
                            ticketPool.getTotalTickets(),
                            ticketPool.getTicketsSold(),
                            ticketPool.getTicketsBought(),
                            ticketPool.getTicketPrice()
                    ))
                    .collect(Collectors.toList());
        }

    }
}
