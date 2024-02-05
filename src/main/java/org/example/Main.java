package org.example;

import org.example.crossroad_factory.CrossroadFactory;
import org.example.crossroad_factory.impl.CrossroadFactoryImpl;
import org.example.model.CrossroadCondition;
import org.example.model.TrafficLight;
import org.example.service.Condition;
import org.example.service.CrossroadService;
import org.example.service.impl.ConditionImpl;
import org.example.service.impl.CrossroadServiceImpl;

import java.util.List;

public class Main {
    public static void main(String[] args) {
        // Получаем модель перекрёстка
        CrossroadCondition crossroadCondition = getCrossroadCondition();
        // Изменяем начальные значения количества людей/машин для каждого из направлений
        Condition condition = new ConditionImpl();
        condition.setCrossroadCondition(crossroadCondition);
        int leftUpToLeftDownPeople = 0;
        int leftUpToRightUpPeople = 1;
        int rightUpToLeftUpPeople = 2;
        int rightUpToRightDownPeople = 3;
        int rightDownToRightUpPeople = 4;
        int rightDownToLeftDownPeople = 5;
        int leftDownToRightDownPeople = 6;
        int leftDownToLeftUpPeople = 7;
        int leftToRightCars = 0;
        int UpToDownCars = 1;
        int rightToLeftCars = 2;
        int downToUpCars = 3;
        setPedestrianAndCarsCount(condition,
                                  leftUpToLeftDownPeople,
                                  leftUpToRightUpPeople,
                                  rightUpToLeftUpPeople,
                                  rightUpToRightDownPeople,
                                  rightDownToRightUpPeople,
                                  rightDownToLeftDownPeople,
                                  leftDownToRightDownPeople,
                                  leftDownToLeftUpPeople,
                                  leftToRightCars,
                                  UpToDownCars,
                                  rightToLeftCars,
                                  downToUpCars);
        // Передаём текущее состояние алгоритму управления светофорами
        CrossroadService crossroadService = new CrossroadServiceImpl(condition);
        crossroadService.manage();
    }

    private static CrossroadCondition getCrossroadCondition() {
        CrossroadFactory crossroad = new CrossroadFactoryImpl();
        CrossroadCondition crossroadCondition = new CrossroadCondition();
        crossroadCondition.setCarTrafficLights(crossroad.getCarTrafficLights().getTrafficLights());
        crossroadCondition.setPedestrianTrafficLights(crossroad.getPedestrianTrafficLights().getTrafficLights());
        return crossroadCondition;
    }

    private static void setPedestrianAndCarsCount(Condition condition,
                                                  int leftUpToLeftDownPeople,
                                                  int leftUpToRightUpPeople,
                                                  int rightUpToLeftUpPeople,
                                                  int rightUpToRightDownPeople,
                                                  int rightDownToRightUpPeople,
                                                  int rightDownToLeftDownPeople,
                                                  int leftDownToRightDownPeople,
                                                  int leftDownToLeftUpPeople,
                                                  int leftToRightCars,
                                                  int UpToDownCars,
                                                  int rightToLeftCars,
                                                  int downToUpCars
                                                 ) {
        List<TrafficLight> pedestrianTrafficLights = condition.getCrossroadCondition().getPedestrianTrafficLights();
        pedestrianTrafficLights.get(0).setCount(leftUpToLeftDownPeople);
        pedestrianTrafficLights.get(1).setCount(leftUpToRightUpPeople);
        pedestrianTrafficLights.get(2).setCount(rightUpToLeftUpPeople);
        pedestrianTrafficLights.get(3).setCount(rightUpToRightDownPeople);
        pedestrianTrafficLights.get(4).setCount(rightDownToRightUpPeople);
        pedestrianTrafficLights.get(5).setCount(rightDownToLeftDownPeople);
        pedestrianTrafficLights.get(6).setCount(leftDownToRightDownPeople);
        pedestrianTrafficLights.get(7).setCount(leftDownToLeftUpPeople);
        List<TrafficLight> carTrafficLights = condition.getCrossroadCondition().getCarTrafficLights();
        carTrafficLights.get(0).setCount(leftToRightCars);
        carTrafficLights.get(1).setCount(UpToDownCars);
        carTrafficLights.get(2).setCount(rightToLeftCars);
        carTrafficLights.get(3).setCount(downToUpCars);
        CrossroadCondition crossroadCondition = new CrossroadCondition(carTrafficLights, pedestrianTrafficLights);
        condition.setCrossroadCondition(crossroadCondition);
    }
}