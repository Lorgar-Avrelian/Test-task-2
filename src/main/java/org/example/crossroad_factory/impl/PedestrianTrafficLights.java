package org.example.crossroad_factory.impl;

import org.example.crossroad_factory.TrafficLights;
import org.example.model.Message;
import org.example.model.TrafficLight;
import org.example.model.TrafficLightColor;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.List;
import java.util.Queue;

public class PedestrianTrafficLights extends TrafficLights {
    @Override
    public List<TrafficLight> getTrafficLights() {
        List<TrafficLight> pedestrianTrafficLights = new ArrayList<>();
        for (int i = 0; i < 8; i++) {
            Queue<Message> messages = new ArrayDeque<>();
            TrafficLight trafficLight = new TrafficLight(i, TrafficLightColor.RED, messages, 0);
            pedestrianTrafficLights.add(trafficLight);
        }
        return pedestrianTrafficLights;
    }
}
