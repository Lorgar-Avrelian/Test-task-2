package org.example.service.impl;

import org.example.model.CrossroadCondition;
import org.example.service.Condition;

public class ConditionImpl implements Condition {
    private CrossroadCondition crossroadCondition;

    @Override
    public CrossroadCondition getCrossroadCondition() {
        return crossroadCondition;
    }

    @Override
    public void setCrossroadCondition(CrossroadCondition crossroadCondition) {
        System.out.println(crossroadCondition);
        System.out.println("+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++");
        this.crossroadCondition = crossroadCondition;
    }
}
