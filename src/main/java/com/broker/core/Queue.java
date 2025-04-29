package com.broker.core;

import com.broker.interfaces.MessageCallback;
import java.rmi.RemoteException;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.LinkedList;
import java.util.Iterator;

public class Queue {
    private static final long MESSAGE_TIMEOUT_MINUTES = 5;
    private final LinkedList<Message> messages;
    private final String name;
    private final Thread cleanupThread;

    public Queue(String name) {
        this.name = name;
        this.messages = new LinkedList<>();
        this.cleanupThread = new Thread(this::cleanupExpiredMessages);
        this.cleanupThread.setDaemon(true);
        this.cleanupThread.start();
    }

    public synchronized void addMessage(Message message) {
        messages.add(message);
        notifyAll();
    }

    public synchronized Message getMessage() throws InterruptedException {
        while (messages.isEmpty()) {
            wait(); // Wait until a message is available
        }
        return messages.removeFirst();
    }

    public synchronized void deliverPendingMessages(MessageCallback callback) {
        Iterator<Message> iterator = messages.iterator();
        while (iterator.hasNext()) {
            Message message = iterator.next();
            try {
                callback.onMessage(message);
                iterator.remove(); // Remove message after successful delivery
                System.out.println("Delivered and removed message from queue " + name + ": " + message.getContent());
            } catch (RemoteException e) {
                System.err.println("Error delivering pending message: " + e.toString());
            }
        }
    }

    private void cleanupExpiredMessages() {
        while (true) {
            try {
                Thread.sleep(60000); // Check every minute
                synchronized (this) {
                    Iterator<Message> iterator = messages.iterator();
                    Instant now = Instant.now();
                    while (iterator.hasNext()) {
                        Message message = iterator.next();
                        if (ChronoUnit.MINUTES.between(message.getTimestamp(), now) >= MESSAGE_TIMEOUT_MINUTES) {
                            iterator.remove();
                            System.out.println("Removed expired message from queue " + name + ": " + message.getContent());
                        }
                    }
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                break;
            }
        }
    }

    public String getName() {
        return name;
    }

    public synchronized int size() {
        return messages.size();
    }
}