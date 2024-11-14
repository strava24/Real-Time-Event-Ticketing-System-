// Temporary representation of thread pool

import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class TicketPool {

    private Queue<Ticket> tickets;

    private int maxTicketCapacity; // the max ticket capacity of the pool
    private int totalTickets;

    private ReentrantLock lock = new ReentrantLock(true); // Creating a fair lock
    private Condition notEmpty = lock.newCondition();
    private Condition notFull = lock.newCondition();

    private int ticketsSold;
    private int ticketsBought;

    public TicketPool(int maxTicketCapacity, int totalTickets) {
        tickets = new LinkedList<Ticket>();
        this.maxTicketCapacity = maxTicketCapacity;
        this.totalTickets = totalTickets;
    }

    public void put() {
        lock.lock(); // locks to make it thread safe
        try {
            while (tickets.size() == maxTicketCapacity) {
                notFull.await(); // wait till notFull condition is true, ie threads will wait till
            }
            if (ticketsSold < totalTickets) {
                tickets.add(new Ticket());
                System.out.println("Produced a ticket." + ticketsSold);
                notEmpty.signalAll(); // When the producer produces a product the shared resource is not empty anymore
                // Signaling all the treads that are waiting on this condition that the condition is met
                ticketsSold++; // Incrementing the no of tickets sold
            } else {
                System.out.println("Produced all the tickets!");
                Thread.currentThread().interrupt();
            }

        } catch (InterruptedException e) {
            Thread.currentThread().interrupt(); // Restore interrupted status
            System.err.println("Producer thread interrupted");
        } finally {
            lock.unlock();
        }

    }

    public Ticket take() {
        lock.lock();
        try {
            while (tickets.isEmpty()) { // having while instead of if, to avoid NullPointers
                if (ticketsSold == totalTickets) {
                    System.out.println("Consumed all the tickets!");
                    throw new InterruptedException();
                }

                notEmpty.await();

            }
            if (ticketsBought < totalTickets) {
                Ticket ticket = tickets.remove();
                ticketsBought++;
                notFull.signalAll();
                System.out.println("Consumed a ticket: " + ticket);
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
