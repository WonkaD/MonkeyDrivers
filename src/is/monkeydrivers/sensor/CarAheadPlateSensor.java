package is.monkeydrivers.sensor;

import is.monkeydrivers.Bus;
import is.monkeydrivers.Message;
import is.monkeydrivers.json.JSONDeserializer;

import java.time.Instant;

public class CarAheadPlateSensor implements VirtualSensor {

    Bus bus;

    @Override
    public void registerBus(Bus bus) {
        this.bus = bus;
    }

    @Override
    public void receive(Message message) {
            bus.send(message(getPlateFrom(message)));
    }

    private String getPlateFrom(Message message) {
        return new JSONDeserializer(message.message()).getValueOfField("plate");
    }

    private Message message(final String plate) {
        return new Message() {
            @Override
            public String type() {
                return "plate";
            }

            @Override
            public String message() {
                return plate;
            }

            @Override
            public Instant timestamp() {
                return Instant.now();
            }
        };
    }
}
