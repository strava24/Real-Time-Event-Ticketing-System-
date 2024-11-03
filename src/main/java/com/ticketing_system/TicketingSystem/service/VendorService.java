package com.ticketing_system.TicketingSystem.service;

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

    public void signupVendor(Vendor vendor) {
        vendorRepo.save(vendor);
    }

    public Vendor updateVendorByID(int id, Vendor vendor) {
        return vendorRepo.save(vendor);
    }

    public int getNoOfEventsHostedByVendorID(int id) {
        return eventRepo.countByVendorId(id);
    }
}
