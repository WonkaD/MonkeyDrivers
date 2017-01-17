package is.monkeydrivers;

import is.monkeydrivers.vehicle.Vehicle;

public class SpeedActuator implements Actuator {
    private Vehicle vehicle;
    private Double carAheadSpeed;
    private Double roadMaxSpeed;

    @Override
    public void receive(Message message) {
        setCurrentMessage(message);
        vehicle.setSpeed(calculateNewSpeed());
    }

    private double calculateNewSpeed() {
        if (carAheadSpeed == null || (roadMaxSpeed != null && carAheadSpeed > roadMaxSpeed)) return roadMaxSpeed;
        else if (roadMaxSpeed != null) return carAheadSpeed;
        return vehicle.getSpeed();
    }


    private void setCurrentMessage(Message currentMessage) {
        if (currentMessage.type().equals("carAheadSpeed"))
            carAheadSpeed = currentMessage.message().equals("null") ? null : Double.parseDouble(currentMessage.message());
        else if (currentMessage.type().equals("roadMaxSpeed"))
            roadMaxSpeed = Double.parseDouble(currentMessage.message());
    }

    @Override
    public void registerVehicle(Vehicle vehicle) {
        this.vehicle = vehicle;
    }
}
