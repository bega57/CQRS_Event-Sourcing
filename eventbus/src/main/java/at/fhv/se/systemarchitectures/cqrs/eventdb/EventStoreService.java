package at.fhv.se.systemarchitectures.cqrs.eventdb;

import com.eventstore.dbclient.*;
import jakarta.enterprise.context.ApplicationScoped;

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
}