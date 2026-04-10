package at.fhv.se.systemarchitectures.cqrs.subscriber;

import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.ApplicationScoped;

import java.net.HttpURLConnection;
import java.net.URL;
import java.io.OutputStream;

@ApplicationScoped
public class SubscriptionService {

    @PostConstruct
    void subscribe() {
        new Thread(() -> {
            int retries = 10;

            while (retries > 0) {
                try {
                    System.out.println("Trying to subscribe...");

                    URL url = new URL("http://eventbus:8080/events/subscribe");
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();

                    conn.setRequestMethod("POST");
                    conn.setDoOutput(true);

                    String callback = "http://readside:8080/events";

                    try (OutputStream os = conn.getOutputStream()) {
                        os.write(callback.getBytes());
                    }

                    int responseCode = conn.getResponseCode();

                    if (responseCode == 200) {
                        System.out.println("ReadSide subscribed successfully!");
                        break;
                    }

                } catch (Exception e) {
                    System.out.println("Subscription failed, retrying...");
                }

                retries--;

                try {
                    Thread.sleep(3000);
                } catch (InterruptedException ignored) {}
            }

            if (retries == 0) {
                System.out.println("Failed to subscribe after retries!");
            }

        }).start();
    }
}