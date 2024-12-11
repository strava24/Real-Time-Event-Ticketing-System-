import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Main {

    // Input validations are done in a utility class called InputValidation
    static int totalTickets;
    static long ticketReleaseRate;
    static long customerRetrievalRate;
    static int maxTicketCapacity;

    static int numVendors = 1;
    static int numCustomers = 1;

    private static boolean isRunning = false;
    static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) throws Exception {

        // Getting the bot into the system
        ApiUtils.loginAICustomer();
        ApiUtils.loginAIVendor();

        starter();
        targetEvent();
        simulationMenu();
    }

    private static void starter() throws Exception {
        List<Configuration> configs =  ApiUtils.loadConfigurations();

        if (configs.isEmpty()) {
            createNewConfiguration(); // Automatically redirecting to create an event
        } else {
            System.out.println();
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
                        getExistingConfigurations(configs);
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
        }
    }

    private static void targetEvent() throws Exception {
        String choice;
        System.out.println(); // To maintain order
        System.out.println("How would you like to target an event?");
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
                    System.out.println();
                    System.out.println("There are no existing events.");
                    ApiUtils.createNewEvent();
                    ApiUtils.createNewTicketPool(new Configuration(totalTickets, ticketReleaseRate, customerRetrievalRate, maxTicketCapacity));
                } else {
                    TicketPool pool = ApiUtils.getExistingPool();
                    if (pool == null) {
                        System.out.println("There are no existing pools for this event.");
                        ApiUtils.createNewTicketPool(new Configuration(totalTickets, ticketReleaseRate, customerRetrievalRate, maxTicketCapacity));
                    } else {
                        break;
                    }
                }
        }
    }

    public static void simulationMenu() {

        System.out.println();
        System.out.print("Enter the number of Vendor threads: ");
        numVendors = Integer.parseInt(scanner.nextLine());

        System.out.print("Enter the number of Customer threads: ");
        numCustomers = Integer.parseInt(scanner.nextLine());

        System.out.println(); // new line character to maintain order
        System.out.println("Starting simulation with " + numVendors + " vendors and " + numCustomers + " customers.");
        System.out.println("Type 'start' to begin the simulation, 'stop' to end it, or 'exit' to exit.");

        List<Thread> producerThreads = new ArrayList<>();
        List<Thread> consumerThreads = new ArrayList<>();

        while (true) {
            String command = scanner.nextLine().trim().toLowerCase();

            switch (command) {
                case "start":
                    if (!isRunning) {
                        isRunning = true;

                        producerThreads = new ArrayList<>();
                        consumerThreads = new ArrayList<>();

                        // Create and start the specified number of Vendor threads
                        for (int i = 0; i < numVendors; i++) {
                            Vendor producer = new Vendor(i);
                            Thread producerThread = new Thread(producer);
                            producerThreads.add(producerThread);
                            producerThread.start();
                        }

                        // Create and start the specified number of Customer threads
                        for (int i = 0; i < numCustomers; i++) {
                            Customer consumer = new Customer(i);
                            Thread consumerThread = new Thread(consumer);
                            consumerThreads.add(consumerThread);
                            consumerThread.start();
                        }

                        System.out.println("Simulation started with " + numVendors + " vendors and " + numCustomers + " customers.");
                    } else {
                        System.out.println("Simulation is already running.");
                    }
                    break;

                case "stop":
                    if (isRunning) {
                        isRunning = false;
                        System.out.println("Stopping simulation...");

                        // Interrupt all producer and consumer threads
                        for (Thread producerThread : producerThreads) {
                            producerThread.interrupt();
                        }
                        for (Thread consumerThread : consumerThreads) {
                            consumerThread.interrupt();
                        }

                        System.out.println("Simulation stopped.");
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

    public static void getExistingConfigurations(List<Configuration> configs) {
        int index = InputValidation.getValidIndex(configs);

        totalTickets = configs.get(index).getTotalTickets();
        ticketReleaseRate = configs.get(index).getTicketReleaseRate();
        customerRetrievalRate = configs.get(index).getCustomerRetrievalRate();
        maxTicketCapacity = configs.get(index).getMaxTicketCapacity();

        System.out.println(configs.get(index));

    }

    public static void createNewConfiguration() throws Exception {
        System.out.println();
        totalTickets = InputValidation.getValidTotalTickets();
        ticketReleaseRate = InputValidation.getValidRate("release rate");
        customerRetrievalRate = InputValidation.getValidRate("retrieval rate");
        maxTicketCapacity = InputValidation.getValidMaxTicketCapacity(totalTickets);

        Configuration configuration =  new Configuration(totalTickets, ticketReleaseRate, customerRetrievalRate, maxTicketCapacity);

        ApiUtils.saveConfigurations(configuration);
    }

}
