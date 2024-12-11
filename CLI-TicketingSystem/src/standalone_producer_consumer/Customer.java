package standalone_producer_consumer;

public class Customer implements Runnable {
    private final TicketPool ticketPool;
    public Customer(TicketPool ticketPool) {
        this.ticketPool = ticketPool;
    }
    @Override
    public void run() {
        while (true) {
            Ticket ticket = ticketPool.take();
            if (ticket == null) {
                System.out.println(Thread.currentThread().getName() + " did not buy a ticket");
            }
            try {
                Thread.sleep(Main.customerRetrievalRate); // Adjust sleep time as needed
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                System.out.println("Customer thread interrupted");
                break;
            }
        }
    }
}