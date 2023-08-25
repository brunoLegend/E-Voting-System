package rmiserver;


import java.awt.event.KeyEvent;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.*;
import java.rmi.RemoteException;
import java.util.Timer;
import java.util.TimerTask;

public class Terminal extends Thread{

    private static String MULTICAST_ADDRESS;
    int n_escolhida=0;
    int free=0;
    String name;
    private static int PORT=4231;
    private static int PORT2=4322;
    private String pool;
    private InetAddress group2;
    private InetAddress group;
    private MulticastSocket socket;
    private MulticastSocket socket2;

    public Terminal(String multicast){
        this.name=""+(long) (Math.random() * 1000);
        System.out.println("sou o "+this.name);
        this.MULTICAST_ADDRESS=multicast;
        this.start();
    }


    public void run() {
        socket = null;
        socket2 = null;
        try {
            socket = new MulticastSocket(PORT);  // create socket and bind it
            group = InetAddress.getByName(MULTICAST_ADDRESS);
            socket.joinGroup(group); //join the multicast group


            SocketAddress server = null;
            String ref = "empty";
            String id = "";
            String Voter_id = null;

            String m = "type | join";
            //SENDS MESSAGE ASKING IF TERMINAL CAN JOIN THE TABLE(BECAUSE OF NUM MAX OF TERMINALS)

            String mess;
            send_message_group1(m);
            String [] Spl;
            //SENDS MESSAGE ASKING IF TERMINAL CAN JOIN THE TABLE(BECAUSE OF NUM MAX OF TERMINALS)


            //RECEIVES MESSAGE SHOWING IF TERMINAL CAN JOIN THE TABLE(BECAUSE OF NUM MAX OF TERMINALS)
            do {
                mess = receive_message_group1();

                Spl=mess.split(";|\\|");
            } while (!(mess.contains("permission")));
            //sees what the message has
            for (int i = 0; i < Spl.length; i++) {
                if (Spl[i].trim().equals("stat")) {
                    ref = Spl[i + 1].trim();
                    if(ref.equals("no")){
                        System.out.println("Número máximo de terminais atingido!");
                        System.exit(0);
                    }
                }
            }

            new is_alive(name,group);



            while (true) {


                //socket will be blocked while listening for call to go the work
                //Se ainda não estiver lá na pool vai ouvir o server  e se estiver free
                if (n_escolhida == 0 && free == 0) {
                    System.out.println("Terminal waiting to go to work...");
                    String message;


                    do {
                        //à espera de receber mensagem da mesa_de_voto para receber uma pessoa
                        message = receive_message_group1();
                        String[] Split0 = message.split(";|\\|");
                        ref = Split0[1];


                        //System.out.println("voter_ id: "+Voter_id);

                    } while (!(ref.trim().equals("Find")));


                    //resposta dos terminais livres com o seu id
                    String response = "type | Terminal ; id | " + this.name;
                    send_message_group1(response);


                }
                String message6;
                String[] Split;

                //RECEIVE
                String electionName = null;
                do {
                    //aguarda resposta com as listas das eleições e se foi escolhida (mesmo pacote)
                    message6 = receive_message_group1();
                    Split = message6.split(";|\\|");
                    for (int i = 0; i < Split.length; i++) {
                        if (Split[i].trim().equals("type")) {
                            ref = Split[i + 1].trim();
                        } else if (Split[i].trim().equals("dest")) {
                            id = Split[i + 1].trim();
                        } else if (Split[i].trim().equals("pool")) {
                            pool = Split[i + 1].trim();
                        } else if (Split[i].trim().equals("voterid")) {
                            Voter_id = Split[i + 1].trim();
                        } else if (Split[i].trim().equals("election_title")) {
                            electionName = Split[i + 1].trim();
                        }

                    }


                } while (!(ref.equals("item_list")));

                //se n for a escolhida
                if (!id.equals(this.name)) {
                    n_escolhida = 1;
                }
                //se for a escolhida
                if (id.equals(this.name)) {
                    n_escolhida = 0;
                    Thread n=new input(socket2, group2, pool, electionName, Split, message6, Voter_id, name);
                    Timer t=new Timer();
                    t.schedule(new TimeOutTask(n,t),120*1000);
                    n.start();
                    n.join();
                }
            }
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        } finally {
            socket.close();
        }
    }
    public String[] login() throws IOException {
        String[] data = new String[2];
        String user;
        String pass;
        InputStreamReader input;
        BufferedReader reader;
        input = new InputStreamReader(System.in);
        reader = new BufferedReader(input);
        System.out.println("Por favor introduza o seu nome de utilizador");

        user = reader.readLine();
        System.out.println("Por favor introduza a sua palavra passe");
        pass = reader.readLine();
        data[0]=user;
        data[1]=pass;
        return data;

    }

    public String receive_message_group2() throws IOException {
        byte[] buffer = new byte[256];
        DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
        socket2.receive(packet); //blocked here
        String message = new String(packet.getData(), 0, packet.getLength());
        return message;
    }

    public String[] get_lists(String message6, String[] Split) {

        Split = message6.split("_|;|\\|");

        int count = 0;
        //we get the count of elements in the list
        for (int i = 0; i < Split.length; i++) {
            if (Split[i].trim().equals("count")) {
                count = Integer.parseInt(Split[i + 1].trim());

            }
        }

        String listas[]=new String[count];

        int indice=0;
        for (int i = 0; i < Split.length; i++) {
            if (Split[i].trim().equals("name")) {
                indice = Integer.parseInt(Split[i -1].trim());
                listas[indice]=Split[i + 1].trim();

            }
        }
        return  listas;
    }

    private void vote(String[] listas,String electionName) throws IOException {
        InputStreamReader input = new InputStreamReader(System.in);
        BufferedReader reader = new BufferedReader(input);
        String num = reader.readLine();
        String Vote = null;
        int good_vote=0;
        if(Integer.parseInt(num)<listas.length){
            good_vote=1;
            Vote=listas[Integer.parseInt(num)];
        }
        else if(Integer.parseInt(num)==listas.length){
            good_vote=1;
            Vote="Branco";
        }


        if(good_vote==0) Vote="Nulo";



        send_message_group2("type | Voting; Vote | "+Vote+"; terminal_id | "+this.name+"; election_title | "+electionName);


    }
    public void send_message_group2(String message7) throws IOException {

        byte[] bufferf = message7.getBytes();

        DatagramPacket packetf = new DatagramPacket(bufferf, bufferf.length, group2, PORT2);
        socket2.send(packetf);
    }





    public static void main(String[] args) throws RemoteException {
        if(args.length!=1) {
            System.out.println("insira o nome do ip multicast como argumento");
            System.exit(0);
        }
        Terminal t = new Terminal(args[0]);

    }





    private void send_message_group1(String response) throws IOException {
        byte[] buffer2 = response.getBytes();
        group = InetAddress.getByName(MULTICAST_ADDRESS);
        DatagramPacket packet2 = new DatagramPacket(buffer2, buffer2.length, group, PORT);
        socket.send(packet2);
    }

    private String receive_message_group1() throws IOException {
        byte[] buffer = new byte[256];
        DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
        socket.receive(packet); //blocked here
        String message = new String(packet.getData(), 0, packet.getLength());
        return message;
    }


}


class TimeOutTask extends TimerTask {
    private Thread t;
    private Timer timer;
    private KeyEvent Ke;


    TimeOutTask(Thread t, Timer timer){
        this.t = t;
        this.timer = timer;

    }

    public void run() {
        if (t != null && t.isAlive()) {
            System.out.println("time's up");
            t.interrupt();
        }
    }
}


class input extends Thread{

    private static int PORT=4231;
    private static int PORT2=4322;
    private MulticastSocket socket2;
    private InetAddress group2;
    private String pool;
    private String electionName;
    private String[] Split;
    String message6;
    private String Voter_id;

    public input(MulticastSocket socket2, InetAddress group2, String pool, String electionName, String[] split, String message6, String voter_id, String name) {
        this.socket2 = socket2;
        this.group2 = group2;
        this.pool = pool;
        this.electionName = electionName;
        Split = split;
        this.message6 = message6;
        Voter_id = voter_id;
        this.name = name;
    }

    private String name;


    public void run(){
        //create new socket for login and voting
        try {
            socket2 = new MulticastSocket(PORT2);  // create socket and bind it
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            group2 = InetAddress.getByName(pool);
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
        try {
            socket2.joinGroup(group2); //join the multicast group
        } catch (IOException e) {
            e.printStackTrace();
        }


        //user logs in

        while(true) {

            String[] info = new String[0];
            try {
                info = login();
            } catch (IOException | InterruptedException e) {
                return;
            }
            String message7 = "type | login; username | " + info[0] + "; password | " + info[1]+"; voter_id | "+Voter_id;
            //sends the login for the mesa_voto to then send to rmi server for verification
            try {
                send_message_group2(message7);
            } catch (IOException e) {
                e.printStackTrace();
            }

            //RECEIVE
            String logged = null;
            String received_voter_id = "";
            String ref = null;
            do {
                //receive login confirmation or denial with status
                String message8 = null;
                try {
                    message8 = receive_message_group2();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                Split = message8.split(";|\\|");
                for (int i = 0; i < Split.length; i++) {
                    if (Split[i].trim().equals("type")) {
                        ref = Split[i + 1].trim();
                    }
                    if (Split[i].trim().equals("logged")) {
                        System.out.println("logged: " + Split[i + 1]);
                        logged=Split[i+1].trim();
                    }
                    if (Split[i].trim().equals("voter_id")) {

                        received_voter_id=Split[i+1].trim();
                    }
                }
            } while (!ref.equals("status") || (!(received_voter_id.equals(Voter_id))));

            if(logged.equals("on")) break;


        }
        //this function gets the names of the lists from the protocol format so we can show them to the user

        String listas[]=get_lists(message6,Split);
        System.out.println("Selecione o número da lista na qual pretende votar: ");

        for(int i=0;i< listas.length;i++){
            System.out.println(i+": "+listas[i]);
        }
        System.out.println(listas.length+" : (Voto em branco)");


        try {
            vote(listas,electionName);
        } catch (IOException | InterruptedException e) {
            return;
            //e.printStackTrace();
        }







    }

    public String[] login() throws IOException, InterruptedException {
        String[] data = new String[2];
        String user;
        String pass;
        InputStreamReader input;
        BufferedReader reader;
        input = new InputStreamReader(System.in);
        reader = new BufferedReader(input);
        System.out.println("Por favor introduza o seu nome de utilizador");
        while(!reader.ready()){
            sleep(200);
        }
        user = reader.readLine();
        System.out.println("Por favor introduza a sua palavra passe");
        while(!reader.ready()){
            sleep(200);
        }
        pass = reader.readLine();
        data[0]=user;
        data[1]=pass;
        return data;

    }

    private void vote(String[] listas,String electionName) throws IOException, InterruptedException {
        InputStreamReader input = new InputStreamReader(System.in);
        BufferedReader reader = new BufferedReader(input);
        while(!reader.ready()){
            sleep(200);
        }
        String num = reader.readLine();
        String Vote = null;
        int good_vote=0;
        if(Integer.parseInt(num)<listas.length){
            good_vote=1;
            Vote=listas[Integer.parseInt(num)];
        }
        else if(Integer.parseInt(num)==listas.length){
            good_vote=1;
            Vote="Branco";
        }


        if(good_vote==0) Vote="Nulo";



        send_message_group2("type | Voting; Vote | "+Vote+"; terminal_id | "+this.name+"; election_title | "+electionName);


    }

    public String receive_message_group2() throws IOException {
        byte[] buffer = new byte[256];
        DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
        socket2.receive(packet); //blocked here
        String message = new String(packet.getData(), 0, packet.getLength());
        return message;
    }

    public void send_message_group2(String message7) throws IOException {

        byte[] bufferf = message7.getBytes();

        DatagramPacket packetf = new DatagramPacket(bufferf, bufferf.length, group2, PORT2);
        socket2.send(packetf);
    }
    public String[] get_lists(String message6, String[] Split) {

        Split = message6.split("_|;|\\|");

        int count = 0;
        //we get the count of elements in the list
        for (int i = 0; i < Split.length; i++) {
            if (Split[i].trim().equals("count")) {
                count = Integer.parseInt(Split[i + 1].trim());

            }
        }

        String listas[]=new String[count];

        int indice=0;
        for (int i = 0; i < Split.length; i++) {
            if (Split[i].trim().equals("name")) {
                indice = Integer.parseInt(Split[i -1].trim());
                listas[indice]=Split[i + 1].trim();

            }
        }
        return  listas;
    }
}


class is_alive extends Thread{
    private static int PORT=4231;

    private static MulticastSocket Socket;
    private static String name;
    private static InetAddress group;

    public is_alive(String name,InetAddress group) throws IOException {
        this.group=group;
        this.name=name;
        Socket= new MulticastSocket(PORT);
        this.start();
    }

    public void run(){

        while(true){
            try {
                String message="type | alive; id | "+name;
                byte[] bufferf = message.getBytes();
                DatagramPacket packetf = new DatagramPacket(bufferf, bufferf.length, group, PORT);
                Socket.send(packetf);
                sleep(3000);


            } catch (IOException | InterruptedException e) {
                e.printStackTrace();
            }


        }

    }
}

