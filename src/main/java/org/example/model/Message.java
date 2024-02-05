package org.example.model;

import java.util.Objects;

public class Message {
    private int count;
    private int id;
    private TrafficLightColor color;

    public Message() {
    }

    public Message(int count, int id, TrafficLightColor color) {
        this.count = count;
        this.id = id;
        this.color = color;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public TrafficLightColor getColor() {
        return color;
    }

    public void setColor(TrafficLightColor color) {
        this.color = color;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Message message = (Message) o;
        return count == message.count && id == message.id && Objects.equals(color, message.color);
    }

    @Override
    public int hashCode() {
        return Objects.hash(count, id, color);
    }

    @Override
    public String toString() {
        return "Message{" +
                "count=" + count +
                ", id=" + id +
                ", color='" + color + '\'' +
                '}';
    }
}
