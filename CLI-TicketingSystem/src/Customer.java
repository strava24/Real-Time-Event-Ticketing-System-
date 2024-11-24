public class Customer implements Runnable {



//    public Customer(int poolId, int customerId) {
//        this.poolId = poolId;
//        this.customerId = customerId;
//    }

    @Override
    public void run() {


        while (true) {
            try {
                ApiUtils.buyTicket();

                Thread.sleep(3000);
            } catch (Exception e) {
                Thread.currentThread().interrupt();
//                System.err.println("Consumer thread interrupted");
                break;
            }
        }

    }
}
