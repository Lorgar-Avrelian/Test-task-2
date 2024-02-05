package org.example.model;

import java.util.Objects;
import java.util.Queue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class TrafficLight {
    private int id;
    private TrafficLightColor color;
    private Queue<Message> messages;
    private int count;

    public TrafficLight() {
    }

    public TrafficLight(int id, TrafficLightColor color, Queue<Message> messages, int count) {
        this.id = id;
        this.color = color;
        this.messages = messages;
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

    public Queue<Message> getMessages() {
        return messages;
    }

    public void setMessages(Queue<Message> messages) {
        this.messages = messages;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TrafficLight that = (TrafficLight) o;
        return id == that.id && count == that.count && Objects.equals(color, that.color);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, color, count);
    }

    @Override
    public String toString() {
        return "TrafficLight{" +
                "id=" + id +
                ", color='" + color + '\'' +
                ", messages=" + messages +
                ", count=" + count +
                '}';
    }

    public void sendMessage(Message message, long timeout) {
        ExecutorService service = Executors.newFixedThreadPool(12);
        Thread thread = new Thread(() -> {
            addMessage(message);
        });
        try {
            thread.sleep(timeout);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        service.submit(thread);
        thread.start();
    }

    private void addMessage(Message message) {
        Queue<Message> messages = getMessages();
        messages.add(message);
        setMessages(messages);
    }
}
