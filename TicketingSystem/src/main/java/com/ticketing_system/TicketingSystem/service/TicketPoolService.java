package com.ticketing_system.TicketingSystem.service;

import com.ticketing_system.TicketingSystem.model.*;
import com.ticketing_system.TicketingSystem.repository.EventRepository;
import com.ticketing_system.TicketingSystem.repository.TicketPoolRepository;
import com.ticketing_system.TicketingSystem.repository.VendorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class TicketPoolService {

    @Autowired
    VendorRepository vendorRepository;

    @Autowired
    VendorService vendorService;

    @Autowired
    TicketPoolRepository ticketPoolRepository;
    @Autowired
    private EventService eventService;
    @Autowired
    private Event event;
    @Autowired
    private EventRepository eventRepository;
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
    public Map<String, Integer> createTicketPool(Configuration configuration) {

        Optional<Vendor> vendor = vendorRepository.findByVendorEmail("ai@gmail.com");
        Vendor ai;

        if (vendor.isPresent()) {
            ai = vendor.get();

            // If the vendor is present have to check if default event is already there, if so adding a pool to the event

        } else {
            ai = vendorService.signupVendor(new Vendor("A.I. Inc.", "ai@gmail.com", "ai"));
        }

        Event defaultEvent = new Event("A.I. Meetup", configuration.getTotalTickets(), ai, configuration.getMaxTicketCapacity());
        eventService.createEvent(defaultEvent);

        TicketPool ticketPool =  ai.createNewEvent(defaultEvent);
        saveTicketPool(ticketPool);

        Map<String, Integer> map = new HashMap<>();
        map.put("aiVendorID", ai.getVendorID());
        map.put("poolID", ticketPool.getPoolID());

        return map; // Creating an event

    }

    public void saveTicketPool(TicketPool ticketPool) {
        ticketPoolRepository.save(ticketPool);
    }


    /**
     * Method handling the business logic to add a ticket to a pool
     * @param aiVendorID
     * @return
     */
    public synchronized boolean addTicket(int aiVendorID) {

        Vendor vendor = vendorRepository.findById(aiVendorID).orElse(null);

        if (vendor != null) {
            List<TicketPool> ticketPools = ticketPoolRepository.findByEventId(vendor.getHostedEvents().getLast().getEventID());
            Ticket ticket =  ticketPools.getLast().addTicket();

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
            System.out.println(customer.incrementBoughtTickets());
            return ticketService.removeTicket(ticket);

        } else
            return false;

    }

    public TicketPool findTicketPoolByID(int id) {
        return ticketPoolRepository.findById(id).orElse(null);
    }

//    public void createTicketPool(DummyTicketPool dummyTicketPool) {
//        ticketPoolRepository.save(dummyTicketPool);
//    }

}
