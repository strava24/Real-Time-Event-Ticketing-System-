package com.ticketing_system.TicketingSystem.controller;

import com.ticketing_system.TicketingSystem.model.Customer;
import com.ticketing_system.TicketingSystem.service.CustomerService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/customers")
@CrossOrigin(origins = "http://localhost:4209")
public class CustomerController {

    private static final Logger logger = LogManager.getLogger(CustomerController.class);

    @Autowired
    private CustomerService customerService;

    /**
     * End point to retrieve a customer by ID
     * @param id - customer ID
     * @return - The customer if found along with a status code
     */
    @GetMapping("/{id}")
    public ResponseEntity<Customer> getCustomerByID(@PathVariable int id) {
        Customer customer = customerService.getCustomerByID(id);
        // Checking if a customer with this ID exists
        if (customer != null) {
            logger.info("Returning customer data with ID C{}", id);
            return new ResponseEntity<>(customer, HttpStatus.OK);
        }
        else
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    /**
     * End to check if a user credentials are valid to login
     * @param email - email of the user
     * @param password - password of a user
     * @return - returns the customer if found along with a status code
     */
    @PostMapping("/login")
    public ResponseEntity<Customer> loginCustomer(@RequestParam String email, @RequestParam String password) {
        Customer customer = customerService.loginCustomer(email, password);

        if (customer != null)
            return new ResponseEntity<>(customer, HttpStatus.OK);
        else
            return new ResponseEntity<>(HttpStatus.NOT_ACCEPTABLE);
    }

    /**
     * end point to register a customer to the system
     * @param customer - Customer object
     * @return - The registered customer
     */
    @PostMapping("/signup")
    public Customer signupCustomer(@RequestBody Customer customer) {
        return customerService.signupCustomer(customer);
    }

    /**
     * End point to update a customer details
     * @param id - ID of the customer
     * @param customer - The modified customer object
     * @return - A boolean value along with a status code
     */
    @PutMapping("/{id}")
    public ResponseEntity<Boolean> updateCustomerByID(@PathVariable int id, @RequestBody Customer customer) {
        Customer customer1 =  customerService.updateCustomerByID(id, customer);

        if (customer1 != null)
            return new ResponseEntity<>(true, HttpStatus.OK);
        else
            return new ResponseEntity<>(false, HttpStatus.NOT_FOUND);

    }

}
