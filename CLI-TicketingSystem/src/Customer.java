public class Customer implements Runnable {

    private final TicketPool ticketPool;

    public Customer(TicketPool ticketPool) {
        this.ticketPool = ticketPool;
    }

    @Override
    public void run() {
        while (true) {
            Ticket ticket = ticketPool.take();
            if (ticket != null) {
//                System.out.println("Consumed a ticket: " + ticket);
            }

            try {
                Thread.sleep(Main.customerRetrievalRate);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
//                System.err.println("Consumer thread interrupted");
                break;
            }
        }
    }
}
