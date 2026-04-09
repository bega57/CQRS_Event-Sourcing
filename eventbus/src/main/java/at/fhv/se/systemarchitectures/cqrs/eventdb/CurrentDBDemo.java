package at.fhv.se.systemarchitectures.cqrs.eventdb;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.json.JsonMapper;
import io.kurrent.dbclient.*;
import io.quarkus.logging.Log;
import io.quarkus.runtime.Startup;
import jakarta.enterprise.context.ApplicationScoped;

import java.io.IOException;
import java.util.concurrent.ExecutionException;
import java.util.stream.Stream;

@ApplicationScoped
public class CurrentDBDemo {

    public static class DemoEvent {
        private String text;

        public DemoEvent() { }

        public DemoEvent(String text) {
            this.text = text;
        }

        public String getText() {
            return text;
        }

        public void setText(String text) {
            this.text = text;
        }

        @Override
        public String toString() {
            return "DemoEvent{" +
                    "text='" + text + '\'' +
                    '}';
        }
    }

    @Startup
    public void demonstrate() throws ExecutionException, InterruptedException {

        // todo this is just a simple demo to show how writing and reading to CurrentDB works; For the exercise, make the connection information configurable via quarkus configuration options

        KurrentDBClientSettings clientSettings = KurrentDBConnectionString.parseOrThrow("kurrentdb://eventdb:2113?tls=false");
        KurrentDBClient client = KurrentDBClient.create(clientSettings);

        JsonMapper mapper = new JsonMapper();


        Stream.of("foo", "bar", "foobar")
                .map(DemoEvent::new)
                .map(event -> {
                    try { return mapper.writeValueAsBytes(event); }
                    catch (JsonProcessingException e) { throw new RuntimeException(e);}
                })
                .map(mapped -> EventData.builderAsJson("DemoEventType", mapped).build())
                .forEach(data -> {
                    Log.info("added event to stream!");
                    try { client.appendToStream("demo", data).get(); }
                    catch (InterruptedException | ExecutionException e) { throw new RuntimeException(e);}
                });


        ReadStreamOptions readOptions = ReadStreamOptions.get().forwards().fromStart();
        ReadResult readResult = client.readStream("demo", readOptions).get();

        readResult.getEvents()
                .stream()
                .map(ResolvedEvent::getOriginalEvent)
                .map(recordedEvent -> {
                    try { return mapper.readValue(recordedEvent.getEventData(), DemoEvent.class); }
                    catch (IOException e) { throw new RuntimeException(e);}
                })
                .forEach(event -> Log.info("read event: " + event));
    }

}
