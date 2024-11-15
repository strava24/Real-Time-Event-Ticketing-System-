// This class is responsible for the automation, which would simulate the real world scenario where multiple people are trying to book a ticket

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.lang.reflect.Type;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;
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

        Vendor producer = new Vendor(ticketPool);
        Customer consumer = new Customer(ticketPool);

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

        // Sending a get request
        HttpRequest getRequest = HttpRequest.newBuilder()
                .uri(new URI("http://localhost:8080/api/config")) // api endpoint
                .GET() // Can get rid of this line as well, cause GET by default
                .build();

        HttpResponse<String> getResponse = HttpClient.newHttpClient().send(getRequest, HttpResponse.BodyHandlers.ofString()); // This is to accept the response as a string

        /* This line captures the generic type List<Configuration> and retrieves it as a Type object,
           which is passed to Gson to guide the deserialization process.*/
        Type listType = new TypeToken<List<Configuration>>() {}.getType();
        List<Configuration> configs = gson.fromJson(getResponse.body(), listType);

        int index = InputValidation.getValidIndex(configs);

        totalTickets = configs.get(index).getTotalTickets();
        ticketReleaseRate = configs.get(index).getTicketReleaseRate();
        customerRetrievalRate = configs.get(index).getCustomerRetrievalRate();
        maxTicketCapacity = configs.get(index).getMaxTicketCapacity();

    }

    public static void createNewConfiguration() throws URISyntaxException, IOException, InterruptedException {
        totalTickets = InputValidation.getValidTotalTickets();
        ticketReleaseRate = InputValidation.getValidRate("release rate");
        customerRetrievalRate = InputValidation.getValidRate("retrieval rate");
        maxTicketCapacity = InputValidation.getValidMaxTicketCapacity(totalTickets);

        Configuration configuration =  new Configuration(totalTickets, ticketReleaseRate, customerRetrievalRate, maxTicketCapacity);
        String jsonRequest = gson.toJson(configuration);

        System.out.println(jsonRequest);

        // Saving this configuration in the DB
        HttpRequest postRequest = HttpRequest.newBuilder()
                .uri(new URI("http://localhost:8080/api/config/configuration"))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(jsonRequest))
                .build();

        HttpClient httpClient = HttpClient.newHttpClient();
        HttpResponse<String> postResponse =  httpClient.send(postRequest, HttpResponse.BodyHandlers.ofString());

        if (postResponse.statusCode() == 200) {
            System.out.println("Configuration added to the database.");
        } else if (postResponse.statusCode() == 400) {
            System.out.println("Configuration could not be added to the database.");
        } else {
            System.out.println("Unexpected response code: " + postResponse.statusCode());
        }

        System.out.println(postResponse.body());

    }

}