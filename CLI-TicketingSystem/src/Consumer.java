import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class Consumer implements Runnable {


    @Override
    public void run() {

        while (true) {
            try {
                // Simulate selling a ticket
                HttpClient client = HttpClient.newHttpClient();
                HttpRequest request = HttpRequest.newBuilder()
                        .uri(URI.create("http://localhost:8080/buyTicket")) // For now just a dummy URL
                        .GET()
                        .build();
                HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
                System.out.println("Consumer bought ticket: " + response.body());

                // Add random delay
                Thread.sleep(Main.customerRetrievalRate);
            } catch (Exception e) {
                Thread.currentThread().interrupt();
//                System.err.println("Consumer thread interrupted");
                break;
            }
        }

    }
}
