package com.broker.examples;

import java.rmi.server.UnicastRemoteObject;
import com.broker.core.Message;
import com.broker.interfaces.MessageCallback;
import java.rmi.RemoteException;

public class MessageCallbackImpl extends UnicastRemoteObject implements MessageCallback {
    private static final long serialVersionUID = 1L;
    
    public MessageCallbackImpl() throws RemoteException {
        super();
    }
    
    @Override
    public void onMessage(Message message) throws RemoteException {
        System.out.println("\nConsumer received message: " + message.getContent());
    }
}