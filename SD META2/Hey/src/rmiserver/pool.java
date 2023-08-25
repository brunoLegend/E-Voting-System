
package rmiserver;



import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

public class pool extends Thread implements Client_Interface{
    private int PORT=4322;

    private String num_pool;
    private String depart;
    private MulticastSocket socket;
    private InetAddress group;
    private interface_central h;
    private Mesa_voto server;
    private String received_voter_id;

    private static String ip;

    public pool(MulticastSocket Socket2, interface_central h, String num_pool, Mesa_voto server, InetAddress group2, String depart) throws IOException {
        this.depart=depart;
        this.h=h;
        this.num_pool=num_pool;
        this.socket = Socket2;
        this.server=server;
        this.group=group2;
        this.start();



    }
    public void run(){
        byte[] buffer9 = new byte[256];
        DatagramPacket packet9 = new DatagramPacket(buffer9, buffer9.length);

        String type;
        String message9;
        String[] Split2;

        do {
            try {


                //fica a escuta de um login
                socket.receive(packet9); //blocked here
            } catch (IOException e) {
                e.printStackTrace();
            }
            message9 = new String(packet9.getData(), 0, packet9.getLength());
            Split2 = message9.split(";|\\|");
            String received_voter_ide = null;
            //searchterminal id()

            for(int i=0;i<Split2.length;i++){
                if(Split2[i].trim().equalsIgnoreCase("voter_id")){
                    received_voter_ide=Split2[i+1];
                    this.received_voter_id=received_voter_ide;
                }
            }

            type = Split2[1].trim();
            //if(received_terminal_id!=null && received_terminal_id.equalsIgnoreCase("terminal_id")) {
            if (type.equals("login")) {
                HashMap<String, String> login_hash = conver_hashmap(Split2);
                login_hash.put("pool", num_pool);

                //send login to rmi server for verification
                HashMap<String, String> status = null;
                while (true) {
                    try {
                       status = h.process_message(login_hash, server, received_voter_id);
                        break;
                    } catch(java.rmi.ConnectException c){
                        try {
                            h = (interface_central) LocateRegistry.getRegistry(ip,7000 ).lookup("central");
                        } catch (RemoteException e) {
                            e.printStackTrace();
                        } catch (NotBoundException e) {
                            e.printStackTrace();
                        }
                    } catch(IOException e) {
                        e.printStackTrace();
                    }
                }
                //if(status.get("logged").equals("off")) continue;

                String Status_s = convert_string(status);

                //send login status
                byte[] buffer5 = Status_s.getBytes();

                DatagramPacket packet5 = new DatagramPacket(buffer5, buffer5.length, group, PORT);
                try {
                    socket.send(packet5);
                } catch (IOException e) {
                    e.printStackTrace();
                }

            } else if (type.equals("Voting")) {
                String ref = null;
                String vote = null;
                try {
                    for (int i = 0; i < Split2.length; i++) {
                        if (Split2[i].trim().equals("election_title")) {
                            ref = Split2[i + 1].trim();
                        }
                    }
                    for (int i = 0; i < Split2.length; i++) {
                        if (Split2[i].trim().equals("Vote")) {
                            vote = Split2[i + 1].trim();
                        }
                    }

                    while(true) {
                        try {
                            SimpleDateFormat ft = new SimpleDateFormat("yyyy/MM/dd HH:mm");
                            Date now = new Date();
                           h.Update_Votes(ref, vote,now,received_voter_id,0);
                            break;

                        }catch(java.rmi.ConnectException e){
                            sleep(1000);
                            h = (interface_central) LocateRegistry.getRegistry(ip,7000).lookup("central");
                        }
                    }
                } catch (RemoteException | NotBoundException | ParseException | InterruptedException e) {
                    e.printStackTrace();
                }
            }
            //}





        } while (true);






        //sends vote to rmi that atualizes the admin consoles

    }


    public static HashMap<String, String> conver_hashmap(String[] message9) {
        HashMap<String, String> login=new HashMap<>();
        for(int i=0;i<= message9.length-1;i=i+2){
            login.put(message9[i].trim(),message9[i+1].trim());

        }

        return login;
    }


    public static String convert_string(HashMap<String, String> election) {

        String result="";
        for (String i: election.keySet()){

            result=result+i+" | "+election.get(i)+"; ";


        }
        return result;
    }

    @Override
    public int Response_autent(HashMap<String, String> message) throws IOException {
        return 0;
    }
}



