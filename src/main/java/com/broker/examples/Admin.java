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

    public void deleteQueue(String name) {
        try {
            broker.deleteQueue(name);
            System.out.println("Queue '" + name + "' deleted successfully");
        } catch (Exception e) {
            System.err.println("Error deleting queue: " + e.toString());
        }
    }

    public static void main(String[] args) {
        if (args.length < 2) {
            System.out.println("Usage: Admin delete <queueName>");
            return;
        }

        Admin admin = new Admin();
        String queueName = args[1];
        admin.deleteQueue(queueName);
    }
}
