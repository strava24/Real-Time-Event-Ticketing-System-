// Temporary representation of thread pool

import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class TicketPool {

    private Queue<Ticket> tickets;
    private int max; // the max ticket capacity of the pool
    private ReentrantLock lock = new ReentrantLock(true); // Creating a fair lock

    private Condition notEmpty = lock.newCondition();
    private Condition notFull = lock.newCondition();

    public TicketPool(int max) {
        tickets = new LinkedList<Ticket>();
        this.max = max;
    }

    public void put() {
        lock.lock(); // locks to make it thread safe
        try {
            while (tickets.size() == max) {
                notFull.await(); // wait till notFull condition is true, ie threads will wait till
            }
            tickets.add(new Ticket());
            notEmpty.signalAll(); // When the producer produces a product the shared resource is not empty anymore
            // Signaling all the treads that are waiting on this condition that the condition is met

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
                notEmpty.await();
            }
            Ticket ticket = tickets.remove();
            notFull.signalAll();
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
