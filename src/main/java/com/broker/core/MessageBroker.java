package com.broker.core;

import java.rmi.RemoteException;
import java.rmi.registry.Registry;
import java.rmi.registry.LocateRegistry;
import java.rmi.server.UnicastRemoteObject;
import java.util.*;
import com.broker.interfaces.*;

public class MessageBroker extends UnicastRemoteObject implements MessageBrokerRemote {
    private static final int PORT = 1099;
    private Map<String, Queue> queues;
    private Map<String, List<MessageCallback>> subscribers;
    private Map<String, Integer> currentConsumerIndex; // Track current consumer for each queue

    public MessageBroker() throws RemoteException {
        super();
        this.queues = new HashMap<>();
        this.subscribers = new HashMap<>();
        this.currentConsumerIndex = new HashMap<>();
    }

    @Override
    public synchronized void declareQueue(String name) throws RemoteException {
        if (!queues.containsKey(name)) {
            queues.put(name, new Queue(name));
            subscribers.put(name, new ArrayList<>());
            currentConsumerIndex.put(name, 0);
        }
    }

    @Override
    public synchronized void publish(String queueName, Message message) throws RemoteException {
        Queue queue = queues.get(queueName);
        if (queue != null) {
            queue.addMessage(message);
            // Distribuir el mensaje a los suscriptores en un nuevo hilo
            new Thread(() -> notifySubscribers(queueName, message)).start();
        } else {
            System.err.println("Message discarded - Queue does not exist: " + queueName);
            throw new RemoteException("Queue '" + queueName + "' does not exist");
        }
    }

    @Override
    public synchronized void subscribe(String queueName, MessageCallback callback) throws RemoteException {
        List<MessageCallback> queueSubscribers = subscribers.get(queueName);
        if (queueSubscribers != null) {
            queueSubscribers.add(callback);
        }
    }

    private void notifySubscribers(String queueName, Message message) {
        List<MessageCallback> queueSubscribers = subscribers.get(queueName);
        if (queueSubscribers != null && !queueSubscribers.isEmpty()) {
            synchronized (this) {
                // Get current index for this queue
                int currentIndex = currentConsumerIndex.get(queueName);
                
                // Get next consumer in round-robin fashion
                MessageCallback callback = queueSubscribers.get(currentIndex);
                
                try {
                    // Send message to the selected consumer
                    callback.onMessage(message);
                    
                    // Update index for next message
                    currentIndex = (currentIndex + 1) % queueSubscribers.size();
                    currentConsumerIndex.put(queueName, currentIndex);
                } catch (Exception e) {
                    System.err.println("Error notifying subscriber: " + e.toString());
                    e.printStackTrace();
                    
                    // If failed, remove the failed consumer and adjust index
                    queueSubscribers.remove(currentIndex);
                    if (currentIndex >= queueSubscribers.size()) {
                        currentIndex = 0;
                    }
                    currentConsumerIndex.put(queueName, currentIndex);
                }
            }
        }
    }

    public static void main(String[] args) {
        try {
            MessageBroker broker = new MessageBroker();
            Registry registry = LocateRegistry.createRegistry(PORT);
            registry.rebind("MessageBroker", broker);
            System.out.println("Message Broker is running...");
        } catch (Exception e) {
            System.err.println("Server exception: " + e.toString());
            e.printStackTrace();
        }
    }
}