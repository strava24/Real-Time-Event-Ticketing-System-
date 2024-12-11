import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Type;
import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.logging.Logger;

public final class ApiUtils {

    static Logger logger = Logger.getLogger(ApiUtils.class.getName());

    static Gson gson = new Gson();
    static Scanner input = new Scanner(System.in);

//    private static final Logger logger = LogManager.getLogger(ApiUtils.class);


    static int poolID;
    static int aiCustomerID;
    static int aiVendorID;
    static int eventID;

    static String url = "http://localhost:8080/api";

    private ApiUtils() {}

    /**
     * this method is to create a ticket pool on the backend
     * There should be an event that holds the ticket pool
     * in this case since these are all simulation and automations, there is a default vendor on the backend
     * The vendor is called A.I. Inc. In the back ends POV it will be this vendor trying to create a ticket pool
     * @param configuration - A configuration object to create the pool
     * @throws Exception - There is a  possibility fot IOException or InterruptedException
     */
    public static void createNewTicketPool(Configuration configuration) throws Exception{

        System.out.println();
        System.out.print("Enter Ticket pool Name : ");
        String poolName = input.nextLine();
        int ticketPrice = InputValidation.getValidTicketPrice();

        // Construct the URL with query parameters
        String requestUrl = String.format("%s/ticket-pool/create/%d?poolName=%s&ticketPrice=%d&maxTicketCapacity=%d&totalTickets=%d",
                url,
                eventID,
                URLEncoder.encode(poolName, StandardCharsets.UTF_8),
                ticketPrice,
                configuration.getMaxTicketCapacity(),
                configuration.getTotalTickets());

        HttpRequest postRequest = HttpRequest.newBuilder()
                .uri(new URI(requestUrl))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.noBody())
                .build();

        HttpClient httpClient = HttpClient.newHttpClient();
        HttpResponse<String> postResponse = httpClient.send(postRequest, HttpResponse.BodyHandlers.ofString());

        Map<String, Integer> details = gson.fromJson(postResponse.body(), new TypeToken<Map<String, Integer>>() {}.getType());

        logger.info(details.toString());

        poolID = details.get("poolID");
    }

    public static void createNewEvent() throws Exception {

        System.out.println();
        System.out.print("Enter the event name : ");
        String eventName = input.nextLine();
        System.out.print("Enter the event location : ");
        String eventLocation = input.nextLine();

        String eventDate = InputValidation.getValidEventDate();


        // Manually constructing the JSON payload as a String
        String requestBody = "{"
                + "\"eventName\":\"" + eventName + "\","
                + "\"vendorID\":" + aiVendorID + ","
                + "\"date\":\"" + eventDate + "\","
                + "\"location\":\"" + eventLocation + "\""
                + "}";

        // Creating the POST request
        HttpRequest postRequest = HttpRequest.newBuilder()
                .uri(new URI(url + "/events/create"))
                .header("Content-Type", "application/json") // Use application/json for JSON payload
                .POST(HttpRequest.BodyPublishers.ofString(requestBody))
                .build();

        HttpResponse<String> postResponse = HttpClient.newHttpClient()
                .send(postRequest, HttpResponse.BodyHandlers.ofString());

        if (postResponse.statusCode() == 200) {
            eventID = Integer.parseInt(postResponse.body());
            System.out.println("Event created successfully with Event ID: " + eventID);
        } else {
            System.out.println("Error occurred: " + postResponse.statusCode());
            System.out.println("Response body: " + postResponse.body());
        }

    }

    /**
     * Method used to send HTTP request to the back end to place an order and book a ticket
     * @throws Exception There is a  possibility fot IOException or InterruptedException
     */
    public static String sellTicket() throws Exception {
        // Sending a get request
        HttpRequest getRequest = HttpRequest.newBuilder()
                .uri(new URI( url +"/ticket-pool/" + poolID + "/sell-ticket/" + aiVendorID)) // api endpoint
                .GET() // Can get rid of this line as well, cause GET by default
                .build();

        HttpResponse<String> getResponse = HttpClient.newHttpClient().send(getRequest, HttpResponse.BodyHandlers.ofString()); // This is to accept the response as a string

        return getResponse.body();
    }

    /**
     *  Method used to send HTTP request to buy/remove a ticket from the ticket pool
     * @throws Exception There is a  possibility fot IOException or InterruptedException
     */
    public static String buyTicket() throws Exception {

        HttpRequest getRequest = HttpRequest.newBuilder()
                .uri(new URI(url + "/ticket-pool/" + poolID + "/buy-ticket/" + aiCustomerID))
                .GET()
                .build();

        HttpResponse<String> getResponse = HttpClient.newHttpClient().send(getRequest, HttpResponse.BodyHandlers.ofString());

        return (getResponse.body());

    }

    /**
     * Method used to log in the default customer
     * If the default customer is not logged in, it will redirect to sign up the default customer
     * @throws Exception There is a  possibility fot IOException or InterruptedException
     */
    public static void loginAICustomer() throws Exception {

        String requestBody = "email=ai@gmail.com&password=ai";

        // Create the POST request
        HttpRequest postRequest = HttpRequest.newBuilder()
                .uri(new URI(url + "/customers/login"))
                .header("Content-Type", "application/x-www-form-urlencoded")
                .POST(HttpRequest.BodyPublishers.ofString(requestBody))
                .build();

        HttpResponse<String> postResponse = HttpClient.newHttpClient()
                .send(postRequest, HttpResponse.BodyHandlers.ofString());

        if (postResponse.statusCode() == 200) {

            Map<String, String> aiCustomerDetails = gson.fromJson(postResponse.body(), new TypeToken<Map<String, String>>() {}.getType());

            logger.info("Login successful as a customer : " + postResponse.body() + "\n");

            aiCustomerID = Integer.parseInt(aiCustomerDetails.get("customerID"));

        } else {
            System.out.println("Creating a new account as a customer.");
            signupAICustomer();
        }

    }

    /**
     * Method to sign up the default customer
     * @throws Exception There is a  possibility fot IOException or InterruptedException
     */
    public static void signupAICustomer() throws Exception {

        JsonObject requestBody = new JsonObject();
        requestBody.addProperty("customerName", "A.I.");
        requestBody.addProperty("customerEmail", "ai@gmail.com");
        requestBody.addProperty("customerPassword", "ai");

        // Convert the JSON object to a string
        String jsonRequestBody = requestBody.toString();

        // Create the POST request
        HttpRequest postRequest = HttpRequest.newBuilder()
                .uri(new URI(url + "/customers/signup"))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(jsonRequestBody))
                .build();

        // Send the request and get the response
        HttpResponse<String> postResponse = HttpClient.newHttpClient()
                .send(postRequest, HttpResponse.BodyHandlers.ofString());

        Map<String, String> aiCustomerDetails = gson.fromJson(postResponse.body(), new TypeToken<Map<String, String>>() {}.getType());

        aiCustomerID = Integer.parseInt(aiCustomerDetails.get("customerID"));

        logger.info("Signup successful as a customer : " + postResponse.body() + "\n");

    }

    public static void loginAIVendor() throws Exception {
        String requestBody = "email=ai@gmail.com&password=ai";

        // Create the POST request
        HttpRequest postRequest = HttpRequest.newBuilder()
                .uri(new URI(url + "/vendors/login"))
                .header("Content-Type", "application/x-www-form-urlencoded")
                .POST(HttpRequest.BodyPublishers.ofString(requestBody))
                .build();

        // Send the request and get the response
        HttpResponse<String> postResponse = HttpClient.newHttpClient()
                .send(postRequest, HttpResponse.BodyHandlers.ofString());

        if (postResponse.statusCode() == 200) {
            logger.info("Login successful as a vendor with ID: V" + postResponse.body() + "\n");
            aiVendorID = Integer.parseInt(postResponse.body());

        } else {
            System.out.println("Creating a new account as a vendor");
            signupAIVendor();
        }
    }

    public static void signupAIVendor() throws Exception {
        JsonObject requestBody = new JsonObject();
        requestBody.addProperty("vendorName", "A.I.");
        requestBody.addProperty("vendorEmail", "ai@gmail.com");
        requestBody.addProperty("vendorPassword", "ai");

        // Convert the JSON object to a string
        String jsonRequestBody = requestBody.toString();

        // Create the POST request
        HttpRequest postRequest = HttpRequest.newBuilder()
                .uri(new URI(url + "/vendors/signup"))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(jsonRequestBody))
                .build();

        // Send the request and get the response
        HttpResponse<String> postResponse = HttpClient.newHttpClient()
                .send(postRequest, HttpResponse.BodyHandlers.ofString());

        aiVendorID = Integer.parseInt(postResponse.body());

        logger.info("Signup successful as a vendor : " + postResponse.body() + "\n");
    }

    public static TicketPool getExistingPool() throws Exception{
        HttpRequest getRequest = HttpRequest.newBuilder()
                // Have to update the event ID
                .uri(new URI( url +"/ticket-pool/" + eventID)) // api endpoint
                .GET() // Can get rid of this line as well, cause GET by default
                .build();

        HttpResponse<String> getResponse = HttpClient.newHttpClient().send(getRequest, HttpResponse.BodyHandlers.ofString()); // This is to accept the response as a string

        if (getResponse.statusCode() == 200) {
            /* This line captures the generic type List<TicketPool> and retrieves it as a Type object,
           which is passed to Gson to guide the deserialization process.*/
            Type listType = new TypeToken<List<TicketPool>>() {}.getType();
            List<TicketPool> pools = gson.fromJson(getResponse.body(), listType);

            if (pools.isEmpty()) {
                return null;
            } else {
                int index = InputValidation.getValidIndex(pools);
                TicketPool pool = pools.get(index);
                poolID = pool.getPoolID();
                return pool;
            }
        } else {
            return null;
        }
    }

    public static Event getExistingEvents() throws Exception {
        HttpRequest getRequest = HttpRequest.newBuilder()
                // Have to update the event ID
                .uri(new URI( url +"/events")) // api endpoint
                .GET() // Can get rid of this line as well, cause GET by default
                .build();

        HttpResponse<String> getResponse = HttpClient.newHttpClient().send(getRequest, HttpResponse.BodyHandlers.ofString()); // This is to accept the response as a string

         if (getResponse.statusCode() == 200) {
            /* This line captures the generic type List<Event> and retrieves it as a Type object,
           which is passed to Gson to guide the deserialization process.*/
             Type listType = new TypeToken<List<Event>>() {}.getType();
             List<Event> events = gson.fromJson(getResponse.body(), listType);

             if (events.isEmpty()) {
                 return null;
             } else {
                 int index = InputValidation.getValidIndex(events);
                 Event event = events.get(index);
                 eventID = event.getEventID();
                 System.out.println("Targeting event with ID: E" + eventID);
                 return event;
             }
         } else {
             return null;
         }
    }

    public static List<Configuration> loadConfigurations() {
        // Read from JSON File
        try (FileReader reader = new FileReader("configs.json")) {
            Type listType = new TypeToken<List<Configuration>>() {}.getType();
            List<Configuration> loadedData = gson.fromJson(reader, listType);

            if (loadedData == null) {
                return new ArrayList<>();
            }

            return loadedData;
        } catch (Exception e) {
            return new ArrayList<>();
        }
    }

    public static void saveConfigurations(Configuration configuration) {

        List<Configuration> configs = loadConfigurations();

        configs.add(configuration);

        // Write to JSON File
        try (FileWriter writer = new FileWriter("configs.json")) {
            gson.toJson(configs, writer);
            System.out.println("Data saved to configs.json");
        } catch (IOException e) {
            System.out.println("There was an issue while saving to the file!");
        }

    }
}
