package com.broker.examples;

import java.rmi.registry.Registry;
import java.rmi.registry.LocateRegistry;
import java.util.Scanner;
import java.util.HashSet;
import java.util.Set;
import com.broker.interfaces.MessageBrokerRemote;
import com.broker.interfaces.MessageCallback;

public class Consumer {
    private MessageBrokerRemote broker;
    private Scanner scanner;
    private Set<String> subscribedQueues;
    private MessageCallback callback;

    public Consumer() {
        try {
            Registry registry = LocateRegistry.getRegistry(1099);
            broker = (MessageBrokerRemote) registry.lookup("MessageBroker");
            scanner = new Scanner(System.in);
            subscribedQueues = new HashSet<>();
            callback = new MessageCallbackImpl();
        } catch (Exception e) {
            System.err.println("Client exception: " + e.toString());
            e.printStackTrace();
        }
    }

    public void subscribe() {
        try {
            System.out.print("Enter queue name to subscribe: ");
            String queueName = scanner.nextLine();
            
            if (subscribedQueues.contains(queueName)) {
                System.out.println("Already subscribed to queue: " + queueName);
                return;
            }

            broker.declareQueue(queueName);
            broker.subscribe(queueName, callback);
            subscribedQueues.add(queueName);
            System.out.println("Subscribed to queue: " + queueName);
        } catch (Exception e) {
            System.err.println("Error subscribing: " + e.toString());
        }
    }

    public void unsubscribe() {
        try {
            if (subscribedQueues.isEmpty()) {
                System.out.println("Not subscribed to any queues");
                return;
            }

            System.out.println("Currently subscribed to:");
            subscribedQueues.forEach(queue -> System.out.println("- " + queue));
            
            System.out.print("Enter queue name to unsubscribe: ");
            String queueName = scanner.nextLine();
            
            if (subscribedQueues.remove(queueName)) {
                System.out.println("Unsubscribed from queue: " + queueName);
            } else {
                System.out.println("Not subscribed to queue: " + queueName);
            }
        } catch (Exception e) {
            System.err.println("Error unsubscribing: " + e.toString());
        }
    }

    public void showMenu() {
        while (true) {
            System.out.println("\n=== Consumer Menu ===");
            System.out.println("1. Subscribe to Queue");
            System.out.println("2. Unsubscribe from Queue");
            System.out.println("3. Exit");
            System.out.print("Select an option: ");
            
            String option = scanner.nextLine();
            
            switch (option) {
                case "1":
                    subscribe();
                    break;
                case "2":
                    unsubscribe();
                    break;
                case "3":
                    System.out.println("Exiting...");
                    return;
                default:
                    System.out.println("Invalid option. Please try again.");
            }
        }
    }

    public static void main(String[] args) {
        Consumer consumer = new Consumer();
        consumer.showMenu();
    }
}