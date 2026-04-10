package at.fhv.se.systemarchitectures.cqrs.api;

import at.fhv.se.systemarchitectures.cqrs.eventdb.EventStoreService;
import jakarta.inject.Inject;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;

@Path("/events")
public class EventResource {

    @Inject
    EventStoreService eventStore;

    @POST
    public void receiveEvent(String event) {
        System.out.println("EVENT BUS RECEIVED: " + event);

        try {
            eventStore.appendEvent(
                    "roles-stream",
                    "RoleCreatedEvent",
                    event
            );
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}