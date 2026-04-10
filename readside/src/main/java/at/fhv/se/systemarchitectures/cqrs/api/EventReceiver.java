package at.fhv.se.systemarchitectures.cqrs.api;

import at.fhv.se.systemarchitectures.cqrs.projection.RoleProjection;
import jakarta.inject.Inject;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;

@Path("/events")
public class EventReceiver {

    @Inject
    RoleProjection projection;

    @POST
    public void receiveEvent(String event) {
        System.out.println("READSIDE RECEIVED: " + event);
        projection.handle(event);
    }
}