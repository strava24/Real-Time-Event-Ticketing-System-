package com.ticketing_system.TicketingSystem.service;

import com.ticketing_system.TicketingSystem.model.*;
import com.ticketing_system.TicketingSystem.repository.TicketPoolRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class TicketPoolService {

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
     * @return the vendor ID of A.I.Inc is returned
     */
    public Map<String, Integer> createTicketPool(Configuration configuration, int eventID) {
        Event event = eventService.getEventByID(eventID);

        if (event != null) {

            Vendor vendor = eventService.getEventByID(eventID).getVendor();

            if (vendor != null) {
                TicketPool ticketPool =  vendor.createNewEvent(event, configuration.getMaxTicketCapacity(), configuration.getTotalTickets());
                saveTicketPool(ticketPool);

                Map<String, Integer> map = new HashMap<>();
                map.put("aiVendorID", vendor.getVendorID());
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
     * @param aiVendorID
     * @return
     */
    public synchronized boolean addTicket(int aiVendorID, int poolId) {

        Vendor vendor = vendorService.getVendorByID(aiVendorID);
        TicketPool ticketPool = findTicketPoolByID(poolId);

        if (vendor != null && ticketPool != null) {
            Ticket ticket =  ticketPool.addTicket();

            if (ticketService.addTicket(ticket))  {
                System.out.println(vendor.getVendorName() + " sold " + vendor.incrementTicketsSold() + " tickets");
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
            if (ticketService.removeTicket(ticket)) {
                System.out.println(customer.getCustomerName() + " bought " + customer.incrementBoughtTickets() + " tickets");
            }
            return ticketService.removeTicket(ticket);

        } else
            return false;
    }

    public TicketPool findTicketPoolByID(int id) {
        return ticketPoolRepository.findById(id).orElse(null);
    }

}
