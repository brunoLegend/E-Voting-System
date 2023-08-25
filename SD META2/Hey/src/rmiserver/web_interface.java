package rmiserver;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface web_interface extends Remote {
    public void sendMessage(String text) throws RemoteException;

}
