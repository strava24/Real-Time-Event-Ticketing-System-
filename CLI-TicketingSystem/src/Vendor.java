public class Vendor implements Runnable{

    private final TicketPool ticketPool;

    public Vendor(TicketPool ticketPool) {
        this.ticketPool = ticketPool;
    }


    @Override
    public void run() {
        while (true) {
            ticketPool.put();
            System.out.println("Produced a ticket.");

            try {
                Thread.sleep(Main.ticketReleaseRate); // Adjust sleep time as needed
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                System.out.println("Producer thread interrupted");
                break;
            }
        }
    }
}
