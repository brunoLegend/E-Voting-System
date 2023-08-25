package ws;

import rmiserver.Election;
import rmiserver.interface_central;
import rmiserver.web_interface;

import java.io.IOException;
import java.rmi.ConnectException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.concurrent.atomic.AtomicInteger;

import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import javax.websocket.OnOpen;
import javax.websocket.OnClose;
import javax.websocket.OnMessage;
import javax.websocket.OnError;
import javax.websocket.Session;

@ServerEndpoint(value = "/ws")
public class WebSocketAnnotation extends UnicastRemoteObject implements web_interface {
    private static final AtomicInteger sequence = new AtomicInteger(1);
    private final String username;
    private Session session;
    private static interface_central s;
    private int roomNumber;
    private static  int num_users_online=0;
    private static final Set<WebSocketAnnotation> users = new CopyOnWriteArraySet<>();
    private static ArrayList<String> user_names = new ArrayList<>();


    public WebSocketAnnotation() throws RemoteException {
        super();
        username = "User" + sequence.getAndIncrement();

    }

    @OnOpen
    public void start(Session session) throws RemoteException {




        this.session = session;


        users.add(this);

        try {



            s = (interface_central) LocateRegistry.getRegistry("localhost",7000).lookup("central");

            WebSocketAnnotation ws=new WebSocketAnnotation();
            s.subscribe_web(ws);

        } catch (ConnectException | NotBoundException con) {
            System.out.println("CONECTION");
        }
    }

    @OnClose
    public void end() {
        users.remove(this);
    	// clean up once the WebSocket connection is closed
    }

    @OnMessage
    public void receiveMessage(String message) throws RemoteException {
		// one should never trust the client, and sensitive HTML
        // characters should be replaced with &lt; &gt; &quot; &amp;
        System.out.println("me: "+message);
        String[] split = message.split(" ");
        if(split[0].trim().equalsIgnoreCase("new")) {
            System.out.println("someiiiiiiii");
            num_users_online++;
            String upperCaseMessage = message.toUpperCase();


            sendMessage( "Num: " + num_users_online+"("+split[2]+")");
        }
        else if(message.equalsIgnoreCase("show users")){
            sendMessage("users"+" "+ num_users_online);
        }
        else if(message.equalsIgnoreCase("user out")){
            System.out.println("outtttt");
            num_users_online--;
            sendMessage(" " + num_users_online);
        }
        else if(split[0].trim().equalsIgnoreCase("mesas")){
            System.out.println("mesas");
            System.out.println("l: "+message);
            System.out.println("split: "+split[1]);
            sendMessage(" " + split[2]+" "+split[1]);
        }
        else if(message.trim().equalsIgnoreCase("gmesas")){
            System.out.println("mesas");
            System.out.println("l: "+message);

            //sendMessage(" " + split[2]+" "+split[1]);
        }
        else if(split[0].trim().equalsIgnoreCase("vote")){
            System.out.println("l: "+message);
            Election votes_el=s.Search_election(split[1].trim());
            String vote=votes_el.getNum_votes();
            sendMessage("vote "+ split[1]+": "+vote);
        }
        else if(split[0].trim().equalsIgnoreCase("votes")){
            System.out.println("entrei");
            Election votes_el=s.Search_election(split[1].trim());
            String vote=votes_el.getNum_votes();
            sendMessage(split[2]);
            sendMessage("vote "+ split[1]+": "+vote);
        }
        else if(split[0].trim().equalsIgnoreCase("logged")){
            System.out.println("entrei");
            sendMessage("meta1 mesas "+split[1]);
        }
    }



    @OnError
    public void handleError(Throwable t) {
    	t.printStackTrace();
    }

    public void sendMessage(String text) throws RemoteException{
    	// uses *this* object's session to call sendText()
        System.out.println("s:"+users.size());
        System.out.println("enviar "+text);
        for(WebSocketAnnotation user: users){
            try {

            user.session.getBasicRemote().sendText(text);
            } catch (IOException e) {
                // clean up once the WebSocket connection is closed
                try {
                    this.session.close();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
        }



		}
    }
}
