import java.util.logging.Logger;

public class Customer implements Runnable {

    static Logger logger = Logger.getLogger(Customer.class.getName());

    private final int customerID;

    public Customer(int customerID) {
        this.customerID = customerID;
    }

    public int getCustomerID() {
        return customerID;
    }

    @Override
    public void run() {

        for (int i = 0; i < Main.totalTickets / Main.numCustomers; i++) {
            try {
                String message = ApiUtils.buyTicket();

                logger.info(message + " by C" + this.customerID + "\n");

                Thread.sleep(Main.customerRetrievalRate);

            } catch (Exception e) {
                Thread.currentThread().interrupt();
//                System.err.println("Consumer thread interrupted");
                break;
            }
        }

    }
}
