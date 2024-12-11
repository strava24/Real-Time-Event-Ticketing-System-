package standalone_producer_consumer;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Main {
    // Input validations are done in a utility class called InputValidation
    static int totalTickets = InputValidation.getValidTotalTickets();
    static long ticketReleaseRate = InputValidation.getValidRate("release rate");
    static long customerRetrievalRate = InputValidation.getValidRate("retrieval rate");
    static int maxTicketCapacity = InputValidation.getValidMaxTicketCapacity(totalTickets);
    private static boolean isRunning = false;

    static Scanner scanner = new Scanner(System.in);


    public static void main(String[] args) {
        TicketPool ticketPool = new TicketPool(maxTicketCapacity); // Set max capacity to 10

        System.out.println();
        int numVendors = 5;
        int numCustomers = 5;

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
                            Vendor producer = new Vendor(ticketPool);
                            Thread producerThread = new Thread(producer);
                            producerThreads.add(producerThread);
                            producerThread.start();
                        }

                        // Create and start the specified number of Customer threads
                        for (int i = 0; i < numCustomers; i++) {
                            Customer consumer = new Customer(ticketPool);
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

}