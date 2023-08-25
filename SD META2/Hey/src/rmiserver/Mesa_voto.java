package rmiserver;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.net.SocketTimeoutException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.server.UnicastRemoteObject;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

import static java.lang.System.exit;


public class Mesa_voto extends UnicastRemoteObject implements Client_Interface  {

    private static int PORT=4231;
    private static int PORT2=4322;

    private static String MULTICAST_ADDRESS;

    private static String multicast_vote;
    private static interface_central h;
    //variavel a simular dados no texto.
    private int txt=2018278008;

    static MulticastSocket socket;
    static MulticastSocket socket2;
    static InetAddress group;
    static InetAddress group2;

    //construtor variables
    private static  int num_terminais;
    private  static  String depart;
    int group_id;
    private static String name;
    static int n_pool;


    static String num;
    static InputStreamReader input;
    static BufferedReader reader;

    private static String ip;


    //construtor
    public Mesa_voto(int num_terminais,String depart,int group_id) throws RemoteException {
        this.name="Server " + (long) (Math.random() * 1000);
        this.num_terminais=num_terminais;
        this.depart=depart;
        this.n_pool=0;
        this.MULTICAST_ADDRESS="224.3.2."+(group_id);
        this.multicast_vote="224.3.1."+(group_id);;




    }


    public static void main(String[] args) throws IOException, NotBoundException {

        if(args.length!=3) System.out.println("Insira como argumento o nome do departamento o numero de terminais e o ip do servidor");
        else {
            ip=args[2];

            h = (interface_central) LocateRegistry.getRegistry(ip,7000).lookup("central");


            String c = h.check_dep(args[0]);

            //se a mesa neste departamento já tiver sido criada
            if (!c.equals(" ")) {
                Mesa_voto server = new Mesa_voto(Integer.parseInt(args[1]), args[0], Integer.parseInt(c));
                go(server);
            }
            //caso contrário
            else {
                System.out.println("Access denied");
                System.out.println("A mesa no " + depart + " ainda não foi criada, por favor crie a mesa na Admin Console");
            }
        }
        exit(0);
    }


    public static void go(Mesa_voto server) throws IOException, NotBoundException {
        long counter = 0;

        try {


            //socket for finding terminals
            socket = new MulticastSocket(PORT);  // create socket without binding it (only for sending)
            group = InetAddress.getByName(MULTICAST_ADDRESS);
            socket.joinGroup(group); //join the multicast group

            //socket in another multicast for communication with terminals(logins and voting)
            socket2 = new MulticastSocket(PORT2);
            group2 = InetAddress.getByName(multicast_vote);
            socket2.joinGroup(group2); //join the multicast group


            new terminal_manager(group, num_terminais, h, depart,ip);
            new pool(socket2, h, multicast_vote, server, group2, depart);


            while (true) {

                try {
                    HashMap<String, String> identi = new HashMap<String, String>();
                    while(true) {
                        System.out.println("press 1 to let a person vote");

                        num = "";
                        input = new InputStreamReader(System.in);
                        reader = new BufferedReader(input);
                        num = reader.readLine();


                        // Closing Scanner after the use

                        if (num.equals("1")) {
                            System.out.println("Olá identifique-se por favor, qual o seu número do bi?");
                            num = reader.readLine();
                            //String current_voter_id;
                            //current_voter_id=num;
                            // Create a HashMap object with message
                            identi.put("type", "autentication");
                            identi.put("id", num);
                            identi.put("dep_name", depart);
                            break;

                        } else {
                            System.out.println("numero incorreto");
                        }
                    }

                    //ENVIA autenticacao e RECEBE se foi autenticado corretamente ou nao
                    HashMap<String, String> response;
                    while(true) {
                        try {
                            response = h.process_message(identi, server, num);
                            break;
                        } catch (java.rmi.ConnectException i) {
                            h = (interface_central) LocateRegistry.getRegistry(ip,7000).lookup("central");
                        }
                    }

                    if (response == null) System.out.println("Não Pode votar em nenhuma eleição desta mesa");

                    else if (response.size() != 0) {

                        System.out.println("Bem vindo escolha a eleição na qual pretende votar...");
                        //choose election returns the candidates of one election
                        HashMap<String, String> election_s = choose_election(response, server, num);
                        if (election_s.size() == 0) {
                            System.out.println("Não pode votar numa eleição em que já votou ou em eleições de outro nucleo...");
                        } else {

                            System.out.println("Bem vindo estamos à procura de um terminal de voto...");
                            server.FindTerminal(election_s, server, num);
                        }
                    } else {
                        System.out.println("nao registado");
                    }
                } catch (java.rmi.ConnectException ccc) {
                    try {
                        h = (interface_central) LocateRegistry.getRegistry(ip,7000).lookup("central");
                    } catch (java.rmi.ConnectException cgcc) {

                    }

                } catch (IOException e) {
                    e.printStackTrace();
                } catch (NullPointerException er) {

                }
            }

        } catch (NotBoundException e) {

        } catch (IOException e) {

        } finally {
            socket.close();
        }
    }

    /**
     * receives response with list of election, the function will list them and scan the choosen one
     * then will ask the rmi server for the lists of this election and send to the terminal
     */

    public static HashMap<String, String>  choose_election(HashMap<String, String> response, Mesa_voto server, String voter_id) throws IOException, NotBoundException {
        HashMap<String, String> election_protocol = new HashMap<String, String>();
        HashMap<Integer, String> choose_election = new HashMap<Integer, String>();
        String name;
        String election_value = "";
        String choosed="";
        int aux=0;
        int num=0;
        while(true) {
            System.out.println("Escreva o número da eleição que pretende escolher");
            for (String i : response.keySet()) {
                System.out.println(++num+" :"+i + "- " + response.get(i));
                choose_election.put(num, i);
            }

            //lê o numero da eleição escolhida
            name = reader.readLine();
            if (Integer.parseInt(name) <= 0 || Integer.parseInt(name) > num) {
                System.out.println("invalid number");
                num=0;

            }
            else break;
        }
        //type: get_election
        //election_name:x
        String protocol_m = "type | get_election ; election_name | " + choose_election.get(Integer.parseInt(name));
        election_protocol.put("type", "get_election");
        election_protocol.put("election_name", choose_election.get(Integer.parseInt(name)));

        HashMap<String, String> election;
        while(true) {
            try {
                election = h.process_message(election_protocol, server, voter_id);
                break;
            }catch (java.rmi.ConnectException o){
                h = (interface_central) LocateRegistry.getRegistry(ip,7000).lookup("central");
            }
        }



        return  election;

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
        String type=message.get("type");
        if(type.equals("permission")) {
            String registed=message.get("registed");
            if(registed.equals("yes")){



                return 1;
            }
            else{
                System.out.println("Pessoa não identificada corretamente");
                return 0;
            }
        }

        return -1;

    }





    public  void FindTerminal(HashMap<String, String> election,Mesa_voto server,String Voter_id) throws IOException {

        //pergunta se algum está disponivel
        String message = "type | " + "Find"+"; dep_mesa | "+name;
        send_message(message);

        //aguarda resposta dos disponiveis Type Terminal(resposta dos terminais)
        String[] Split = null;

        do {
            try {
                socket.setSoTimeout(3000);

                String message2 = receive_message();
                Split = message2.split(";|\\|");
            }catch (SocketTimeoutException e){
                //pergunta se algum está disponivel
                String messager = "type | " + "Find"+"; dep_mesa | "+name;
                send_message(messager);
            }
        } while (!Split[1].trim().equals("Terminal"));

        //substitui no protocol daas listas das eleições o terminal destinatário
        election.replace("dest", Split[3]);
        election.replace("pool", multicast_vote);
        election.replace("voterid",Voter_id);
        String election_s = convert_string(election);
        //envia já a lista dos candidatos da eleição para o terminal com o identificador do escolhido


        String message4 = election_s;
        send_message(message4);
        System.out.println("Por favor dirija-se ao terminal: "+Split[3]);


    }

    private static String receive_message() throws IOException {
        byte[] buffer2 = new byte[256];
        DatagramPacket packet2 = new DatagramPacket(buffer2, buffer2.length);
        socket.receive(packet2); //blocked here
        String message = new String(packet2.getData(), 0, packet2.getLength());
        return message;
    }

    private static void send_message(String message) throws IOException {
        byte[] buffer = message.getBytes();
        DatagramPacket packet = new DatagramPacket(buffer, buffer.length, group, PORT);

        socket.send(packet);
    }



    public static HashMap<String, String> conver_hashmap(String[] message9) {

        HashMap<String, String> login=new HashMap<>();
        for(int i=0;i<= message9.length-1;i=i+2){

            login.put(message9[i].trim(),message9[i+1].trim());

        }

        return login;
    }
}

//THREAD LISTENING TO TERMINALS AND CHECKING IF THEY CAN JOIN THE TABLE AND CHECKING IF THEY ARE STILL ON
class terminal_manager extends Thread{
    private static int PORT=4231;
    private static InetAddress group;
    private static MulticastSocket socket;
    private static int max_terminals;
    private static int num_on;
    private static String name;
    private static ArrayList<Integer> terminals_on;
    private static interface_central h;
    private static String ip;

    public terminal_manager(InetAddress group,int max_terminals,interface_central h,String name,String ip){
        this.name=name;
        this.group=group;
        this.max_terminals=max_terminals;
        terminals_on=new ArrayList();
        this.ip=ip;
        this.h=h;


        this.start();
    }

    public void run() {

        while (true) {

            try {
                Calendar now = Calendar.getInstance();

                long timeInSecs = now.getTimeInMillis();
                Date after = new Date(timeInSecs + (7 * 1000));
                socket = new MulticastSocket(PORT);
                socket.joinGroup(group); //join the multicast group
                String ref;
                String message;

                socket.setSoTimeout(4000);
                message = receive_message_group1();
                if (message.contains("alive")) {
                    String[] Split = message.split(";|\\|");
                    for (int i = 0; i < Split.length; i++) {
                        if (Split[i].trim().equalsIgnoreCase("id")) {
                            int t_id = Integer.parseInt(Split[i + 1].trim());
                            if (terminals_on.size() == 0) {
                                terminals_on.add(t_id);

                            } else {
                                int aux = 0;
                                for (int j = 0; j < terminals_on.size(); j++) {
                                    if (terminals_on.get(j) == t_id) {
                                        aux = 1;
                                        num_on = terminals_on.size();

                                        terminals_on.clear();
                                        terminals_on.add(t_id);
                                    }
                                }
                                if (aux == 0) {
                                    terminals_on.add(t_id);
                                }
                            }
                        }
                    }
                }
                if (message.contains("join")) {
                    if (num_on < max_terminals) {
                        String send = "type | permission; stat | yes";
                        send_message(send);
                    } else {
                        String send = "type | permission; stat | no";
                        send_message(send);
                    }
                }


            } catch (IOException e) {
                num_on=0;
                terminals_on.clear();
            }
            try {
                h.update_terminals_states(num_on,name);


            }catch (java.rmi.ConnectException i){
                try {
                    sleep(3000);
                    h = (interface_central) LocateRegistry.getRegistry(ip,7000).lookup("central");
                } catch (InterruptedException | RemoteException | NotBoundException e) {
                    e.printStackTrace();
                }

            } catch (RemoteException e) {
                e.printStackTrace();
            } catch (ParseException e) {
                e.printStackTrace();
            } catch (NotBoundException e) {
                e.printStackTrace();
            }
        }
    }
    private String receive_message_group1() throws IOException {
        byte[] buffer = new byte[256];
        DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
        socket.receive(packet); //blocked here
        String message = new String(packet.getData(), 0, packet.getLength());
        return message;
    }
    private static void send_message(String message) throws IOException {
        byte[] buffer = message.getBytes();
        DatagramPacket packet = new DatagramPacket(buffer, buffer.length, group, PORT);

        socket.send(packet);
    }
}


