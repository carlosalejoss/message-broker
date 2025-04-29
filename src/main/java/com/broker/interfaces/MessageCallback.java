package com.broker.interfaces;

import java.rmi.Remote;
import java.rmi.RemoteException;
import com.broker.core.Message;

public interface MessageCallback extends Remote {
    void onMessage(Message message) throws RemoteException;
}