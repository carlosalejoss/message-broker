package com.broker.examples;

import java.rmi.registry.Registry;
import java.rmi.registry.LocateRegistry;
import com.broker.interfaces.MessageBrokerRemote;

public class Admin {
    private MessageBrokerRemote broker;

    public Admin() {
        try {
            Registry registry = LocateRegistry.getRegistry(1099);
            broker = (MessageBrokerRemote) registry.lookup("MessageBroker");
        } catch (Exception e) {
            System.err.println("Admin exception: " + e.toString());
            e.printStackTrace();
        }
    }

    public void listQueues() {
        try {
            System.out.println("Available queues:");
            broker.listQueues().forEach(queue -> System.out.println("- " + queue));
        } catch (Exception e) {
            System.err.println("Error listing queues: " + e.toString());
        }
    }

    public void deleteQueue(String name) {
        try {
            broker.deleteQueue(name);
            System.out.println("Queue '" + name + "' deleted successfully");
        } catch (Exception e) {
            System.err.println("Error deleting queue: " + e.toString());
        }
    }

    public static void main(String[] args) {
        if (args.length < 1) {
            System.out.println("Usage: Admin <command> [args]");
            System.out.println("Commands:");
            System.out.println("  list         - List all queues");
            System.out.println("  delete <name> - Delete a queue");
            return;
        }

        Admin admin = new Admin();
        String command = args[0];

        switch (command) {
            case "list":
                admin.listQueues();
                break;
            case "delete":
                if (args.length < 2) {
                    System.out.println("Error: Queue name required for delete command");
                    return;
                }
                admin.deleteQueue(args[1]);
                break;
            default:
                System.out.println("Unknown command: " + command);
        }
    }
}
