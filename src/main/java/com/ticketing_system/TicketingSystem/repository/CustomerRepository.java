package com.ticketing_system.TicketingSystem.repository;

import com.ticketing_system.TicketingSystem.model.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Integer> {

    /**
     * method to enable login features
     * finds the customer based on their email
     * @param customerEmail - checks if there is a record with this customerEmail
     * @return an Optional Customer if such customer is found if not returns an empty Optional
     */
    Optional<Customer> findByCustomerEmail(String customerEmail);

}
