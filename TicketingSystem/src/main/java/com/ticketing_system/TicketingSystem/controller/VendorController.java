package com.ticketing_system.TicketingSystem.controller;

import com.ticketing_system.TicketingSystem.DTO.VendorDTO;
import com.ticketing_system.TicketingSystem.model.Vendor;
import com.ticketing_system.TicketingSystem.service.VendorService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.web.client.RestTemplateAutoConfiguration;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/vendors")
@CrossOrigin(origins = "http://localhost:4209")
public class VendorController {

    private static final Logger logger = LogManager.getLogger(VendorController.class);

    @Autowired
    private VendorService vendorService;
    @Autowired
    private RestTemplateAutoConfiguration restTemplateAutoConfiguration;

    /**
     * This method is to return all the vendors on the database
     * The plan is to display all the available vendors so users will be able to book tickets based on vendors
     * @return all vendors on the database
     */
    @GetMapping
    public ResponseEntity<List<VendorDTO>> getAllVendors() {

        List<VendorDTO> vendors = vendorService.getAllVendors();

        if (vendors.isEmpty())
            return new ResponseEntity<>(HttpStatus.NOT_IMPLEMENTED);
        else
            return new ResponseEntity<>(vendors, HttpStatus.OK);

    }

    /**
     * End point to get a vendor on the system
     * @param id - Vendor ID
     * @return - Vendor Data Transfer Object along with a status code
     */
    @GetMapping("/{id}")
    public ResponseEntity<VendorDTO> getVendorByID(@PathVariable int id) {
        VendorDTO vendor = vendorService.getVendorDTOByID(id);

        if (vendor != null)
            return new ResponseEntity<>(vendor, HttpStatus.OK);
        else
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);

    }

    /**
     * End point to login a vendor to the system
     * @param email - Email of the vendor
     * @param password - Password of the vendor
     * @return - The ID of the vendor along with the a status code
     */
    @PostMapping("/login")
    public ResponseEntity<Integer> loginVendor(@RequestParam String email, @RequestParam String password) {
        Vendor vendor = vendorService.loginVendor(email, password);

        if (vendor != null)
            return new ResponseEntity<>(vendor.getVendorID(), HttpStatus.OK);
        else
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    /**
     * End point to register a vendor to the system
     * @param vendor - The vendor object
     * @return - The ID of the vendor along with a status code
     */
    @PostMapping("/signup")
    public ResponseEntity<Integer> signupVendor(@RequestBody Vendor vendor) {
        Vendor newVendor = vendorService.signupVendor(vendor);
        logger.info("Vendor with ID V{} has logged successfully.", newVendor.getVendorID());
        return new ResponseEntity<>(newVendor.getVendorID(), HttpStatus.OK);
    }

    /**
     * End point to update a vendor details
     * @param id - ID of the vendor
     * @param vendor - The updated vendor object
     * @return - A boolean value along with a status code
     */
    @PutMapping("/{id}")
    public ResponseEntity<Boolean> updateVendorByID(@PathVariable int id, @RequestBody Vendor vendor) {
        Vendor vendor1 =  vendorService.updateVendorByID(id, vendor);

        if (vendor1 != null)
            return new ResponseEntity<>(true, HttpStatus.OK);
        else
            return new ResponseEntity<>(false, HttpStatus.NOT_FOUND);
    }

    /**
     * End point to get the number of events hosted by a vendor
     * @param id - Vendor ID
     * @return - Number of events hosted by a vendor along with a status code
     */
    @GetMapping("/events/{id}")
    public ResponseEntity<Integer> getNoOfEventsHostedByVendorID(@PathVariable int id) {
        Vendor vendor = vendorService.getVendorByID(id);

        if (vendor != null)
            return new ResponseEntity<>(vendorService.getNoOfEventsHostedByVendorID(id), HttpStatus.OK);
        else
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);

    }


}
