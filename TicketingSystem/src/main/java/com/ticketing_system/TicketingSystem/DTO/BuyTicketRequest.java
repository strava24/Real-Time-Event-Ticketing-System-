package com.ticketing_system.TicketingSystem.DTO;

public class BuyTicketRequest {

    private int poolId;
    private int customerId;

    public BuyTicketRequest(int poolId, int customerId) {
        this.poolId = poolId;
        this.customerId = customerId;
    }

    public BuyTicketRequest() {}

    public int getPoolId() {
        return poolId;
    }

    public void setPoolId(int poolId) {
        this.poolId = poolId;
    }

    public int getCustomerId() {
        return customerId;
    }

    public void setCustomerId(int customerId) {
        this.customerId = customerId;
    }
}
