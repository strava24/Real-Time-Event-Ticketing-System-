package com.ticketing_system.TicketingSystem.model;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class TicketPool {

    private List<Ticket> tickets;

    private int maxTicketCapacity; // the max ticket capacity of the pool
    private int totalTickets;

    private ReentrantLock lock = new ReentrantLock(true); // Creating a fair lock
    private Condition notEmpty = lock.newCondition();
    private Condition notFull = lock.newCondition();

    private int ticketsSold; // Variable to keep count of how many tickets are sold
    private int ticketsBought;

    public TicketPool(int maxTicketCapacity, int totalTickets) {
        tickets = Collections.synchronizedList(new LinkedList<>());
        this.maxTicketCapacity = maxTicketCapacity;
        this.totalTickets = totalTickets;
    }

    public void addTicket() {
        lock.lock(); // lock to make the resource thread safe
        try {
            while (tickets.size() == maxTicketCapacity) {
                notFull.await();
            }

            if (ticketsSold < totalTickets) {
                tickets.add(new Ticket());
                System.out.println("Produced a ticket." + ticketsSold);
                notEmpty.signalAll(); // Signalling the other threads that the pool is not empty anymore
                // This wakes up the threads that waits for this condition
                ticketsSold++;
            }

        } catch (InterruptedException e) {
            Thread.currentThread().interrupt(); // Restore interrupted status
            System.err.println("Producer thread interrupted");
        } finally {
            lock.unlock(); // releasing the lock
        }
    }

    public Ticket removeTicket() {
        lock.lock();
        try {
            while (tickets.isEmpty()) {
                if (ticketsSold == totalTickets) {
                    System.out.println("Consumed all the tickets!");
                    throw new InterruptedException();
                }

                notFull.await();

            }

            if (ticketsBought < totalTickets) {
                Ticket ticket = tickets.removeFirst();
                ticketsBought++;
                notEmpty.signalAll();

                System.out.println("Consumed a ticket." + ticket);
                return ticket;
            } else {
                System.out.println("Consumed all the tickets!");
                throw new InterruptedException();
            }

        } catch (InterruptedException e) {
            Thread.currentThread().interrupt(); // Restore interrupted status
            System.err.println("Consumer thread interrupted");
        } finally {
            lock.unlock();
        }
        return null;
    }

}
