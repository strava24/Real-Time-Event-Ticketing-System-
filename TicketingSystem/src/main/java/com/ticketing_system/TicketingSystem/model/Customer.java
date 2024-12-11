package com.ticketing_system.TicketingSystem.model;

import jakarta.persistence.*;
import org.springframework.stereotype.Component;

@Entity
@Component
public class Customer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int customerID;
    private String customerName;
    private String customerEmail;
    private String customerPassword;
    private int boughtTickets;

    public Customer(String customerName, String customerEmail, String customerPassword) {
        this.customerName = customerName;
        this.customerEmail = customerEmail;
        this.customerPassword = customerPassword;
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

    public int incrementBoughtTickets() {

       this.boughtTickets = (this.boughtTickets + 1);
       return boughtTickets;
    }

    public int getBoughtTickets() {
        return boughtTickets;
    }
}
