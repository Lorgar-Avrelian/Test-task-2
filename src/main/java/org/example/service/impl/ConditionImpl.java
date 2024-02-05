package org.example.service.impl;

import org.example.model.CrossroadCondition;
import org.example.service.Condition;

public class ConditionImpl implements Condition {
    private CrossroadCondition crossroadCondition;

    @Override
    public synchronized CrossroadCondition getCrossroadCondition() {
        return crossroadCondition;
    }

    @Override
    public synchronized void setCrossroadCondition(CrossroadCondition crossroadCondition) {
        this.crossroadCondition = crossroadCondition;
    }
}
