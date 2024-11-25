public class Vendor implements Runnable {

    @Override
    public void run() {

        while(true) {
            try {
                ApiUtils.sellTicket();

                Thread.sleep(Main.ticketReleaseRate);
            } catch (Exception e) {
                Thread.currentThread().interrupt();
                System.err.println("Producer thread interrupted");
                break;
            }
        }


    }
}
