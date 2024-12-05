import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;

public final class ApiUtils {

    static Gson gson = new Gson();

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

        // Hardcoded values for poolName and ticketPrice
        String poolName = "VIP Pool";
        int ticketPrice = 100;

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

        // Parse the response body
        System.out.println(postResponse.body());
        Map<String, Integer> details = gson.fromJson(postResponse.body(), new TypeToken<Map<String, Integer>>() {}.getType());

        System.out.println(postResponse.body());

        poolID = details.get("poolID");
    }

    public static void createNewEvent() throws Exception {

        // Manually constructing the JSON payload as a String
        String requestBody = "{"
                + "\"eventName\":\"A.I. Meetup\","
                + "\"vendorID\":" + aiVendorID + ","
                + "\"date\":\"2025-12-15\"" + ","
                + "\"location\":\"Colombo\""
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
     * Method is used to create a new configuration
     * @param configuration the configuration object with the user data
     * @throws Exception There is a  possibility fot IOException or InterruptedException
     */
    public static void createNewConfiguration(Configuration configuration) throws Exception {
        String jsonRequest = gson.toJson(configuration);

        System.out.println(jsonRequest);

        // Saving this configuration in the DB
        HttpRequest postRequest = HttpRequest.newBuilder()
                .uri(new URI( url +"/config/configuration"))
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

    /**
     * Method is used to fetch the existing configurations on the database
     * After fetching all the configurations on the database the user will be able to choose a configuration
     * @return the configuration user choose
     * @throws Exception There is a  possibility fot IOException or InterruptedException
     */
    public static Configuration getExistingConfigurations() throws Exception {
        // Sending a get request
        HttpRequest getRequest = HttpRequest.newBuilder()
                .uri(new URI( url +"/config")) // api endpoint
                .GET() // Can get rid of this line as well, cause GET by default
                .build();

        HttpResponse<String> getResponse = HttpClient.newHttpClient().send(getRequest, HttpResponse.BodyHandlers.ofString()); // This is to accept the response as a string

        /* This line captures the generic type List<Configuration> and retrieves it as a Type object,
           which is passed to Gson to guide the deserialization process.*/
        Type listType = new TypeToken<List<Configuration>>() {}.getType();
        List<Configuration> configs = gson.fromJson(getResponse.body(), listType);

        if (configs.isEmpty()) {
            return null;
        } else {
            int index = InputValidation.getValidIndex(configs);
            return configs.get(index);
        }
    }

    /**
     * Method used to send HTTP request to the back end to place an order and book a ticket
     * @throws Exception There is a  possibility fot IOException or InterruptedException
     */
    public static void sellTicket() throws Exception {
        // Sending a get request
        HttpRequest getRequest = HttpRequest.newBuilder()
                .uri(new URI( url +"/ticket-pool/" + poolID + "/sell-ticket/" + aiVendorID)) // api endpoint
                .GET() // Can get rid of this line as well, cause GET by default
                .build();

        HttpResponse<String> getResponse = HttpClient.newHttpClient().send(getRequest, HttpResponse.BodyHandlers.ofString()); // This is to accept the response as a string

        System.out.println(getResponse.body());
    }

    /**
     *  Method used to send HTTP request to buy/remove a ticket from the ticket pool
     * @throws Exception There is a  possibility fot IOException or InterruptedException
     */
    public static void buyTicket() throws Exception {

        HttpRequest getRequest = HttpRequest.newBuilder()
                .uri(new URI(url + "/ticket-pool/" + poolID + "/buy-ticket/" + aiCustomerID))
                .GET()
                .build();

        HttpResponse<String> getResponse = HttpClient.newHttpClient().send(getRequest, HttpResponse.BodyHandlers.ofString());

        System.out.println(getResponse.body());

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
            System.out.println("Login successful.");
            Map<String, String> aiCustomerDetails = gson.fromJson(postResponse.body(), new TypeToken<Map<String, String>>() {}.getType());

            System.out.println(postResponse.body());

            aiCustomerID = Integer.parseInt(aiCustomerDetails.get("customerID"));

        } else {
            System.out.println("Creating a new account");
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

        System.out.println(postResponse.body());

        aiCustomerID = Integer.parseInt(aiCustomerDetails.get("customerID"));

        System.out.println("Successfully created customer account");

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
            System.out.println("Login successful.");
            aiVendorID = Integer.parseInt(postResponse.body());

        } else {
            System.out.println("Creating a new account");
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

        System.out.println("Successfully created vendor account");
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
                 System.out.println("Targeting even with ID " + eventID);
                 return event;
             }
         } else {
             return null;
         }
    }


}
