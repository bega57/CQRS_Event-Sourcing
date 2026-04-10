package at.fhv.se.systemarchitectures.cqrs.publisher;

import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.core.MediaType;

@RegisterRestClient(configKey = "eventbus-api")
@Path("/events")
public interface EventBusClient {

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    void publishEvent(Object event);
}