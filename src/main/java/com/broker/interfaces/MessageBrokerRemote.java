package com.broker.interfaces;

import java.rmi.Remote;
import java.rmi.RemoteException;
import com.broker.core.Message;

public interface MessageBrokerRemote extends Remote {
    void declareQueue(String name) throws RemoteException;
    void publish(String queueName, Message message) throws RemoteException;
    void subscribe(String queueName, MessageCallback callback) throws RemoteException;
}