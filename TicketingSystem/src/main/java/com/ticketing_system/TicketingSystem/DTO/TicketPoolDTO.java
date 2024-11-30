package com.ticketing_system.TicketingSystem.DTO;

public class TicketPoolDTO {

    private int poolID;
    private String poolName;
    private int maxTicketCapacity; // the max ticket capacity of the pool
    private int totalTickets;
    private int ticketsSold; // Variable to keep count of how many tickets are sold
    private int ticketsBought;

    public TicketPoolDTO(int poolID, String poolName, int maxTicketCapacity, int totalTickets, int ticketsSold, int ticketsBought) {
        this.poolID = poolID;
        this.poolName = poolName;
        this.maxTicketCapacity = maxTicketCapacity;
        this.totalTickets = totalTickets;
        this.ticketsSold = ticketsSold;
        this.ticketsBought = ticketsBought;
    }

    public int getPoolID() {
        return poolID;
    }

    public void setPoolID(int poolID) {
        this.poolID = poolID;
    }

    public String getPoolName() {
        return poolName;
    }

    public void setPoolName(String poolName) {
        this.poolName = poolName;
    }

    public int getMaxTicketCapacity() {
        return maxTicketCapacity;
    }

    public void setMaxTicketCapacity(int maxTicketCapacity) {
        this.maxTicketCapacity = maxTicketCapacity;
    }

    public int getTotalTickets() {
        return totalTickets;
    }

    public void setTotalTickets(int totalTickets) {
        this.totalTickets = totalTickets;
    }

    public int getTicketsSold() {
        return ticketsSold;
    }

    public void setTicketsSold(int ticketsSold) {
        this.ticketsSold = ticketsSold;
    }

    public int getTicketsBought() {
        return ticketsBought;
    }

    public void setTicketsBought(int ticketsBought) {
        this.ticketsBought = ticketsBought;
    }


}



