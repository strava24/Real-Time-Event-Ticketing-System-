package com.ticketing_system.TicketingSystem.service;

import com.ticketing_system.TicketingSystem.model.*;
import com.ticketing_system.TicketingSystem.repository.EventRepository;
import com.ticketing_system.TicketingSystem.repository.TicketPoolRepository;
import com.ticketing_system.TicketingSystem.repository.VendorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;
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

    /**
     * This method is to create a ticket pool for automatic events
     *
     * @param configuration - The configuration details from the CLI to create the pool
     * @return the vendor ID of A.I.Inc is returned
     */
    public int createTicketPool(Configuration configuration) {

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

        return ai.getVendorID(); // Creating an event

    }

    public void saveTicketPool(TicketPool ticketPool) {
        ticketPoolRepository.save(ticketPool);
    }

    public boolean addTicket(int aiVendorID) {

        Vendor vendor = vendorRepository.findById(aiVendorID).orElse(null);

        if (vendor != null) {
            List<TicketPool> ticketPools = ticketPoolRepository.findByEventId(vendor.getHostedEvents().getLast().getEventID());
            Ticket ticket =  ticketPools.getLast().addTicket();
            ticketService.addTicket(ticket);
            return true;

        } else
            return false;

    }

    public TicketPool getTicketPoolByID(int id) {
        return ticketPoolRepository.findById(id).orElse(null);
    }

//    public void createTicketPool(DummyTicketPool dummyTicketPool) {
//        ticketPoolRepository.save(dummyTicketPool);
//    }

}
