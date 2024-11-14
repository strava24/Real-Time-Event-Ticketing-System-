// This class is responsible for the automation, which would simulate the real world scenario where multiple people are trying to book a ticket

import java.util.Scanner;

public class Main {

    // Input validations are done in a utility class called InputValidation
    static int totalTickets = InputValidation.getValidTotalTickets();
    static long ticketReleaseRate = InputValidation.getValidRate("release rate");
    static long customerRetrievalRate = InputValidation.getValidRate("retrieval rate");
    static int maxTicketCapacity = InputValidation.getValidMaxTicketCapacity(totalTickets);

    private static Thread producerThread;
    private static Thread consumerThread;
    private static boolean isRunning = false;


    public static void main(String[] args) {
        TicketPool ticketPool = new TicketPool(maxTicketCapacity); // Set max capacity to 10

        Vendor producer = new Vendor(ticketPool);
        Customer consumer = new Customer(ticketPool);

        producerThread = new Thread(producer);
        consumerThread = new Thread(consumer);

        Scanner scanner = new Scanner(System.in);

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
}