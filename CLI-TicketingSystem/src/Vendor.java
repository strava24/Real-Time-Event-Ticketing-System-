import java.util.logging.Logger;

public class Vendor implements Runnable {

    static Logger logger = Logger.getLogger(Vendor.class.getName());

    private final int vendorId;

    public Vendor(int vendorId) {
        this.vendorId = vendorId;
    }

    public int getVendorId() {
        return vendorId;
    }

    @Override
    public void run() {

        for (int i=0; i< Main.totalTickets / Main.numVendors; i++) {
            try {
                String message =  ApiUtils.sellTicket();

                logger.info(message + " by V" + this.vendorId  + "\n");

                Thread.sleep(Main.ticketReleaseRate);
            } catch (Exception e) {
                Thread.currentThread().interrupt();
                System.err.println("Producer thread interrupted");
                break;
            }
        }




    }
}
