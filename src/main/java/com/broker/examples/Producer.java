package com.broker.examples;

import java.rmi.registry.Registry;
import java.rmi.registry.LocateRegistry;
import com.broker.core.Message;
import com.broker.interfaces.MessageBrokerRemote;

public class Producer {
    private MessageBrokerRemote broker;

    public Producer() {
        try {
            Registry registry = LocateRegistry.getRegistry(1099);
            broker = (MessageBrokerRemote) registry.lookup("MessageBroker");
        } catch (Exception e) {
            System.err.println("Client exception: " + e.toString());
            e.printStackTrace();
        }
    }

    public void sendMessage(String queueName, String content) {
        try {
            Message message = new Message(content);
            broker.publish(queueName, message);
            System.out.println("Message sent to queue " + queueName + ": " + content);
        } catch (Exception e) {
            System.err.println("Error sending message: " + e.toString());
        }
    }

    public static void main(String[] args) {
        Producer producer = new Producer();
        String queueName = "exampleQueue";
        
        try {
            producer.broker.declareQueue(queueName);
            producer.sendMessage(queueName, "Hello, World!");
            producer.sendMessage(queueName, "Another message");
            producer.sendMessage(queueName, "Third message");
        } catch (Exception e) {
            System.err.println("Error: " + e.toString());
        }
    }
}