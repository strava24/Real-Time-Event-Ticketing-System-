public class Test {

    public static void main(String[] args) throws Exception {

        Configuration configuration = new Configuration(10, 1000, 3000, 5);

        ApiUtils.loginAI();
        ApiUtils.createNewTicketPool(configuration);

        Vendor vendor = new Vendor();
        Customer customer = new Customer();

        Thread vendorThread = new Thread(vendor);
        Thread customerThread = new Thread(customer);
        Thread customerThread2 = new Thread(customer);

        vendorThread.start();
        customerThread.start();
        customerThread2.start();

    }

}
