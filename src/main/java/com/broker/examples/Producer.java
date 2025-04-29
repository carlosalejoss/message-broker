package com.broker.examples;

import java.rmi.Naming;
import java.util.Scanner;
import com.broker.core.Message;
import com.broker.interfaces.MessageBrokerRemote;

public class Producer {
    private Scanner scanner;

    public Producer() {
        scanner = new Scanner(System.in);
    }

    public void createQueue(MessageBrokerRemote broker) {
        try {
            System.out.print("Enter queue name: ");
            String queueName = scanner.nextLine();
            broker.declareQueue(queueName);
            System.out.println("Queue '" + queueName + "' created successfully");
        } catch (Exception e) {
            System.err.println("Error creating queue: " + e.toString());
        }
    }

    public void sendMessage(MessageBrokerRemote broker) {
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

    public void showMenu(MessageBrokerRemote broker) {
        while (true) {
            System.out.println("\n=== Producer Menu ===");
            System.out.println("1. Create Queue");
            System.out.println("2. Send Message");
            System.out.println("3. Exit");
            System.out.print("Select an option: ");
            
            String option = scanner.nextLine();
            
            switch (option) {
                case "1":
                    createQueue(broker);
                    break;
                case "2":
                    sendMessage(broker);
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
        try {
            String urlBroker = "rmi://" + args[0] + ":1099/MessageBroker";
            Producer producer = new Producer();
            MessageBrokerRemote broker = (MessageBrokerRemote) Naming.lookup(urlBroker);
            System.out.println("Producer ready and bound to " + urlBroker);

            producer.showMenu(broker);
        } catch (Exception e) {
            System.err.println("Error initializing Producer: " + e.toString());
        }
    }
}