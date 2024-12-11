package com.ticketing_system.TicketingSystem.service;

import com.ticketing_system.TicketingSystem.model.Customer;
import com.ticketing_system.TicketingSystem.repository.CustomerRepository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CustomerService {

    private static final Logger logger = LogManager.getLogger(CustomerService.class);

    @Autowired
    CustomerRepository customerRepo;

    public Customer getCustomerByID(int id) {
        return customerRepo.findById(id).orElse(null);
    }

    /**
     * Method to login a user to the system
     * The login feature requires to validate based on the email and password
     * @param email - The email of the registered user
     * @param password - Password of the registered user
     * @return the customer object if the customer exists else returns null
     */
    public Customer loginCustomer(String email, String password) {
        Optional<Customer> customer = customerRepo.findByCustomerEmail(email);

        // Checking if a customer of this email exists and if so checking if the credentials are matching
        if (customer.isPresent() && customer.get().getCustomerPassword().equals(password)) {
            logger.info("Customer logged in C{}", customer.get().getCustomerID());
            return customer.get();
        }
        return null;
    }

    /**
     * Method to register a customer to the system
     * @param customer - Custoemr object
     * @return - The registered customer
     */
    public Customer signupCustomer(Customer customer) {
        logger.info("New customer named {} signed in", customer.getCustomerName());
        return customerRepo.save(customer);
    }

    /**
     * Method to update customer details
     * @param id - Customer ID
     * @param customer - Modified customer Object
     * @return - Updated customer
     */
    public Customer updateCustomerByID(int id, Customer customer) {
        return customerRepo.save(customer);
    }
}
