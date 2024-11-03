package com.ticketing_system.TicketingSystem.controller;

import com.ticketing_system.TicketingSystem.model.Customer;
import com.ticketing_system.TicketingSystem.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/customers")
public class CustomerController {

    @Autowired
    private CustomerService customerService;

    // For now there is no point in returning all the customers in the DB

    @GetMapping("/{id}")
    public ResponseEntity<Customer> getCustomerByID(@PathVariable int id) {
        Customer customer = customerService.getCustomerByID(id);
        // Checking if a customer with this ID exists
        if (customer != null)
            return new ResponseEntity<>(customer, HttpStatus.OK);
        else
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    // @RequestParam to get form inputs and not JSON, if needed change to @RequestBody later
    @PostMapping("/login")
    public ResponseEntity<Boolean> loginCustomer(@RequestParam String email, @RequestParam String password) {
        boolean isAuthenticated = customerService.loginCustomer(email, password);

        if (isAuthenticated)
            return new ResponseEntity<>(true, HttpStatus.OK);
        else
            return new ResponseEntity<>(false, HttpStatus.NOT_ACCEPTABLE);
    }

    @PostMapping("/signup")
    public void signupCustomer(@RequestBody Customer customer) {
        customerService.signupCustomer(customer);
    }

}
