package com.broker.examples;

import java.rmi.registry.Registry;
import java.rmi.registry.LocateRegistry;
import java.util.Scanner;
import com.broker.core.Message;
import com.broker.interfaces.MessageBrokerRemote;

public class Producer {
    private MessageBrokerRemote broker;
    private Scanner scanner;

    public Producer() {
        try {
            Registry registry = LocateRegistry.getRegistry(1099);
            broker = (MessageBrokerRemote) registry.lookup("MessageBroker");
            scanner = new Scanner(System.in);
        } catch (Exception e) {
            System.err.println("Client exception: " + e.toString());
            e.printStackTrace();
        }
    }

    public void createQueue() {
        try {
            System.out.print("Enter queue name: ");
            String queueName = scanner.nextLine();
            broker.declareQueue(queueName);
            System.out.println("Queue '" + queueName + "' created successfully");
        } catch (Exception e) {
            System.err.println("Error creating queue: " + e.toString());
        }
    }

    public void sendMessage() {
        try {
            System.out.print("Enter queue name: ");
            String queueName = scanner.nextLine();
            System.out.print("Enter message content: ");
            String content = scanner.nextLine();
            
            Message message = new Message(content);
            broker.publish(queueName, message);
            System.out.println("Message sent to queue " + queueName + ": " + content);
        } catch (Exception e) {
            System.err.println("Error sending message: " + e.toString());
        }
    }

    public void showMenu() {
        while (true) {
            System.out.println("\n=== Producer Menu ===");
            System.out.println("1. Create Queue");
            System.out.println("2. Send Message");
            System.out.println("3. Exit");
            System.out.print("Select an option: ");
            
            String option = scanner.nextLine();
            
            switch (option) {
                case "1":
                    createQueue();
                    break;
                case "2":
                    sendMessage();
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
        Producer producer = new Producer();
        producer.showMenu();
    }
}