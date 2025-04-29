package com.broker.examples;

import java.rmi.registry.Registry;
import java.rmi.registry.LocateRegistry;
import com.broker.interfaces.MessageBrokerRemote;
import com.broker.interfaces.MessageCallback;

public class Consumer {
    private MessageBrokerRemote broker;

    public Consumer() {
        try {
            Registry registry = LocateRegistry.getRegistry(1099);
            broker = (MessageBrokerRemote) registry.lookup("MessageBroker");
        } catch (Exception e) {
            System.err.println("Client exception: " + e.toString());
            e.printStackTrace();
        }
    }

    public void startConsuming(String queueName) {
        try {
            MessageCallback callback = new MessageCallbackImpl();
            broker.subscribe(queueName, callback);
        } catch (Exception e) {
            System.err.println("Error subscribing: " + e.toString());
        }
    }

    public static void main(String[] args) {
        Consumer consumer = new Consumer();
        String queueName = "exampleQueue";
        
        try {
            consumer.broker.declareQueue(queueName);
            System.out.println("Starting to consume messages from queue: " + queueName);
            consumer.startConsuming(queueName);
            
            // Keep the consumer running
            while (true) {
                Thread.sleep(1000);
            }
        } catch (Exception e) {
            System.err.println("Error: " + e.toString());
        }
    }
}