package com.ticketing_system.TicketingSystem.model;

import jakarta.persistence.*;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

@Entity
@Component
//@Scope("prototype")
public class TicketPool {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int TicketPoolID;

    // One ticketPool can hold multiple tickets
    @OneToMany(mappedBy = "ticketPool", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Ticket> ticketPool = Collections.synchronizedList(new LinkedList<>());

    // An event can have multiple ticket pools - Ex: early birds ticketPool, on-site ticketPool
    @ManyToOne
    @JoinColumn(name = "event_id")
    private Event event;

    private String poolName;
    private int maxTicketCapacity;

    public TicketPool(String poolName, int maxTicketCapacity) {
        this.poolName = poolName;
        this.maxTicketCapacity = maxTicketCapacity;
    }

    public TicketPool() {}

    public void addTicket(Ticket ticket) {
        synchronized (ticketPool) {
            while (ticketPool.size() >= maxTicketCapacity) {
                try {
                    System.out.println("Ticket pool is full! Waiting to add: " + ticket);
                    ticketPool.wait(); // Wait if the pool is at max capacity
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    System.out.println("Vendor interrupted");
                    return;
                }
            }
            ticketPool.add(ticket);
            System.out.println("Added: " + ticket);
            ticketPool.notifyAll(); // Notify consumers that a ticket is available
        }
    }public void addTicket() {}

    public Ticket removeTicket() {
        synchronized (ticketPool) {
            while (ticketPool.isEmpty()) {
                try {
                    System.out.println("Ticket pool is empty, waiting for tickets...");
                    ticketPool.wait(); // Wait if the pool is empty
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    System.out.println("Consumer interrupted");
                    return null;
                }
            }
            Ticket ticket = ((LinkedList<Ticket>) ticketPool).removeFirst();
            System.out.println("Purchased: " + ticket);
            ticketPool.notifyAll(); // Notify producers that there's space for more tickets
            return ticket;
        }
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

    public Event getEvent() {
        return this.event;
    }
}
