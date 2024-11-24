public class SampleVendor implements Runnable{

    private final TicketPool ticketPool;

    public SampleVendor(TicketPool ticketPool) {
        this.ticketPool = ticketPool;
    }


    @Override
    public void run() {
        while (true) {
            ticketPool.put();
//            System.out.println("Produced a ticket.");

            try {
                Thread.sleep(Main.ticketReleaseRate);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                System.err.println("Producer thread interrupted");
                break;
            }
        }
    }
}
