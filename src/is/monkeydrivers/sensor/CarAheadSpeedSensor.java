package is.monkeydrivers.sensor;

import is.monkeydrivers.Bus;
import is.monkeydrivers.Message;

import java.time.Instant;

public class CarAheadSpeedSensor implements VirtualSensor {
    private Bus bus;

    private InstantMessages old;
    private InstantMessages latest;

    @Override
    public void registerBus(Bus bus) {
        this.bus = bus;
    }

    @Override
    public void receive(Message currentMessage) {
        registerMessage(currentMessage);
        bus.send(
                carAheadSpeedMessage(
                        isReadyToCalculate() ? String.valueOf(calculateSpeed()) : "null"
                )
        );
    }

    private Message carAheadSpeedMessage(String message) {
        return new Message() {
            @Override
            public String type() {
                return "carAheadSpeed";
            }

            @Override
            public String message() {
                return message;
            }

            @Override
            public Instant timestamp() {
                return Instant.now();
            }
        };
    }

    private double calculateSpeed() {
        return latest.speed + (distanceDifference() / instantDifferenceInSeconds(latest.instant, old.instant)) * 3.6f;
    }

    private double distanceDifference() {
        return latest.distance - old.distance;
    }

    private boolean isReadyToCalculate() {
        return old != null && old.isComplete() && latest.isComplete()
                && old.plate.equals(latest.plate) && !old.plate.equals("null") && !latest.plate.equals("null");
    }

    private void registerMessage(Message currentMessage) {
        if (latest == null) latest = new InstantMessages(currentMessage);
        else if (!latest.setMessage(currentMessage)) {
            old = latest;
            latest = new InstantMessages(currentMessage);
        }
    }

    private double instantDifferenceInSeconds(Instant t1, Instant t0) {
        return (double) (t1.getEpochSecond() - t0.getEpochSecond()) + (double) (t1.getNano() - t0.getNano()) / 1000000000d;
    }

    private class InstantMessages {
        Instant instant;
        String plate;
        Double distance;
        Double speed;

        public InstantMessages(Message currentMessage) {
            this.instant = currentMessage.timestamp();
            setMessage(currentMessage);
        }

        public boolean setMessage(Message currentMessage) {
            if (instantDifferenceInSeconds(currentMessage.timestamp(), instant) > 0.25d) return false;
            setCurrentMessage(currentMessage);
            return true;
        }

        private void setCurrentMessage(Message currentMessage) {
            if (currentMessage.type().equals("distance")) distance = Double.parseDouble(currentMessage.message());
            else if (currentMessage.type().equals("plate")) plate = currentMessage.message();
            else if (currentMessage.type().equals("speed")) speed = Double.parseDouble(currentMessage.message());
        }

        public boolean isComplete() {
            return plate != null && distance != null && speed != null;
        }

    }
}
