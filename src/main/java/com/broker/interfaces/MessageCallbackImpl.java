package com.broker.interfaces;

import java.rmi.server.UnicastRemoteObject;
import com.broker.core.Message;

public class MessageCallbackImpl extends UnicastRemoteObject implements MessageCallback {
    private static final long serialVersionUID = 1L;
    
    public MessageCallbackImpl() throws java.rmi.RemoteException {
        super();
    }
    
    @Override
    public void onMessage(Message message) throws java.rmi.RemoteException {
        System.out.println("Consumer received message: " + message.getContent());
    }
}