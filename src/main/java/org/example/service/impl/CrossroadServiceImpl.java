package org.example.service.impl;

import org.example.crossroad_factory.constants.Timeout;
import org.example.model.CrossroadCondition;
import org.example.model.Message;
import org.example.model.TrafficLight;
import org.example.model.TrafficLightColor;
import org.example.service.Condition;
import org.example.service.CrossroadService;

import java.util.ArrayList;
import java.util.List;
import java.util.Queue;

public class CrossroadServiceImpl implements CrossroadService {
    private final Condition condition;

    public CrossroadServiceImpl(Condition condition) {
        this.condition = condition;
    }

    /**
     * A method for switching traffic lights.
     */
    @Override
    public void makeSwitches() {
        CrossroadCondition crossroadCondition = condition.getCrossroadCondition();
        List<TrafficLight> carTrafficLights = crossroadCondition.getCarTrafficLights();
        List<TrafficLight> pedestrianTrafficLights = crossroadCondition.getPedestrianTrafficLights();
        for (int i = 0; i < carTrafficLights.size(); i++) {
            int index;
            if (i != 0) {
                index = i - 1;

            } else {
                index = carTrafficLights.size() - 1;
            }
            new Thread(() -> {
                Queue<Message> messages = carTrafficLights.get(index).getMessages();
                while (!messages.isEmpty()) {
                    Message message = messages.poll();
                    TrafficLight trafficLight = carTrafficLights.get(message.getId());
                    trafficLight.setCount(message.getCount());
                    trafficLight.setColor(message.getColor());
                    carTrafficLights.set(message.getId(), trafficLight);
                }
            }).start();
        }
        for (int i = 0; i < pedestrianTrafficLights.size(); i++) {
            int index;
            if (i != 0) {
                index = i - 1;

            } else {
                index = pedestrianTrafficLights.size() - 1;
            }
            new Thread(() -> {
                Queue<Message> messages = pedestrianTrafficLights.get(index).getMessages();
                while (!messages.isEmpty()) {
                    Message message = messages.poll();
                    TrafficLight trafficLight = pedestrianTrafficLights.get(message.getId());
                    trafficLight.setCount(message.getCount());
                    trafficLight.setColor(message.getColor());
                    pedestrianTrafficLights.set(message.getId(), trafficLight);
                }
            }).start();
        }
        CrossroadCondition newCrossroadCondition = new CrossroadCondition(carTrafficLights, pedestrianTrafficLights);
        condition.setCrossroadCondition(newCrossroadCondition);
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
                Thread.sleep(Timeout.TIMEOUT);
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
    private void analyze(List<TrafficLight> carTrafficLights, List<TrafficLight> pedestrianTrafficLights) {
        int carsCount = carTrafficLights.stream()
                                        .mapToInt(TrafficLight::getCount)
                                        .sum();
        int peopleCount = pedestrianTrafficLights.stream()
                                                 .mapToInt(TrafficLight::getCount)
                                                 .sum();
        if (peopleCount == 0 && carsCount == 0) {
            manage();
        }
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
    private void setTrafficLightsByPeopleCount(List<TrafficLight> carTrafficLights, List<TrafficLight> pedestrianTrafficLights) {
        List<Integer> peopleByDirection = new ArrayList<>();
        for (int i = 0; i < pedestrianTrafficLights.size(); i = i + 2) {
            if (i != 0) {
                peopleByDirection.add(pedestrianTrafficLights.get(i).getCount() + pedestrianTrafficLights.get(i - 1).getCount());
            } else {
                peopleByDirection.add(pedestrianTrafficLights.get(0).getCount() + pedestrianTrafficLights.get(pedestrianTrafficLights.size() - 1).getCount());
            }
        }
        int maxDirectionIndex = maxDirectionIndex(peopleByDirection);
        int firstPedestrianTrafficLight;
        int secondPedestrianTrafficLight;
        if (maxDirectionIndex != 0) {
            firstPedestrianTrafficLight = maxDirectionIndex * 2 - 1;
            secondPedestrianTrafficLight = maxDirectionIndex * 2;
        } else {
            firstPedestrianTrafficLight = 0;
            secondPedestrianTrafficLight = pedestrianTrafficLights.size() - 1;
        }
        int firstCarTrafficLight;
        int secondCarTrafficLight;
        if (maxDirectionIndex != carTrafficLights.size() - 1) {
            firstCarTrafficLight = maxDirectionIndex + 1;
        } else {
            firstCarTrafficLight = 0;
        }
        if (maxDirectionIndex != 0) {
            secondCarTrafficLight = maxDirectionIndex - 1;
        } else {
            secondCarTrafficLight = carTrafficLights.size() - 1;
        }
        setLights(carTrafficLights, pedestrianTrafficLights, firstCarTrafficLight, secondCarTrafficLight, firstPedestrianTrafficLight, secondPedestrianTrafficLight);
    }

    /**
     * A method for searching the index of the busiest destination.
     *
     * @param direction
     * @return maxDirectionIndex
     */
    private int maxDirectionIndex(List<Integer> direction) {
        int maxDirectionIndex = 0;
        int maxDirection = direction.get(0);
        for (int i = 1; i < direction.size(); i++) {
            if (direction.get(i) > maxDirection) {
                maxDirection = direction.get(i);
                maxDirectionIndex = i;
            }
        }
        return maxDirectionIndex;
    }

    /**
     * A method for switching traffic light colors.
     *
     * @param carTrafficLights
     * @param pedestrianTrafficLights
     * @param firstCarTrafficLight
     * @param secondCarTrafficLight
     * @param firstPedestrianTrafficLight
     * @param secondPedestrianTrafficLight
     */
    private void setLights(List<TrafficLight> carTrafficLights, List<TrafficLight> pedestrianTrafficLights, int firstCarTrafficLight, int secondCarTrafficLight, int firstPedestrianTrafficLight, int secondPedestrianTrafficLight) {
        int secondCarTrafficLightCount = carTrafficLights.get(secondCarTrafficLight).getCount();
        int secondPedestrianTrafficLightCount = pedestrianTrafficLights.get(secondPedestrianTrafficLight).getCount();
        int firstPedestrianTrafficLightCount = pedestrianTrafficLights.get(firstPedestrianTrafficLight).getCount();
        carTrafficLights.get(firstCarTrafficLight).sendMessage(
                new Message(secondCarTrafficLightCount,
                            secondCarTrafficLight,
                            TrafficLightColor.YELLOW),
                Timeout.TIMEOUT);
        makeSwitches();
        if (secondCarTrafficLightCount > 0) {
            secondCarTrafficLightCount = secondCarTrafficLightCount - 1;
        }
        if (secondPedestrianTrafficLightCount > 0) {
            secondPedestrianTrafficLightCount = secondPedestrianTrafficLightCount - 1;
        }
        if (firstPedestrianTrafficLightCount > 0) {
            firstPedestrianTrafficLightCount = firstPedestrianTrafficLightCount - 1;
        }
        carTrafficLights.get(firstCarTrafficLight).sendMessage(
                new Message(secondCarTrafficLightCount,
                            secondCarTrafficLight,
                            TrafficLightColor.GREEN),
                Timeout.TIMEOUT);
        pedestrianTrafficLights.get(firstPedestrianTrafficLight).sendMessage(
                new Message(secondPedestrianTrafficLightCount,
                            secondPedestrianTrafficLight,
                            TrafficLightColor.GREEN),
                Timeout.TIMEOUT * 2);
        pedestrianTrafficLights.get(secondPedestrianTrafficLight).sendMessage(
                new Message(firstPedestrianTrafficLightCount,
                            firstPedestrianTrafficLight,
                            TrafficLightColor.GREEN),
                Timeout.TIMEOUT * 2);
        makeSwitches();
        secondCarTrafficLightCount = carTrafficLights.get(secondCarTrafficLight).getCount();
        secondPedestrianTrafficLightCount = pedestrianTrafficLights.get(secondPedestrianTrafficLight).getCount();
        firstPedestrianTrafficLightCount = pedestrianTrafficLights.get(firstPedestrianTrafficLight).getCount();
        carTrafficLights.get(firstCarTrafficLight).sendMessage(
                new Message(secondCarTrafficLightCount,
                            secondCarTrafficLight,
                            TrafficLightColor.YELLOW),
                Timeout.TIMEOUT);
        pedestrianTrafficLights.get(firstPedestrianTrafficLight).sendMessage(
                new Message(secondPedestrianTrafficLightCount,
                            secondPedestrianTrafficLight,
                            TrafficLightColor.RED),
                Timeout.TIMEOUT * 2);
        pedestrianTrafficLights.get(secondPedestrianTrafficLight).sendMessage(
                new Message(firstPedestrianTrafficLightCount,
                            firstPedestrianTrafficLight,
                            TrafficLightColor.RED),
                Timeout.TIMEOUT * 2);
        makeSwitches();
        carTrafficLights.get(firstCarTrafficLight).sendMessage(
                new Message(secondCarTrafficLightCount,
                            secondCarTrafficLight,
                            TrafficLightColor.RED),
                Timeout.TIMEOUT);
        makeSwitches();
        manage();
    }

    /**
     * A method that determining traffic light switches by cars count.
     *
     * @param carTrafficLights
     * @param pedestrianTrafficLights
     */
    private void setTrafficLightsByCarsCount(List<TrafficLight> carTrafficLights, List<TrafficLight> pedestrianTrafficLights) {
        List<Integer> carsByDirection = carTrafficLights.stream()
                                                        .map(TrafficLight::getCount)
                                                        .toList();
        int maxDirectionIndex = maxDirectionIndex(carsByDirection);
        int firstCarTrafficLight = 0;
        int secondCarTrafficLight = maxDirectionIndex;
        switch (maxDirectionIndex) {
            case 0 -> {
                firstCarTrafficLight = 2;
            }
            case 1 -> {
                firstCarTrafficLight = 3;
            }
            case 2 -> {
                firstCarTrafficLight = 0;
            }
            case 3 -> {
                firstCarTrafficLight = 1;
            }
        }
        int firstPedestrianTrafficLight = 0;
        int secondPedestrianTrafficLight = 0;
        switch (maxDirectionIndex) {
            case 0 -> {
                firstPedestrianTrafficLight = 1;
                secondPedestrianTrafficLight = 2;
            }
            case 1 -> {
                firstPedestrianTrafficLight = 3;
                secondPedestrianTrafficLight = 4;
            }
            case 2 -> {
                firstPedestrianTrafficLight = 5;
                secondPedestrianTrafficLight = 6;
            }
            case 3 -> {
                firstPedestrianTrafficLight = 7;
            }
        }
        setLights(carTrafficLights, pedestrianTrafficLights, firstCarTrafficLight, secondCarTrafficLight, firstPedestrianTrafficLight, secondPedestrianTrafficLight);
    }
}
