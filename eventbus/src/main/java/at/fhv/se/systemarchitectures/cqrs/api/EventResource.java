package at.fhv.se.systemarchitectures.cqrs.api;

import at.fhv.se.systemarchitectures.cqrs.eventdb.EventStoreService;
import jakarta.inject.Inject;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.core.Response;

import java.util.ArrayList;
import java.util.List;
import java.net.HttpURLConnection;
import java.net.URL;
import java.io.OutputStream;

@Path("/events")
public class EventResource {

    @Inject
    EventStoreService eventStore;

    private static final List<String> subscribers = new ArrayList<>();

    @POST
    @Path("/subscribe")
    public Response subscribe(String callbackUrl) {
        if (!subscribers.contains(callbackUrl)) {
            subscribers.add(callbackUrl);
        }
        return Response.ok().build();
    }

    @POST
    public void receiveEvent(String event) {
        System.out.println("EVENT BUS RECEIVED: " + event);

        try {
            eventStore.appendEvent(
                    "roles-stream",
                    "RoleCreatedEvent",
                    event);

            for (String subscriber : subscribers) {
                sendEventToSubscriber(subscriber, event);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void sendEventToSubscriber(String subscriberUrl, String event) {
        try {
            URL url = new URL(subscriberUrl);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();

            conn.setRequestMethod("POST");
            conn.setDoOutput(true);
            conn.setRequestProperty("Content-Type", "application/json");

            try (OutputStream os = conn.getOutputStream()) {
                os.write(event.getBytes());
                os.flush();
            }

            int responseCode = conn.getResponseCode();
            System.out.println("Sent to " + subscriberUrl + " -> Status: " + responseCode);

            System.out.println("Event sent to: " + subscriberUrl);

        } catch (Exception e) {
            System.out.println("Failed to send to: " + subscriberUrl);
            e.printStackTrace();
        }
    }
}