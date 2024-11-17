package com.ticketing_system.TicketingSystem.service;

import com.ticketing_system.TicketingSystem.model.Event;
//import com.ticketing_system.TicketingSystem.model.DummyTicketPool;
import com.ticketing_system.TicketingSystem.model.Vendor;
import com.ticketing_system.TicketingSystem.repository.EventRepository;
import com.ticketing_system.TicketingSystem.repository.VendorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class VendorService {

    @Autowired
    private VendorRepository vendorRepo;

    @Autowired
    private EventRepository eventRepo;

    public Vendor getVendorByID(int id) {
        return vendorRepo.findById(id).orElse(null);
    }

    public List<Vendor> getAllVendors() {
        return vendorRepo.findAll();
    }

    public boolean loginVendor(String email, String password) {
        Optional<Vendor> vendor = vendorRepo.findByVendorEmail(email);

        // Checking if a vendor with this email exists if so checking if the credentials are matching
        if (vendor.isPresent()) {
            return vendor.get().getVendorPassword().equals(password);
        }
        return false;
    }

    public Vendor signupVendor(Vendor vendor) {
        return vendorRepo.save(vendor);
    }

    public Vendor updateVendorByID(int id, Vendor vendor) {
        return vendorRepo.save(vendor);
    }

    public int getNoOfEventsHostedByVendorID(int id) {
        return eventRepo.countByVendorId(id);
    }

////    @Async // Allows the method to run in a separate thread
//    public void produceTickets(Event event, DummyTicketPool dummyTicketPool) {
//        int totalTickets = event.getTotalTickets();
//        for (int i = 0; i < totalTickets; i++) {
////            Ticket ticket = new Ticket(); // Create a new ticket instance
//            dummyTicketPool.addTicket(1); // Add the ticket to the pool - pass a ticket ID when doing do to have ticket no
//            System.out.println("Produced ticket for event: " + event.getEventName() + " - Ticket ID: " );
//            try {
//                Thread.sleep(1000); // Sleep for 1 second after producing a ticket
//            } catch (InterruptedException e) {
//                Thread.currentThread().interrupt(); // Restore interrupted status
//                System.out.println("Ticket production interrupted for event: " + event.getEventName());
//            }
//        }
//    }

}
