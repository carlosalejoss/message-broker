package com.broker.examples;

import java.rmi.Naming;
import com.broker.interfaces.MessageBrokerRemote;

public class Admin {
    
    public void deleteQueue(MessageBrokerRemote broker, String name) {
        try {
            broker.deleteQueue(name);
            System.out.println("Queue '" + name + "' deleted successfully");
        } catch (Exception e) {
            System.err.println("Error deleting queue: " + e.toString());
        }
    }

    public static void main(String[] args) {
        try {
            if (args.length < 3) {
                System.out.println("Usage: Admin <host> delete <queueName>");
                return;
            }

            String urlBroker = "rmi://" + args[0] + ":1099/MessageBroker";
            Admin admin = new Admin();
            MessageBrokerRemote broker = (MessageBrokerRemote) Naming.lookup(urlBroker);
            System.out.println("Admin ready and bound to " + urlBroker);

            admin.deleteQueue(broker, args[2]);
        } catch (Exception e) {
            System.err.println("Error initializing Admin: " + e.toString());
        }
    }
}
