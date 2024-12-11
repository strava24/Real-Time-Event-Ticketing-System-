package standalone_producer_consumer;

import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class TicketPool {
    private Queue<Ticket> tickets;
    private final int max; // the max ticket capacity of the pool
    private final ReentrantLock lock = new ReentrantLock(true); // Creating a fair lock
    private final Condition notEmpty = lock.newCondition();
    private final Condition notFull = lock.newCondition();
    public TicketPool(int max) {
        tickets = new LinkedList<Ticket>();
        this.max = max;
    }
    public void put(Ticket ticket) {
        lock.lock(); // locks to make it thread safe
        try {
            while (tickets.size() == max) {
                notFull.await(); // wait till notFull condition is true, ie threads will wait till
            }
            tickets.add(ticket);
            System.out.println("Ticket produced by Vendor" + Thread.currentThread().getName() + ": capacity : " + tickets.size());
            notEmpty.signalAll(); // When the producer produces a product the shared resource is not empty anymore
            // Signaling all the treads that are waiting on this condition that the condition is met
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt(); // Restore interrupted status
            System.err.println("Vendor thread interrupted");
        } finally {
            lock.unlock();
        }
    }
    public Ticket take() {
        lock.lock();
        try {
            while (tickets.isEmpty()) { // having while instead of if, to avoid NullPointers
                notEmpty.await();
            }
            Ticket ticket = tickets.remove();
            notFull.signalAll();
            System.out.println("Ticket removed by Customer" + Thread.currentThread().getName() + ": capacity : " + tickets.size());
            return ticket;
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt(); // Restore interrupted status
            System.err.println("Consumer thread interrupted");
        } finally {
            lock.unlock();
        }
        return null;
    }
}