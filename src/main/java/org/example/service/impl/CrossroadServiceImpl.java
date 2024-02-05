package org.example.service.impl;

import org.example.model.CrossroadCondition;
import org.example.model.TrafficLight;
import org.example.model.TrafficLightColor;
import org.example.service.Condition;
import org.example.service.CrossroadService;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class CrossroadServiceImpl implements CrossroadService {
    private final static long TIMEOUT = 1000;
    private final Condition condition;

    public CrossroadServiceImpl(Condition condition) {
        this.condition = condition;
    }

    /**
     * A method for managing traffic lights.
     */
    @Override
    public void manage() {
        CrossroadCondition crossroadCondition = condition.getCrossroadCondition();
        List<TrafficLight> carTrafficLights = crossroadCondition.getCarTrafficLights();
        List<TrafficLight> pedestrianTrafficLights = crossroadCondition.getPedestrianTrafficLights();
        if (checkTrafficLightsColor(carTrafficLights) && checkTrafficLightsColor(pedestrianTrafficLights)) {
            analyze(carTrafficLights, pedestrianTrafficLights);
        } else {
            try {
                Thread.sleep(TIMEOUT);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
            manage();
        }
    }

    /**
     * A method for checking that all traffic lights are {@link TrafficLightColor#RED}
     *
     * @param trafficLights
     * @return boolean
     */
    private static boolean checkTrafficLightsColor(List<TrafficLight> trafficLights) {
        for (TrafficLight trafficLight : trafficLights) {
            if (!trafficLight.getColor().equals(TrafficLightColor.RED)) {
                return false;
            }
        }
        return true;
    }

    /**
     * A method for analyzing the number of people and cars at an intersection and determining traffic light switches.
     *
     * @param carTrafficLights
     * @param pedestrianTrafficLights
     */
    private static void analyze(List<TrafficLight> carTrafficLights, List<TrafficLight> pedestrianTrafficLights) {
        int carsCount = carTrafficLights.stream()
                                        .mapToInt(TrafficLight::getCount)
                                        .sum();
        int peopleCount = pedestrianTrafficLights.stream()
                                                 .mapToInt(TrafficLight::getCount)
                                                 .sum();
        if (peopleCount >= carsCount * 2) {
            setTrafficLightsByPeopleCount(carTrafficLights, pedestrianTrafficLights);
        } else {
            setTrafficLightsByCarsCount(carTrafficLights, pedestrianTrafficLights);
        }
    }

    /**
     * A method that determining traffic light switches by people count.
     *
     * @param carTrafficLights
     * @param pedestrianTrafficLights
     */
    private static void setTrafficLightsByPeopleCount(List<TrafficLight> carTrafficLights, List<TrafficLight> pedestrianTrafficLights) {
        List<Integer> peopleByDirection = new ArrayList<>();
        for (int i = 0; i < pedestrianTrafficLights.size(); i = i + 2) {
            if (i != 0) {
                peopleByDirection.add(pedestrianTrafficLights.get(i).getCount() + pedestrianTrafficLights.get(i - 1).getCount());
            } else {
                peopleByDirection.add(pedestrianTrafficLights.get(0).getCount() + pedestrianTrafficLights.get(pedestrianTrafficLights.size() - 1).getCount());
            }
        }
        int maxDirectionIndex = 0;
        int maxDirection = peopleByDirection.get(0);
        for (int i = 1; i < peopleByDirection.size(); i++) {
            if (peopleByDirection.get(i) > maxDirection) {
                maxDirection = peopleByDirection.get(i);
                maxDirectionIndex = i;
            }
        }

    }

    /**
     * A method that determining traffic light switches by cars count.
     *
     * @param carTrafficLights
     * @param pedestrianTrafficLights
     */
    private static void setTrafficLightsByCarsCount(List<TrafficLight> carTrafficLights, List<TrafficLight> pedestrianTrafficLights) {
    }
}
