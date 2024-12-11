package com.ticketing_system.TicketingSystem.DTO;

public class SellTicketRequest {

    private int vendorId;
    private int poolId;

    public SellTicketRequest(int vendorId, int poolId) {
        this.vendorId = vendorId;
        this.poolId = poolId;
    }

    public SellTicketRequest() {
    }

    public int getVendorId() {
        return vendorId;
    }

    public void setVendorId(int vendorId) {
        this.vendorId = vendorId;
    }

    public int getPoolId() {
        return poolId;
    }

    public void setPoolId(int poolId) {
        this.poolId = poolId;
    }
}
