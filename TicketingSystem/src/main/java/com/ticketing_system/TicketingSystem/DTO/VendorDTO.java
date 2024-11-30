package com.ticketing_system.TicketingSystem.DTO;

// Data Transfer object to prevent infinite recursion on the response
public class VendorDTO {

    private int vendorID;
    private String vendorName;
    private String vendorEmail;
    private String vendorPassword;
    private int ticketsSold;

    public VendorDTO(int vendorID, String vendorName, String vendorEmail, String vendorPassword, int ticketsSold) {
        this.vendorID = vendorID;
        this.vendorName = vendorName;
        this.vendorEmail = vendorEmail;
        this.vendorPassword = vendorPassword;
        this.ticketsSold = ticketsSold;

    }

    public int getVendorID() {
        return vendorID;
    }

    public void setVendorID(int vendorID) {
        this.vendorID = vendorID;
    }

    public String getVendorName() {
        return vendorName;
    }

    public void setVendorName(String vendorName) {
        this.vendorName = vendorName;
    }

    public String getVendorEmail() {
        return vendorEmail;
    }

    public void setVendorEmail(String vendorEmail) {
        this.vendorEmail = vendorEmail;
    }

    public String getVendorPassword() {
        return vendorPassword;
    }

    public void setVendorPassword(String vendorPassword) {
        this.vendorPassword = vendorPassword;
    }

    public int getTicketsSold() {
        return ticketsSold;
    }

    public void setTicketsSold(int ticketsSold) {
        this.ticketsSold = ticketsSold;
    }
}
