package org.example;

import org.example.crossroad_factory.CrossroadFactory;
import org.example.crossroad_factory.constants.Timeout;
import org.example.crossroad_factory.impl.CrossroadFactoryImpl;
import org.example.model.CrossroadCondition;
import org.example.model.Message;
import org.example.model.TrafficLight;
import org.example.model.TrafficLightColor;
import org.example.service.Condition;
import org.example.service.CrossroadService;
import org.example.service.impl.ConditionImpl;
import org.example.service.impl.CrossroadServiceImpl;

import java.util.List;

public class Main {
    public static void main(String[] args) {
        // Получаем модель перекрёстка
        CrossroadCondition crossroadCondition = getCrossroadCondition();
        Condition condition = new ConditionImpl();
        condition.setCrossroadCondition(crossroadCondition);
        // Инжектим в клиент сервис для управления светофорами
        CrossroadService crossroadService = new CrossroadServiceImpl(condition);
        // Изменяем начальные значения количества людей/машин для каждого из направлений
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
        setPedestrianAndCarsCount(crossroadService,
                                  condition,
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
        crossroadService.manage();
    }

    private static CrossroadCondition getCrossroadCondition() {
        CrossroadFactory crossroad = new CrossroadFactoryImpl();
        CrossroadCondition crossroadCondition = new CrossroadCondition();
        crossroadCondition.setCarTrafficLights(crossroad.getCarTrafficLights().getTrafficLights());
        crossroadCondition.setPedestrianTrafficLights(crossroad.getPedestrianTrafficLights().getTrafficLights());
        return crossroadCondition;
    }

    private static void setPedestrianAndCarsCount(CrossroadService crossroadService,
                                                  Condition condition,
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
        //
        Message leftUpToLeftDownPeopleMessage = new Message(leftUpToLeftDownPeople, 0, TrafficLightColor.RED);
        Message leftUpToRightUpPeopleMessage = new Message(leftUpToRightUpPeople, 1, TrafficLightColor.RED);
        Message rightUpToLeftUpPeopleMessage = new Message(rightUpToLeftUpPeople, 2, TrafficLightColor.RED);
        Message rightUpToRightDownPeopleMessage = new Message(rightUpToRightDownPeople, 3, TrafficLightColor.RED);
        Message rightDownToRightUpPeopleMessage = new Message(rightDownToRightUpPeople, 4, TrafficLightColor.RED);
        Message rightDownToLeftDownPeopleMessage = new Message(rightDownToLeftDownPeople, 5, TrafficLightColor.RED);
        Message leftDownToRightDownPeopleMessage = new Message(leftDownToRightDownPeople, 6, TrafficLightColor.RED);
        Message leftDownToLeftUpPeopleMessage = new Message(leftDownToLeftUpPeople, 7, TrafficLightColor.RED);
        //
        Message leftToRightCarsMessage = new Message(leftToRightCars, 0, TrafficLightColor.RED);
        Message UpToDownCarsMessage = new Message(UpToDownCars, 1, TrafficLightColor.RED);
        Message rightToLeftCarsMessage = new Message(rightToLeftCars, 2, TrafficLightColor.RED);
        Message downToUpCarsMessage = new Message(downToUpCars, 3, TrafficLightColor.RED);
        //
        List<TrafficLight> pedestrianTrafficLights = condition.getCrossroadCondition().getPedestrianTrafficLights();
        //
        pedestrianTrafficLights.get(0).sendMessage(leftDownToLeftUpPeopleMessage, Timeout.TIMEOUT);
        pedestrianTrafficLights.get(1).sendMessage(rightUpToLeftUpPeopleMessage, Timeout.TIMEOUT);
        pedestrianTrafficLights.get(2).sendMessage(leftUpToRightUpPeopleMessage, Timeout.TIMEOUT);
        pedestrianTrafficLights.get(3).sendMessage(rightDownToRightUpPeopleMessage, Timeout.TIMEOUT);
        pedestrianTrafficLights.get(4).sendMessage(rightUpToRightDownPeopleMessage, Timeout.TIMEOUT);
        pedestrianTrafficLights.get(5).sendMessage(leftDownToRightDownPeopleMessage, Timeout.TIMEOUT);
        pedestrianTrafficLights.get(6).sendMessage(rightDownToLeftDownPeopleMessage, Timeout.TIMEOUT);
        pedestrianTrafficLights.get(7).sendMessage(leftUpToLeftDownPeopleMessage, Timeout.TIMEOUT);
        //
        List<TrafficLight> carTrafficLights = condition.getCrossroadCondition().getCarTrafficLights();
        //
        carTrafficLights.get(0).sendMessage(rightToLeftCarsMessage, Timeout.TIMEOUT);
        carTrafficLights.get(1).sendMessage(downToUpCarsMessage, Timeout.TIMEOUT);
        carTrafficLights.get(2).sendMessage(leftToRightCarsMessage, Timeout.TIMEOUT);
        carTrafficLights.get(3).sendMessage(UpToDownCarsMessage, Timeout.TIMEOUT);
        //
        crossroadService.makeSwitches();
    }
}