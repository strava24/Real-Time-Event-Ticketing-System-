package com.ticketing_system.TicketingSystem.service;

import com.ticketing_system.TicketingSystem.DTO.VendorDTO;
import com.ticketing_system.TicketingSystem.model.Vendor;
import com.ticketing_system.TicketingSystem.repository.EventRepository;
import com.ticketing_system.TicketingSystem.repository.VendorRepository;
import org.apache.logging.log4j.LogManager;

import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class VendorService {

    private static final Logger logger = LogManager.getLogger(VendorService.class);

    @Autowired
    private VendorRepository vendorRepo;

    @Autowired
    private EventRepository eventRepo;

    /**
     * Method to get a vendor by ID
     * @param id - Vendor ID
     * @return - Vendor object
     */
    public Vendor getVendorByID(int id) {
        return vendorRepo.findById(id).orElse(null);
    }

    /**
     * Method to get a vendor data transfer object by ID
     * @param vendorID - Vendor ID
     * @return - Vendor data transfer object
     */
    public VendorDTO getVendorDTOByID(int vendorID) {
        Vendor vendor = vendorRepo.findById(vendorID).orElse(null);

        if (vendor == null) {
            return null;
        } else {
            logger.info("Retrieving data about vendor with ID : V{}", vendor.getVendorID());
            return new VendorDTO(vendor.getVendorID(), vendor.getVendorName(), vendor.getVendorEmail(), vendor.getVendorPassword(), vendor.getTicketsSold());
        }

    }

    /**
     * Method to get all the vendors in the system
     * @return list of vendor data transfer object
     */
    public List<VendorDTO> getAllVendors() {
        logger.info("Retrieving all vendors");
        return vendorRepo.findAll().stream()
                .map(vendor -> new VendorDTO(
                        vendor.getVendorID(),
                        vendor.getVendorName(),
                        vendor.getVendorEmail(),
                        vendor.getVendorPassword(),
                        vendor.getTicketsSold()
                ))
                .collect(Collectors.toList());
    }

    /**
     * Method to handle the logic to login a registered vendor
     * @param email - email of the registered vendor
     * @param password - password of the registered vendor
     * @return - if the credentials match successfully the method will return true
     */
    public Vendor loginVendor(String email, String password) {
        Optional<Vendor> vendor = vendorRepo.findByVendorEmail(email);

        // Checking if a vendor with this email exists if so checking if the credentials are matching
        if (vendor.isPresent() && vendor.get().getVendorPassword().equals(password)) {
            logger.info("Vendor with ID V{} has logged successfully.", vendor.get().getVendorID());
            return vendor.get();
        }
        return null;
    }

    /**
     * Method to sign up a vendor into the system
     * @param vendor - The vendor object
     * @return - The registered vendor
     */
    public Vendor signupVendor(Vendor vendor) {
        return vendorRepo.save(vendor);
    }

    /**
     * Method to update a vendor
     * @param id - Vendor ID
     * @param vendor - Vendor Object
     * @return - Updated vendor object
     */
    public Vendor updateVendorByID(int id, Vendor vendor) {
        logger.info("Updating vendor with ID : V{}", vendor.getVendorID());
        return vendorRepo.save(vendor);
    }

    /**
     * Method to get the no of events hosted by a vendor
     * @param id - Vendor ID
     * @return - No of events hosted by the vendor
     */
    public int getNoOfEventsHostedByVendorID(int id) {
        logger.info("Retrieving no of events hosted by vendor ID : V{}", id);
        return eventRepo.countByVendorId(id);
    }

}
