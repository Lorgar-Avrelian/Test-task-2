package org.example.crossroad_factory;

import org.example.crossroad_factory.impl.CarTrafficLights;
import org.example.crossroad_factory.impl.PedestrianTrafficLights;

public interface CrossroadFactory {
    CarTrafficLights getCarTrafficLights();
    PedestrianTrafficLights getPedestrianTrafficLights();
}
