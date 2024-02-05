package org.example.crossroad_factory.impl;

import org.example.crossroad_factory.CrossroadFactory;

public class CrossroadFactoryImpl implements CrossroadFactory {
    @Override
    public CarTrafficLights getCarTrafficLights() {
        return new CarTrafficLights();
    }

    @Override
    public PedestrianTrafficLights getPedestrianTrafficLights() {
        return new PedestrianTrafficLights();
    }
}
