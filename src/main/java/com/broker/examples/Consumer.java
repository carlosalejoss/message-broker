package com.broker.examples;

import java.rmi.server.UnicastRemoteObject;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.util.Scanner;
import java.util.HashSet;
import java.util.Set;
import com.broker.interfaces.MessageBrokerRemote;
import com.broker.interfaces.MessageCallback;

public class Consumer extends UnicastRemoteObject {
    private Scanner scanner;
    private Set<String> subscribedQueues;
    private MessageCallback callback;

    public Consumer() throws RemoteException {
        try {
            scanner = new Scanner(System.in);
            subscribedQueues = new HashSet<>();
            callback = new MessageCallbackImpl();
        } catch (Exception e) {
            System.err.println("Client exception: " + e.toString());
            e.printStackTrace();
        }
    }

    public void subscribe(MessageBrokerRemote broker) {
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

    public void listQueues(MessageBrokerRemote broker) {
        try {
            System.out.println("\nAvailable queues:");
            broker.listQueues().forEach(queue -> System.out.println("- " + queue));
        } catch (Exception e) {
            System.err.println("Error listing queues: " + e.toString());
        }
    }

    public void showMenu(MessageBrokerRemote broker) {
        while (true) {
            System.out.println("\n=== Consumer Menu ===");
            System.out.println("1. Subscribe to Queue");
            System.out.println("2. Unsubscribe from Queue");
            System.out.println("3. List Queues");
            System.out.println("4. Exit");
            System.out.print("Select an option: ");
            
            String option = scanner.nextLine();
            
            switch (option) {
                case "1":
                    subscribe(broker);
                    break;
                case "2":
                    unsubscribe();
                    break;
                case "3":
                    listQueues(broker);
                    break;
                case "4":
                    System.out.println("Exiting...");
                    return;
                default:
                    System.out.println("Invalid option. Please try again.");
            }
        }
    }

    public static void main(String[] args) {
        try {
            String urlBroker = "rmi://" + args[0] + ":1099/MessageBroker";
            Consumer consumer = new Consumer();
            MessageBrokerRemote broker = (MessageBrokerRemote) Naming.lookup(urlBroker);
            System.out.println("Consumer ready and bound to " + urlBroker);

            consumer.showMenu(broker);
        } catch (Exception e) {
            System.err.println("Error initializing Consumer: " + e.toString());
        }
    }
}