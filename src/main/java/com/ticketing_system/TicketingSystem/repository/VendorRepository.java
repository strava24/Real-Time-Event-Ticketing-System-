package com.ticketing_system.TicketingSystem.repository;

import com.ticketing_system.TicketingSystem.model.Vendor;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface VendorRepository extends JpaRepository<Vendor, Integer> {

    /**
     * method to enable login features for vendors
     * finds the vendor based on their email address
     * @param vendorEmail - checks if there is a record with this email
     * @return an Optional Vendor if such vendor is found if not returns an empty Optional
     */
    Optional<Vendor> findByVendorEmail(String vendorEmail);

}
