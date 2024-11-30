package com.ticketing_system.TicketingSystem.controller;

import com.ticketing_system.TicketingSystem.DTO.VendorDTO;
import com.ticketing_system.TicketingSystem.model.Vendor;
import com.ticketing_system.TicketingSystem.service.VendorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.web.client.RestTemplateAutoConfiguration;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/vendors")
public class VendorController {

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

    @GetMapping("/{id}")
    public ResponseEntity<VendorDTO> getVendorByID(@PathVariable int id) {
        VendorDTO vendor = vendorService.getVendorDTOByID(id);

        if (vendor != null)
            return new ResponseEntity<>(vendor, HttpStatus.OK);
        else
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);

    }

    @PostMapping("/login")
    public ResponseEntity<Integer> loginVendor(@RequestParam String email, @RequestParam String password) {
        Vendor vendor = vendorService.loginVendor(email, password);

        if (vendor != null)
            return new ResponseEntity<>(vendor.getVendorID(), HttpStatus.OK);
        else
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @PostMapping("/signup")
    public ResponseEntity<Integer> signupVendor(@RequestBody Vendor vendor) {
        Vendor newVendor = vendorService.signupVendor(vendor);

        return new ResponseEntity<>(newVendor.getVendorID(), HttpStatus.OK);

    }


    // Update functionality not working
    @PutMapping("/{id}")
    public ResponseEntity<String> updateVendorByID(@PathVariable int id, @RequestBody Vendor vendor) {
        Vendor vendor1 =  vendorService.updateVendorByID(id, vendor);

        if (vendor1 != null)
            return new ResponseEntity<>("Updated vendor details!", HttpStatus.OK);
        else
            return new ResponseEntity<>("There is no such vendor!", HttpStatus.NOT_FOUND);
    }

    @GetMapping("/events/{id}")
    public ResponseEntity<Integer> getNoOfEventsHostedByVendorID(@PathVariable int id) {
        Vendor vendor = vendorService.getVendorByID(id);

        if (vendor != null)
            return new ResponseEntity<>(vendorService.getNoOfEventsHostedByVendorID(id), HttpStatus.OK);
        else
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);

    }


}
