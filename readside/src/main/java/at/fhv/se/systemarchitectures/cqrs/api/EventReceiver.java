package at.fhv.se.systemarchitectures.cqrs.api;

import at.fhv.se.systemarchitectures.cqrs.projection.RoleProjection;
import at.fhv.se.systemarchitectures.cqrs.subscriber.SubscriptionService;
import jakarta.inject.Inject;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;

@Path("/events")
public class EventReceiver {

    @Inject
    RoleProjection projection;

    @Inject
    SubscriptionService subscriptionService;

    @POST
    public void receiveEvent(String event) {
        System.out.println("READSIDE RECEIVED: " + event);
        projection.handle(event);
    }
}