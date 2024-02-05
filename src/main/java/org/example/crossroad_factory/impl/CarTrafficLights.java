package org.example.crossroad_factory.impl;

import org.example.crossroad_factory.TrafficLights;
import org.example.model.TrafficLightColor;
import org.example.model.Message;
import org.example.model.TrafficLight;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.List;
import java.util.Queue;

public class CarTrafficLights extends TrafficLights {
    @Override
    public List<TrafficLight> getTrafficLights() {
        List<TrafficLight> carTrafficLights = new ArrayList<>();
        for (int i = 0; i < 4; i++) {
            Queue<Message> messages = new ArrayDeque<>();
            TrafficLight trafficLight = new TrafficLight(i, TrafficLightColor.RED, messages, 0);
            carTrafficLights.add(trafficLight);
        }
        return carTrafficLights;
    }
}
