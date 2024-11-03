package com.ticketing_system.TicketingSystem.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import org.springframework.stereotype.Component;

@Entity
@Component
public class Customer implements Runnable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int customerID;
    private String customerName;
    private String customerEmail;
    private String customerPassword;
    private int noOfTicketsBought;

    public Customer(String customerName, String customerEmail, String customerPassword, int noOfTicketsBought) {
        this.customerName = customerName;
        this.customerEmail = customerEmail;
        this.customerPassword = customerPassword;
        this.noOfTicketsBought = noOfTicketsBought;
    }

    // Default constructor to satisfy spring boot
    public Customer() {}

    // Not having a setter for ID cause we're generating
    public int getCustomerID() {
        return customerID;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getCustomerEmail() {
        return customerEmail;
    }

    public void setCustomerEmail(String customerEmail) {
        this.customerEmail = customerEmail;
    }

    public String getCustomerPassword() {
        return customerPassword;
    }

    public void setCustomerPassword(String customerPassword) {
        this.customerPassword = customerPassword;
    }

    public int getNoOfTicketsBought() {
        return noOfTicketsBought;
    }

    public void setNoOfTicketsBought(int noOfTicketsBought) {
        this.noOfTicketsBought = noOfTicketsBought;
    }

    @Override
    public void run() {
        // Multi threading concepts
    }
}
