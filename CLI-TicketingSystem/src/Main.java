import java.util.Scanner;

public class Main {

    // Input validations are done in a utility class called InputValidation
    static int totalTickets;
    static long ticketReleaseRate;
    static long customerRetrievalRate;
    static int maxTicketCapacity;

    private static boolean isRunning = false;
    static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) throws Exception {

        ApiUtils.loginAICustomer();
        ApiUtils.loginAIVendor();

        System.out.println("How would you like to start the application?");
        System.out.println("1. Use the existing Configurations. ");
        System.out.println("2. Create a new configuration. ");
        System.out.print("Enter your choice: ");
        String choice = scanner.next();
        scanner.nextLine(); // To clean the buffer

        boolean isValid = false;

        while (!isValid) {
            switch (choice) {
                case "1":
                    getExistingConfigurations();
                    isValid = true;
                    break;
                case "2":
                    createNewConfiguration();
                    isValid = true;
                    break;
                default:
                    System.out.println("Invalid choice. Try again.");
                    System.out.print("Enter your choice: ");
                    choice = scanner.next();
            }
        }

        System.out.println(); // To maintain order
        System.out.println("1. Create an event and do the simulation.");
        System.out.println("2. Use an existing event for the simulation.");
        System.out.print("Enter your choice: ");
        choice = scanner.next();
        scanner.nextLine();

        switch (choice) {
            case "1":
                System.out.println("Creating an event");
                ApiUtils.createNewEvent();
                ApiUtils.createNewTicketPool(new Configuration(totalTickets, ticketReleaseRate, customerRetrievalRate, maxTicketCapacity));
                break;
            case "2":
                Event event = ApiUtils.getExistingEvents();
                if (event == null) {
                    System.out.println("There are no existing events.");
                    ApiUtils.createNewEvent();
                    ApiUtils.createNewTicketPool(new Configuration(totalTickets, ticketReleaseRate, customerRetrievalRate, maxTicketCapacity));
                } else {
                    TicketPool pool = ApiUtils.getExistingPool();
                    if (pool == null) {
                        System.out.println("There are no existing pools for this event.");
                        ApiUtils.createNewEvent();
                        ApiUtils.createNewTicketPool(new Configuration(totalTickets, ticketReleaseRate, customerRetrievalRate, maxTicketCapacity));
                    } else {
                        break;
                    }
                }
        }
        simulationMenu();
    }

    public static void simulationMenu() {
        Vendor producer = new Vendor();
        Customer consumer = new Customer();

        Thread producerThread1 = new Thread(producer);
        Thread consumerThread1 = new Thread(consumer);

        Thread producerThread2 = new Thread(producer);
        Thread consumerThread2 = new Thread(consumer);

        Thread producerThread3 = new Thread(producer);
        Thread consumerThread3 = new Thread(consumer);

        Thread producerThread4 = new Thread(producer);
        Thread consumerThread4 = new Thread(consumer);

        Thread producerThread5 = new Thread(producer);
        Thread consumerThread5 = new Thread(consumer);


        System.out.println(); // new line character to maintain order
        System.out.println("Type 'start' to begin the simulation, and 'stop' to end it.");

        while (true) {
            String command = scanner.nextLine().trim().toLowerCase();

            switch (command) {
                case "start":
                    if (!isRunning) {
                        isRunning = true;
                        // Create new threads for each run
                        producerThread1 = new Thread(producer);
                        consumerThread1 = new Thread(consumer);

                        producerThread2 = new Thread(producer);
                        consumerThread2 = new Thread(consumer);

                        producerThread3 = new Thread(producer);
                        consumerThread3 = new Thread(consumer);

                        producerThread4 = new Thread(producer);
                        consumerThread4 = new Thread(consumer);

                        producerThread5 = new Thread(producer);
                        consumerThread5 = new Thread(consumer);

                        consumerThread1.start();
                        producerThread1.start();
                        consumerThread2.start();
                        producerThread2.start();
                        consumerThread3.start();
                        producerThread3.start();
                        consumerThread4.start();
                        producerThread4.start();
                        consumerThread5.start();
                        producerThread5.start();


                        System.out.println("Simulation started.");
                    } else {
                        System.out.println("Simulation is already running.");
                    }
                    break;

                case "stop":
                    if (isRunning) {
                        isRunning = false;

                        consumerThread1.interrupt();
                        producerThread1.interrupt();

                        consumerThread2.interrupt();
                        producerThread2.interrupt();

                        consumerThread3.interrupt();
                        producerThread3.interrupt();

                        consumerThread4.interrupt();
                        producerThread4.interrupt();

                        consumerThread5.interrupt();
                        producerThread5.interrupt();

                        System.out.println("Stopping simulation...");
                    } else {
                        System.out.println("Simulation is not running.");
                    }
                    break;

                case "exit":
                    System.out.println("Exiting program...");
                    scanner.close();
                    System.exit(0);

                default:
                    System.out.println("Unknown command. Type 'start', 'stop', or 'exit'.");
            }
        }
    }

    /**
     * Method to get the existing configurations os the database
     * @throws Exception There is a  possibility fot IOException or InterruptedException
     */
    public static void getExistingConfigurations() throws Exception {

        Configuration configuration = ApiUtils.getExistingConfigurations();

        if (configuration != null) {
            totalTickets = configuration.getTotalTickets();
            ticketReleaseRate = configuration.getTicketReleaseRate();
            customerRetrievalRate = configuration.getCustomerRetrievalRate();
            maxTicketCapacity = configuration.getMaxTicketCapacity();
        } else {
            System.out.println("No existing configurations found.");
            System.out.println("Redirecting to create new configuration.");
            createNewConfiguration(); // Redirecting to create new configuration
        }
    }

    /**
     * Method to create new configuration
     * @throws Exception There is a possibility fot IOException or InterruptedException
     */
    public static void createNewConfiguration() throws Exception {
        totalTickets = InputValidation.getValidTotalTickets();
        ticketReleaseRate = InputValidation.getValidRate("release rate");
        customerRetrievalRate = InputValidation.getValidRate("retrieval rate");
        maxTicketCapacity = InputValidation.getValidMaxTicketCapacity(totalTickets);

        Configuration configuration =  new Configuration(totalTickets, ticketReleaseRate, customerRetrievalRate, maxTicketCapacity);

        ApiUtils.createNewConfiguration(configuration);

    }

}
