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

    public boolean loginCustomer(String email, String password) {
        Optional<Customer> customer = customerRepo.findByCustomerEmail(email);

        // Checking if a customer of this username and password exists and if so checking if the credentials are matching
        if (customer.isPresent()) {
            if (customer.get().getCustomerPassword().equals(password))
                return true;
        }
        return false;
    }

    public void signupCustomer(Customer customer) {
        customerRepo.save(customer);
    }
}
