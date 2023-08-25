package rmiserver;

import java.io.IOException;
import java.rmi.Remote;
import java.util.HashMap;

public interface Client_Interface extends Remote {

   public int Response_autent(HashMap<String,String> message) throws IOException;

}
