package at.fhv.se.systemarchitectures.cqrs.eventdb;

import com.eventstore.dbclient.*;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.ArrayList;
import java.util.List;

@ApplicationScoped
public class EventStoreService {

    private final EventStoreDBClient client;

    public EventStoreService() {
        EventStoreDBClientSettings settings =
                EventStoreDBConnectionString.parseOrThrow(
                        "esdb://eventdb:2113?tls=false"
                );

        this.client = EventStoreDBClient.create(settings);
    }

    public void appendEvent(String stream, String eventType, String jsonData) throws Exception {
        EventData event = EventData.builderAsJson(
                eventType,
                jsonData.getBytes()
        ).build();

        client.appendToStream(stream, event).get();
    }


    public List<String> getAllEvents() {
        List<String> events = new ArrayList<>();

        try {
            ReadStreamOptions options = ReadStreamOptions.get()
                    .forwards()
                    .fromStart();

            ReadResult result = client.readStream("roles-stream", options).get();

            for (ResolvedEvent event : result.getEvents()) {
                String json = new String(event.getEvent().getEventData());
                events.add(json);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        System.out.println("No events yet (stream not created)");

        return events;
    }
}