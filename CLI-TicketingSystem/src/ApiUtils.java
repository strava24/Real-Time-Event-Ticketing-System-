import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;

public final class ApiUtils {

    static int aiVendorID;
    static Gson gson = new Gson();

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

        String url = "http://localhost:8080/api";

        String jsonRequest = gson.toJson(configuration);

        HttpRequest postRequest = HttpRequest.newBuilder()
                .uri(new URI(url + "/ticket-pool/create"))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(jsonRequest))
                .build();

        HttpClient httpClient = HttpClient.newHttpClient();
        HttpResponse<String> postResponse = httpClient.send(postRequest, HttpResponse.BodyHandlers.ofString()); // Saying that we're expecting a string in return

        aiVendorID = Integer.parseInt(postResponse.body()); // Getting the vendorId od A.I. Inc for future usage

        System.out.println(postResponse.body());

    }

    public static void createNewConfiguration(Configuration configuration) throws Exception {

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

    public static Configuration getExistingConfigurations() throws Exception {

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

        return configs.get(index);


    }

}
