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

        simulationMenu();

    }

    public static void simulationMenu() {
        TicketPool ticketPool = new TicketPool(maxTicketCapacity, totalTickets);

        SampleVendor producer = new SampleVendor(ticketPool);
        SampleCustomer consumer = new SampleCustomer(ticketPool);

        Thread producerThread = new Thread(producer);
        Thread consumerThread = new Thread(consumer);

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

    public static void getExistingConfigurations() throws Exception {

        Configuration configuration = ApiUtils.getExistingConfigurations();

        totalTickets = configuration.getTotalTickets();
        ticketReleaseRate = configuration.getTicketReleaseRate();
        customerRetrievalRate = configuration.getCustomerRetrievalRate();
        maxTicketCapacity = configuration.getMaxTicketCapacity();

    }

    public static void createNewConfiguration() throws Exception {
        totalTickets = InputValidation.getValidTotalTickets();
        ticketReleaseRate = InputValidation.getValidRate("release rate");
        customerRetrievalRate = InputValidation.getValidRate("retrieval rate");
        maxTicketCapacity = InputValidation.getValidMaxTicketCapacity(totalTickets);

        Configuration configuration =  new Configuration(totalTickets, ticketReleaseRate, customerRetrievalRate, maxTicketCapacity);

        ApiUtils.createNewConfiguration(configuration);

    }

}