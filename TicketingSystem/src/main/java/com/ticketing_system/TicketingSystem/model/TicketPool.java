package com.ticketing_system.TicketingSystem.model;

import jakarta.persistence.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.locks.ReentrantLock;

@Entity
@Component
public class TicketPool {

    private static final Logger logger = LogManager.getLogger(TicketPool.class);

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int poolID;

    private String poolName;
    private int ticketPrice;

    @OneToMany(mappedBy = "ticketPool", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Ticket> tickets;

    private int maxTicketCapacity; // the max ticket capacity of the pool
    private int totalTickets;

    @Transient
    private ReentrantLock lock = new ReentrantLock(true); // Creating a fair lock

    private int ticketsSold; // Variable to keep count of how many tickets are sold
    private int ticketsBought;

    // Reference to foreign Key
    @ManyToOne
    @JoinColumn(name = "event_id") // This column holds the foreign key
    private Event event;

    @Version
    private int version; // Version field for optimistic locking

    public TicketPool(String poolName, int ticketPrice, int maxTicketCapacity, int totalTickets, Event event) {
        this.poolName = poolName;
        this.ticketPrice = ticketPrice;
        tickets = Collections.synchronizedList(new LinkedList<>());
        this.maxTicketCapacity = maxTicketCapacity;
        this.totalTickets = totalTickets;
        this.event = event;
    }

    public TicketPool() {

    }

    public int getPoolID() {
        return poolID;
    }

    public List<Ticket> getTickets() {
        return tickets;
    }

    public int getMaxTicketCapacity() {
        return maxTicketCapacity;
    }

    public int getTotalTickets() {
        return totalTickets;
    }
    public int getTicketsSold() {
        return ticketsSold;
    }

    public int getTicketsBought() {
        return ticketsBought;
    }

    public Event getEvent() {
        return event;
    }

    public Ticket addTicket() {
        lock.lock(); // lock to make the resource thread safe

        Ticket ticket = new Ticket(this);

        try {
            if (tickets.size() == maxTicketCapacity) {
                return null;
            }

            if (ticketsSold < totalTickets) {

                tickets.add(ticket);

                logger.info("Produced a ticket into : P{} total tickets produced on the pool : {}", this.poolID, this.ticketsSold);
                // This wakes up the threads that waits for this condition
                ticketsSold++;
            } else {
                logger.warn("Ticket already sold.");
                throw new InterruptedException("Ticket already sold.");
            }

        } catch (InterruptedException e) {
            Thread.currentThread().interrupt(); // Restore interrupted status
            logger.error("Vendor thread interrupted : {}", String.valueOf(e));
            return null;
        } finally {
            lock.unlock(); // releasing the lock

        }
        return ticket;
    }

    public Ticket removeTicket() {
        lock.lock();
        try {
            if (tickets.isEmpty()) {
                if (ticketsSold == totalTickets) {
                    logger.warn("All the tickets are consumed!");
                    throw new InterruptedException("Consumed all the tickets!");
                }
                return null;

            }

            if (ticketsBought < totalTickets) {
                Ticket ticket = tickets.removeFirst();
                ticketsBought++;

                logger.info("Consumed a ticket from : P{} total tickets consumed are : {}", this.poolID, ticketsBought);
                return ticket;
            } else {
                logger.warn("All the tickets are consumed!");
                throw new InterruptedException();
            }

        } catch (InterruptedException e) {
            Thread.currentThread().interrupt(); // Restore interrupted status
            logger.error("Customer thread interrupted : {}", String.valueOf(e));

        } finally {
            lock.unlock();
        }
        return null;
    }

    public int getVersion() {
        return version;
    }

    public String getPoolName() {
        return poolName;
    }

    public void setPoolName(String poolName) {
        this.poolName = poolName;
    }

    public int getTicketPrice() {
        return ticketPrice;
    }

    public void setTicketPrice(int ticketPrice) {
        this.ticketPrice = ticketPrice;
    }
}
