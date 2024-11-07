package com.ticketing_system.TicketingSystem.controller;

import com.ticketing_system.TicketingSystem.model.Vendor;
import com.ticketing_system.TicketingSystem.service.VendorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/vendors")
public class VendorController {

    @Autowired
    private VendorService vendorService;

    /**
     * This method is to return all the vendors on the database
     * The plan is to display all the available vendors so users will be able to book tickets based on vendors
     * @return all vendors on the database
     */
    @GetMapping
    public ResponseEntity<List<Vendor>> getAllVendors() {
        return new ResponseEntity<>(vendorService.getAllVendors(), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Vendor> getVendorByID(@PathVariable int id) {
        Vendor vendor = vendorService.getVendorByID(id);

        if (vendor != null)
            return new ResponseEntity<>(vendor, HttpStatus.OK);
        else
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);

    }

    @PostMapping("/login")
    public ResponseEntity<Boolean> loginVendor(@RequestParam String email, @RequestParam String password) {
        boolean isAuthenticated = vendorService.loginVendor(email, password);

        if (isAuthenticated)
            return new ResponseEntity<>(true, HttpStatus.OK);
        else
            return new ResponseEntity<>(false, HttpStatus.NOT_ACCEPTABLE);
    }

    @PostMapping("/signup")
    public void signupVendor(@RequestBody Vendor vendor) {
        vendorService.signupVendor(vendor);
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
