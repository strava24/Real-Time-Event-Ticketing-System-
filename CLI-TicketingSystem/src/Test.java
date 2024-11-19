public class Test {

    public static void main(String[] args) throws Exception {

        Configuration configuration = new Configuration(200, 1000, 3000, 100);

        ApiUtils.createNewTicketPool(configuration);

        ApiUtils.sellTicket();

    }

}
