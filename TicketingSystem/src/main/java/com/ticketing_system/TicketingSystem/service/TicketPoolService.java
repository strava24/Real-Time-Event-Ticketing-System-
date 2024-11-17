package com.ticketing_system.TicketingSystem.service;

import com.ticketing_system.TicketingSystem.model.Event;
import com.ticketing_system.TicketingSystem.model.Vendor;
import com.ticketing_system.TicketingSystem.repository.VendorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import com.ticketing_system.TicketingSystem.model.Configuration;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class TicketPoolService {

    @Autowired
    VendorRepository vendorRepository;

    @Autowired
    VendorService vendorService;

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
        } else {
            ai = vendorService.signupVendor(new Vendor("A.I. Inc.", "ai@gmail.com", "ai"));
        }

        return ai.createNewEvent(new Event("A.I. Meetup", configuration.getTotalTickets(), ai, configuration.getMaxTicketCapacity())); // Creating an event

    }

//    public void createTicketPool(DummyTicketPool dummyTicketPool) {
//        ticketPoolRepository.save(dummyTicketPool);
//    }

}
