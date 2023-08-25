package rmiserver;

import java.rmi.NotBoundException;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.text.ParseException;

public interface Interface_Admin extends Remote {

    public void getVote(String election_tile,String voted_list) throws RemoteException, NotBoundException, ParseException;
    public void terminal_states(int num_on,String mesa_name) throws RemoteException, NotBoundException, ParseException;
}
