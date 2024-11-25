public class Customer implements Runnable {

    @Override
    public void run() {

        while (true) {
            try {
                ApiUtils.buyTicket();

                Thread.sleep(Main.customerRetrievalRate);
            } catch (Exception e) {
                Thread.currentThread().interrupt();
//                System.err.println("Consumer thread interrupted");
                break;
            }
        }

    }
}
