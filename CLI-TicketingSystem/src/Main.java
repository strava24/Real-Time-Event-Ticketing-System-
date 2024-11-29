// This class is responsible for the automation, which would simulate the real world scenario where multiple people are trying to book a ticket

import com.google.gson.Gson;

import java.util.Scanner;

public class Main {

    // Input validations are done in a utility class called InputValidation
    static int totalTickets;
    static long ticketReleaseRate;
    static long customerRetrievalRate;
    static int maxTicketCapacity;

    static Gson gson = new Gson(); // Gson to easily serialize and deserialize JSON objects

    private static boolean isRunning = false;
    static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) throws Exception {

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
            }
        }

        ApiUtils.loginAICustomer();
        ApiUtils.loginAIVendor();
        ApiUtils.createNewEvent();
        ApiUtils.createNewTicketPool(new Configuration(totalTickets, ticketReleaseRate, customerRetrievalRate, maxTicketCapacity));

        simulationMenu();

    }

    /**
     * Method to display the main menu
     * @throws Exception There is a  possibility fot IOException or InterruptedException
     */
    public static void simulationMenu() throws Exception {
        Vendor producer = new Vendor();
        Customer consumer = new Customer();

        Thread producerThread = new Thread(producer);
        Thread consumerThread = new Thread(consumer);

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
                        producerThread = new Thread(producer);
                        consumerThread = new Thread(consumer);
                        producerThread.start();
                        consumerThread.start();

                        producerThread2.start();
                        consumerThread2.start();

                        producerThread3.start();
                        consumerThread3.start();

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
                        producerThread.interrupt();
                        consumerThread.interrupt();

                        producerThread2.interrupt();
                        consumerThread2.interrupt();

                        producerThread3.interrupt();
                        consumerThread3.interrupt();

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
     * @throws Exception There is a  possibility fot IOException or InterruptedException
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