package com.ticketing_system.TicketingSystem.service;

import com.ticketing_system.TicketingSystem.model.Customer;
import com.ticketing_system.TicketingSystem.repository.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CustomerService {

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
            return customer.get();
        }
        return null;
    }

    public Customer signupCustomer(Customer customer) {
        return customerRepo.save(customer);
    }

    public Customer updateCustomerByID(int id, Customer customer) {
        return customerRepo.save(customer);
    }
}
