package org.example.model;

import java.util.List;
import java.util.Objects;

public class CrossroadCondition {
    private List<TrafficLight> carTrafficLights;
    private List<TrafficLight> pedestrianTrafficLights;

    public CrossroadCondition() {
    }

    public CrossroadCondition(List<TrafficLight> carTrafficLights, List<TrafficLight> pedestrianTrafficLights) {
        this.carTrafficLights = carTrafficLights;
        this.pedestrianTrafficLights = pedestrianTrafficLights;
    }

    public List<TrafficLight> getCarTrafficLights() {
        return carTrafficLights;
    }

    public void setCarTrafficLights(List<TrafficLight> carTrafficLights) {
        this.carTrafficLights = carTrafficLights;
    }

    public List<TrafficLight> getPedestrianTrafficLights() {
        return pedestrianTrafficLights;
    }

    public void setPedestrianTrafficLights(List<TrafficLight> pedestrianTrafficLights) {
        this.pedestrianTrafficLights = pedestrianTrafficLights;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CrossroadCondition that = (CrossroadCondition) o;
        return Objects.equals(carTrafficLights, that.carTrafficLights) && Objects.equals(pedestrianTrafficLights, that.pedestrianTrafficLights);
    }

    @Override
    public int hashCode() {
        return Objects.hash(carTrafficLights, pedestrianTrafficLights);
    }

    @Override
    public String toString() {
        return "CrossroadCondition{" +
                "\n" +
                "carTrafficLights=" +
                "\n" +
                carTrafficLights +
                ", \n" +
                "pedestrianTrafficLights=" +
                "\n" +
                pedestrianTrafficLights +
                "\n" +
                '}';
    }
}
