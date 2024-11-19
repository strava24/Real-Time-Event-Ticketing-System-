import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class Producer implements Runnable {

    @Override
    public void run() {

        while(true) {
            try {
                // Simulate buying a ticket
                HttpClient client = HttpClient.newHttpClient();
                HttpRequest request = HttpRequest.newBuilder()
                        .uri(URI.create("http://localhost:8080/sellTicket")) // For now just an endpoint
                        .GET()
                        .build();
                HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
                System.out.println("Producer added ticket: " + response.body());

                Thread.sleep(Main.ticketReleaseRate);
            } catch (Exception e) {
                Thread.currentThread().interrupt();
                System.err.println("Producer thread interrupted");
                break;
            }
        }


    }
}
