package at.fhv.se.systemarchitectures.cqrs.subscriber;

import io.quarkus.runtime.Startup;
import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.ApplicationScoped;

import java.net.HttpURLConnection;
import java.net.URL;
import java.io.OutputStream;
import java.util.Scanner;

@ApplicationScoped
@Startup
public class SubscriptionService {

    @PostConstruct
    void init() {
        System.out.println("SUBSCRIBE STARTED");
        subscribeWithRetry();
    }

    private void subscribeWithRetry() {

        new Thread(() -> {

            int retries = 20; // mehr retries = stabiler

            while (retries > 0) {
                try {
                    System.out.println("Trying to subscribe...");

                    URL url = new URL("http://eventbus:8080/events/subscribe");
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();

                    conn.setRequestMethod("POST");
                    conn.setDoOutput(true);

                    String callback = "http://readside:8080/events";

                    conn.setRequestProperty("Content-Type", "text/plain");

                    try (OutputStream os = conn.getOutputStream()) {
                        os.write(callback.getBytes());
                        os.flush();
                    }

                    int responseCode = conn.getResponseCode();

                    if (responseCode == 200) {
                        System.out.println("ReadSide subscribed successfully!");

                        replayEvents();
                        return;
                    }

                } catch (Exception e) {
                    System.out.println("EventBus not ready yet...");
                }

                retries--;

                try {
                    Thread.sleep(3000);
                } catch (InterruptedException ignored) {}
            }

            System.out.println("FAILED TO SUBSCRIBE AFTER RETRIES");

        }).start();
    }

    private void replayEvents() {
        try {
            System.out.println("Replaying old events...");

            URL url = new URL("http://eventbus:8080/events");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();

            conn.setRequestMethod("GET");

            Scanner scanner = new Scanner(conn.getInputStream());

            while (scanner.hasNextLine()) {
                String event = scanner.nextLine();
                sendToReceiver(event);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void sendToReceiver(String event) {
        try {
            URL url = new URL("http://readside:8080/events");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();

            conn.setRequestMethod("POST");
            conn.setDoOutput(true);

            conn.setRequestProperty("Content-Type", "text/plain");

            try (OutputStream os = conn.getOutputStream()) {
                os.write(event.getBytes());
                os.flush();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}