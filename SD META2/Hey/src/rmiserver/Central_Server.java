package rmiserver;

import ws.WebSocketAnnotation;

import javax.persistence.criteria.CriteriaBuilder;
import java.io.*;
import java.net.*;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.ExportException;
import java.rmi.server.UnicastRemoteObject;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;


public class Central_Server extends UnicastRemoteObject implements interface_central {
    static rmiserver.Client_Interface mesa_voto;
    private web_interface web_s;
    private ArrayList<Election> elections = new ArrayList<>();
    private static final long serialVersionUID = 1L;
    private ArrayList<Interface_Admin> Admins = new ArrayList<>();
    private ArrayList<Person> people = new ArrayList<>();
    private HashMap<String, Boolean> dep = new HashMap<>();
    private static String ip;
    private static ArrayList<String> admin_id = new ArrayList<>();
    private HashMap<String, Integer> mesas_on = new HashMap<>();

    public ArrayList<Person> getPeople() throws RemoteException {
        return people;
    }

    public void start_server() throws RemoteException, NotBoundException {
        Registry r = null;


        try {

            r = LocateRegistry.createRegistry(7000);
            r.rebind("central", this);


            getMessage comm_as_reader = new getMessage("main");
            comm_as_reader.start();

            System.out.println("Central server ready...");


        } catch (ExportException re) { //Server primario ja em uso
            System.out.println("Server backup pronto.");
            int i = 0;
            while (true) {
                try {

                    r = LocateRegistry.createRegistry(7000);

                    r.rebind("central", this);


                } catch (ExportException e2) {
                    if (i++ == 0) {

                        sendMessage comm_as_writer_2 = new sendMessage("backup");
                        comm_as_writer_2.start();


                    }
                }
            }
        }
    }

    protected Central_Server() throws RemoteException, NotBoundException {
        super();
        readPeopleFile();

        readElectionFile();
        dep = readMesaFile();
        new timer(elections, this);

        if (dep.size() == 0) {

            config();
        }

        //admin permissions
        Person admin = new Teacher("admin", "00000000", "", "admin");
        admin.setIs_admin(true);
        people.add(admin);


        start_server();

    }

    private void config() throws RemoteException {
        dep.put("DEI 1", false);
        dep.put("DEEC 2", false);
        dep.put("NEEC 3", false);
        dep.put("DEC 4", false);
        dep.put("DEM 5", false);
        writeMesaFile(dep);
    }

    /**
     * verifica se o utilizador existe
     *
     * @param identi
     * @param server_multi
     * @return "" se nao existir
     * @throws IOException
     */

    public HashMap<String, String> process_message(HashMap<String, String> identi, Client_Interface server_multi, String id) throws IOException {

        //vamos buscar o tipo da operação
        String type = identi.get("type");
        System.out.println("_______________________" + "Central Server received an " + type + "______________________");

        if (type.equals("autentication")) {

            HashMap<String, String> verification = new HashMap<String, String>();
            HashMap<String, String> elections = new HashMap<String, String>();
            ArrayList<Election> elections_ARR = new ArrayList<Election>();


            int aux = 0;

            System.out.println(people.size());
            for (int i = 0; i < people.size(); i++) {


                if (people.get(i).getId_card().equals(identi.get("id"))) {

                    people.get(i).setPlaceofVote(identi.get("dep_name"));
                    writePeopleFile(people.get(i), new File("people.dat"));
                    aux = 1;
                    verification.put("type", "permission");
                    verification.put("registed", "yes");

                    elections_ARR = get_elections(identi.get("dep_name"));

                    for (int k = 0; k < elections_ARR.size(); k++) {
                        elections.put(elections_ARR.get(k).getTitle(), elections_ARR.get(k).getDescriprion());
                    }

                    if (elections.size() == 0) {
                        System.out.println("não existem eleições nesta mesa...");
                        return null;
                    }
                    people.get(i).setLogged("true");
                    writePeopleFile(people.get(i), new File("people.dat"));
                    return elections;
                }
            }

            if (aux == 0) {
                verification.put("type", "permission");
                verification.put("registed", "no");
                return elections;
            }

            //mesa_voto = server_multi;
            //int veri = mesa_voto.Response_autent(verification);
            //vai à base de dados buscar eleições


            //if (veri == 1) return elections;
            //else return elections;
        } else if (type.equals("get_election")) {
            //vai buscar ao fich as listas desta eleição com nome:
            //identi.get("election_name")
            HashMap<String, String> election = new HashMap<String, String>();

            String Title = identi.get("election_name");

            Election elec = Search_election(Title);
            if (elec == null) {
                System.out.println("election null");
            }
            Person p = search_person(id);

            assert p != null;
            if (elec.getNucleo().equalsIgnoreCase(p.getDepartment()) && elec.getVoter_type().equalsIgnoreCase(p.getJob()) && !(p.getVoted().contains(elec.getTitle()))) {


                election.put("type", "item_list");
                election.put("election_title", Title);
                election.put("dest", " ");
                election.put("pool", " ");
                election.put("voterid", " ");
                election.put("item_count", Integer.toString(elec.getCandidates().size()));
                p.getVoted().add(Title);

                int aux = 0;

                for (Lista i : elec.getCandidates().keySet()) {

                    String value = "(";
                    for (int j = 0; j < i.getCandidates().size(); j++) {
                        if (j == i.getCandidates().size() - 1) value = value + " " + i.getCandidates().get(j).getName();
                        else
                            value = value + " " + i.getCandidates().get(j).getName() + ", ";
                    }
                    value = value + ")";
                    election.put("item_" + (aux++) + "_name", i.getName() + value);

                }


            }
            return election;


        } else if (type.equals("login")) {
            HashMap<String, String> login = new HashMap<String, String>();
            login.put("type", "status");
            login.put("voter_id", id);
            //verifica o login
            String username = identi.get("username");
            String password = identi.get("password");

            Person p = search_person(id);
            if (p.getName().equalsIgnoreCase(username) && p.getPassword().equalsIgnoreCase(password)) {


                //se existir faz isto

                login.put("logged", "on");
                login.put("msg", "welcome to Evoting");
            } else {
                login.put("logged", "off");

            }

            return login;
        }


        return null;
    }

    public Person search_person(String id) throws RemoteException {
        System.out.println("entrie");
        for (int i = 0; i < people.size(); i++) {
            System.out.println("ola" + people.get(i).getId_card() + " id: " + id);
            if (people.get(i).getId_card().trim().equals(id.trim())) {

                return people.get(i);
            }
        }
        System.out.println("devolvi null");
        return null;
    }

    public Election Search_election(String title) throws RemoteException {
        ArrayList<Election> elections;
        elections = readElectionFile();
        for (int i = 0; i < elections.size(); i++) {
            if (elections.get(i).getTitle().equalsIgnoreCase(title)) {
                return elections.get(i);
            }
        }
        return null;

    }


    public ArrayList<String> get_options() throws RemoteException {
        ArrayList<String> options = new ArrayList<>();
        options.add("1- Registar pessoa.");
        options.add("2- Criar eleição.");
        options.add("3- Gerir eleição.");
        options.add("4- Gerir mesas de voto.");
        options.add("5- Visualizar votos em tempo real.");
        options.add("6- Visualizar onde e quando cada pessoa votou.");
        options.add("7- Consultar detalhes de eleições passadas.");
        return options;

    }


    private ArrayList<Election> get_elections(String dep_mesa) throws RemoteException {

        ArrayList<Election> elections_restr;
        //elections = readElectionFile();

        //eleições restringidas àquela mesa
        elections_restr = get_MesaElections(elections, dep_mesa);
        return elections_restr;


    }


    public ArrayList<Election> list_elections_online(String id_user) throws RemoteException{
        ArrayList<Election> elections_restr=new ArrayList<>();
        Person p=search_person(id_user);
        System.out.println("user id: ");
        for (int i = 0; i < elections.size(); i++) {
            System.out.println(elections.get(i).getVoter_type()+" "+p.getJob()+" && "+elections.get(i).getNucleo()+"== "+p.getDepartment());
            if(elections.get(i).getState()==1 && elections.get(i).getNucleo().equalsIgnoreCase(p.getDepartment()) && elections.get(i).getVoter_type().equalsIgnoreCase(p.getJob())){
                elections_restr.add(elections.get(i));
            }
        }
        System.out.println("ok..."+elections_restr);
        return elections_restr;

    }

    private ArrayList<Election> get_MesaElections(ArrayList<Election> elections, String dep_mesa) {
        ArrayList<Election> elections_restr = new ArrayList<>();
        for (int i = 0; i < elections.size(); i++) {
            if (elections.get(i).getnome_mesa().equals(dep_mesa) && elections.get(i).getState() == 1) {
                elections_restr.add(elections.get(i));
            }
        }
        //se sair com tamanho não existem eleições naquela mesa
        return elections_restr;
    }


    public Person getUser(String username, String password) throws RemoteException {
        System.out.println("?: " + username + " " + password);
        for (int i = 0; i < people.size(); i++) {
            System.out.println("ola username " + people.get(i).getName() + " id: " + people.get(i).getPassword());
            if (people.get(i).getName().trim().equals(username) && people.get(i).getPassword().trim().equals(password)) {

                return people.get(i);
            }
        }
        return null;
    }


    public void Update_Votes(String election_tile, String voted_list, Date now, String received_voter_id,int is_online) throws RemoteException, NotBoundException, ParseException {

        //se nenhum terminal estiver ligado

        refreshElection(8, "", election_tile);
        refreshElection(11, voted_list, election_tile);

        Person p = search_person(received_voter_id);

        System.out.println(p.getName()+"votou as "+p.getTime_of_vote());
        String type = "";
        if(is_online==1){

            for(int i=0;i<people.size();i++){
                if(people.get(i).getName().equals(p.getName()) && people.get(i).getId_card().equals(p.getId_card())){
                    people.get(i).setPlaceofVote("online");
                    people.get(i).setTime_of_vote(now);
                    people.get(i).setLogged("false");
                    people.get(i).getVoted().add(election_tile);
                    type=people.get(i).getJob();
                    writePeopleFile(people.get(i), new File("people.dat"));
                }
            }

        }


        System.out.println("enviando: "+election_tile);

        for(int i=0;i<people.size();i++){
            if(people.get(i).getName().equals(p.getName()) && people.get(i).getId_card().equals(p.getId_card())){
                people.get(i).setLogged("false");
                type=people.get(i).getJob();
                writePeopleFile(people.get(i), new File("people.dat"));
            }
        }


        if(is_online==1)
            web_s.sendMessage("votes "+election_tile+" *[new"+type+"voted----online]*"+" "+Search_election(election_tile).getNum_votes());
        else{
            web_s.sendMessage("votes "+election_tile+" *[new"+type+"voted----local]*"+" "+Search_election(election_tile).getNum_votes());
        }
    }


    @Override
    public void printVotes(int Votes) throws RemoteException {
        System.out.println("Numero de votos: " + Votes);
    }


    @Override
    public void autentication(String autenticator) throws RemoteException {

    }


    public void writeMesaFile(HashMap<String, Boolean> dep) throws RemoteException {
        try {
            FileOutputStream fis = new FileOutputStream("mesa.dat", false);
            ObjectOutputStream ois = new ObjectOutputStream(fis);

            ois.writeObject(dep);

            ois.close();
            fis.close();


        } catch (FileNotFoundException ex) {
            System.out.println("Empty file");
        } catch (IOException ex) {
            System.out.println("Erro a ler ficheiro.");
        }


    }


    public String writePeopleFile(Person person, File f) {
        try {
            FileOutputStream fis = new FileOutputStream(f);
            ObjectOutputStream ois = new ObjectOutputStream(fis);

            ois.writeObject(people);

            ois.close();
            fis.close();


        } catch (FileNotFoundException ex) {
            System.out.println("Empty file");
        } catch (IOException ex) {
            System.out.println("Erro a ler ficheiro.");
        }

        return person.getName() + " stored in our database!";
    }

    public String writeElectionFile(File f) {
        try {
            FileOutputStream fis = new FileOutputStream(f);
            ObjectOutputStream ois = new ObjectOutputStream(fis);
            System.out.println("escrevi");
            ois.writeObject(elections);

            ois.close();
            fis.close();


        } catch (FileNotFoundException ex) {
            System.out.println("Empty file");
        } catch (IOException ex) {
            System.out.println("Erro a ler ficheiro.");
        }
        return " stored in our database!";
    }

    public ArrayList<Election> readElectionFile() throws RemoteException {

        try {
            FileInputStream fis = new FileInputStream("elections.dat");
            ObjectInputStream ois = new ObjectInputStream(fis);
            elections = (ArrayList<Election>) ois.readObject();


            ois.close();
            fis.close();

        } catch (FileNotFoundException ex) {
            System.out.println("Empty file");
        } catch (IOException ex) {
            System.out.println("Erro a ler ficheiro." + ex.getMessage());
        } catch (ClassNotFoundException ex) {
            System.out.println("Erro a converter objeto.");
        }
        return elections;
    }


    public HashMap<String, Boolean> readMesaFile() throws RemoteException {

        try {
            FileInputStream fis = new FileInputStream("mesa.dat");
            ObjectInputStream ois = new ObjectInputStream(fis);
            dep = (HashMap<String, Boolean>) ois.readObject();


            ois.close();
            fis.close();

        } catch (FileNotFoundException ex) {
            System.out.println("Empty file");
        } catch (IOException ex) {
            System.out.println("Erro a ler ficheiro.");
        } catch (ClassNotFoundException ex) {
            System.out.println("Erro a converter objeto.");
        }
        return dep;
    }


    public void readPeopleFile() throws RemoteException {
        try {
            FileInputStream fiss = new FileInputStream("people.dat");
            ObjectInputStream oiss = new ObjectInputStream(fiss);
            people = (ArrayList<Person>) oiss.readObject();


            oiss.close();
            fiss.close();

        } catch (FileNotFoundException ex) {
            System.out.println("Empty file");
        } catch (IOException ex) {
            System.out.println("Erro a ler ficheiro." + ex.getMessage());
        } catch (ClassNotFoundException ex) {
            System.out.println("Erro a converter objeto.");
        }

    }


    public void count_votes(Election election) throws RemoteException, ParseException {
        String vencedora = winner(election);
        refreshElection(12, vencedora, election.getTitle());
        refreshElection(10, election.getNum_votes(), election.getTitle());
    }

    private String winner(Election election) throws RemoteException, ParseException {
        int max = -1;
        String winner = null;
        for (Lista i : election.getCandidates().keySet()) {
            if (election.getCandidates().get(i) > max) {
                winner = i.getName();
            }
        }

        return winner;
    }


    public String refreshElection(int param, String new_param, String old_title) throws RemoteException, ParseException {
        System.out.println("Refreshing elec");
        for (int i = 0; i < this.elections.size(); i++) {
            if (elections.get(i).getTitle().equals(old_title)) {
                if (param == 1) {
                    elections.get(i).setTitle(new_param);
                } else if (param == 2) {
                    SimpleDateFormat ft = new SimpleDateFormat("yyyy/MM/dd HH:mm");
                    elections.get(i).setInit_date(ft.parse(new_param));
                } else if (param == 3) {
                    SimpleDateFormat ft = new SimpleDateFormat("yyyy/MM/dd HH:mm");
                    elections.get(i).setEnd_date(ft.parse(new_param));
                } else if (param == 4) {
                    elections.get(i).setDescriprion(new_param);
                } else if (param == 5) {
                    System.out.println("estou 5");
                    for (Lista j  :elections.get(i).getCandidates().keySet()) {
                        System.out.println(j.getName()+" == "+new_param);
                        if (j.getName().equalsIgnoreCase(new_param)) {
                            elections.get(i).getCandidates().keySet().remove(j);
                        }
                    }
                } else if (param == 6) {
                    Lista list = new Lista(new_param);
                    elections.get(i).addCandidate(list);
                } else if (param == 7) {
                    elections.get(i).setNome_Mesa(new_param);
                } else if (param == 8) {
                    if (elections.get(i).getState() == 1) {
                        int aux = Integer.parseInt(elections.get(i).getNum_votes()) + 1;
                        elections.get(i).setNum_votes(Integer.toString(aux));
                    }
                } else if (param == 9) {
                    elections.get(i).setState(Integer.parseInt(new_param));
                } else if (param == 10) {
                    elections.get(i).setFinal_votes(Integer.parseInt(new_param));
                } else if (param == 11) {
                    String[] Split = new_param.split("\\(");
                    Lista list = new Lista(Split[0]);
                    elections.get(i).add_vote(list);
                } else if (param == 12) {
                    elections.get(i).setWinner(new_param);
                } else if (param == 13) {
                    for (Lista l : elections.get(i).getCandidates().keySet()) {
                        l.getCandidates().remove(Integer.parseInt(new_param));
                        //for(Candidate cand: l.getCandidates()){

                        //if(cand.getName().equals(new_param)) l.getCandidates().remove(cand);
                        //}
                    }
                } else if (param == 14) {
                    elections.get(i).setVoter_type(new_param);
                }

            }

        }
        return old_title + "has been changed and " + writeElectionFile(new File("elections.dat"));
    }

    public String refreshList(int type, Election el, String nome, String cc, String list) throws RemoteException {
        System.out.println("refreshing list");
        if (type == 1) {

            for (Lista l : el.getCandidates().keySet()) {
                if (l.getName().equals(list)) {
                    l.removeCandidates(new Candidate(nome, cc));
                }
            }
        } else if (type == 2) {

            for (Lista l : el.getCandidates().keySet()) {
                System.out.println(l.getName()+" == "+list);
                if (l.getName().equals(list)) {
                    l.addCandidates(new Candidate(nome, cc));


                }

            }
        }
        int i = 0;

        for (Election aux : elections) {

            if (aux.getTitle().equals(el.getTitle())) {
                elections.set(i, el);
            }
            i++;
        }
        System.out.println("aqyuiiiiiii");
        writeElectionFile(new File("elections.dat"));
        System.out.println("aqyuiiiiiii22222222222");
        return el.getTitle() + "has been changed and ";

    }

    public int verify_list(Election e, String name) throws RemoteException {
        for (Lista l : e.getCandidates().keySet()) {
            if (l.getName().equalsIgnoreCase(name)) return 1;
        }
        return 0;
    }

    public Lista search_list(Election e,String name) throws RemoteException {
        for (Lista l : e.getCandidates().keySet()) {
            System.out.println("search"+l.getName()+" == "+name);
            if (l.getName().equalsIgnoreCase(name)) return l;
        }
        return null;
    }

    @Override
    public void subscribe_web(web_interface ws) throws RemoteException{
        System.out.println("ola");
        web_s=ws;
        System.out.println("ola2");
    }

    public int verify_candidate(Lista l, String cc) throws RemoteException {
        for (Candidate c : l.getCandidates()) {
            if (c.getId_card().equals(cc)) return 1;
        }
        return 0;
    }


    public void Register_admin(Interface_Admin Ad) throws RemoteException, NotBoundException {
        int aux = 0;
        for (Interface_Admin i : Admins) {
            if (Ad.hashCode() == i.hashCode()) {
                aux = 1;

            }
        }
        if (aux == 0) {
            Ad = (Interface_Admin) LocateRegistry.getRegistry(ip, 1100).lookup("admin");
        }
        Admins.add(Ad);

    }




    public String regist_new_election(Election election) throws RemoteException {
        elections.add(election);

        return election + writeElectionFile(new File("elections.dat"));
    }


    public String regist_person(Person person) throws RemoteException {
        people.add(person);
        return writePeopleFile(person, new File("people.dat"));
    }


    public void update_terminals_states(int num_on, String mesa_name) throws RemoteException, NotBoundException, ParseException {
        System.out.println("updating tables");
        if(mesas_on.containsKey(mesa_name)==false){
            mesas_on.put(mesa_name,num_on);
        }
        else{
            mesas_on.replace(mesa_name,num_on);
        }
           String aux = "";
        for(String i:mesas_on.keySet()){
            aux=aux+i;
            aux=aux+": "+mesas_on.get(i)+"\n";
        }
            //Admins.terminal_states(num_on, mesa_name);
            System.out.println("ud");
            int logged=0;
            for(Person k:people ){
                System.out.println(k);
                if(k.getLogged().equals("true")){
                    logged++;
                }
            }
            if(web_s!=null) {
                web_s.sendMessage("mesas " + aux);
                web_s.sendMessage("logged "+logged);
            }

    }


    public static void main(String[] args) throws RemoteException {

        if (args.length != 1) {
            System.out.println("Insira Ip");
            System.exit(0);
        }
        ip = args[0];


        try {
            System.setProperty("java.rmi.server.hostname", ip);
            interface_central server = new Central_Server();

        } catch (Exception re) {
            System.out.println("Exception in : " + re);
        }
    }

    public ArrayList<Election> getElections() throws RemoteException {
        return elections;
    }

    public String check_dep(String nome) throws RemoteException {
        dep = readMesaFile();
        for (String i : dep.keySet()) {
            String Split[] = i.split(" ");
            if (nome.equals(Split[0])) {
                if (dep.get(i) == true) {
                    return Split[1];
                } else {
                    return " ";
                }
            }

        }
        return " ";
    }


    public String exists_dep(String nome) throws RemoteException {
        dep = readMesaFile();
        for (String i : dep.keySet()) {
            String Split[] = i.split(" ");
            if (nome.equals(Split[0])) {
                return "true";
            }
        }
        return "false";
    }
}

class getMessage extends Thread {
    String name;

    getMessage(String threadname) {
        this.name = threadname;
    }

    public void run() {

        byte[] receive = new byte[1000];
        DatagramPacket DpReceive = null;
        DatagramPacket DpSend = null;
        DatagramSocket ds = null;
        try {
            ds = new DatagramSocket(3334);
            while (true) {

                // Step 2 : create a DatgramPacket to receive the data.
                DpReceive = new DatagramPacket(receive, receive.length);

                // Step 3 : revieve the data in byte buffer.
                ds.receive(DpReceive);

               // Exit the server if the client sends "bye"
                byte[] answer = new byte[1000];
                answer = "on".getBytes();
                // Clear the buffer after every message.
                DpSend = new DatagramPacket(answer, answer.length, DpReceive.getAddress(), DpReceive.getPort());
                while (true) ds.send(DpSend);


            }
        }catch (BindException b){

        } catch(SocketException e) {
            e.printStackTrace();
        }catch (IOException e) {System.out.println("IO: " + e.getMessage());
        }finally {
            if(ds!=null)
                ds.close();
        }
    }
}

class sendMessage extends Thread{
    String name;
    sendMessage(String threadname){
        this.name = threadname;
    }
    public void run(){
        DatagramSocket aSocket = null;
        String s;
        try {
            aSocket = new DatagramSocket();

            while (true) {
                byte[] buffer = new byte[1000];
                buffer = "check".getBytes();
                DatagramPacket request = new DatagramPacket(buffer, buffer.length, InetAddress.getByName("localhost"), 3334);
                aSocket.send(request);
                byte[] buf = new byte[1000];
                DatagramPacket reply = new DatagramPacket(buf, buf.length);

                aSocket.setSoTimeout(5000);   // set the timeout in millisecounds.

                while (true) {        // receive data until timeout
                    try {
                        aSocket.receive(reply);
                        String rcvd = "rcvd from " + reply.getAddress() + ", " + reply.getPort() + ": " + new String(reply.getData(), 0, reply.getLength());
                        //System.out.println(rcvd);
                    } catch (SocketTimeoutException e) {
                        // timeout exception.
                        aSocket.close();
                        System.out.println("Server PrimÃ¡rio down! A ligar...");
                        getMessage get = new getMessage("another");
                        get.start();
                        System.out.println("return");

                        return;
                        //aSocket.close();

                        //Fich config  guardar ips
                    }

                }

            }
        }catch (SocketException e){System.out.println("Socket: " + e.getMessage());
        }catch (IOException e) {System.out.println("IO: " + e.getMessage());
        }finally {
            if(aSocket != null) aSocket.close();}
    }
}


class timer extends Thread{
    ArrayList<Election> elections=new ArrayList<>();
    interface_central s;
    public timer( ArrayList<Election> elections,interface_central s) throws RemoteException, NotBoundException {
        this.elections=elections;
        this.s=s;
        this.start();
    }
    public void run() {
        while(true) {
            try {
                elections=s.readElectionFile();
            } catch (RemoteException e) {
                e.printStackTrace();
            }
            SimpleDateFormat ft = new SimpleDateFormat("yyyy/MM/dd HH:mm");
            Date now = new Date();
            for (int i = 0; i < elections.size(); i++) {
                try {
                    if ((elections.get(i).getState()!=1  && elections.get(i).getInit_date().before(ft.parse(ft.format((now))))  && elections.get(i).getEnd_date().after(ft.parse(ft.format((now))))) || (elections.get(i).getState()!=1  && elections.get(i).getInit_date().equals(ft.parse(ft.format((now)))))){
                        System.out.println("INFO: election : "+elections.get(i).getTitle()+" has started");
                        //set the state to 1(is on)
                        //elections.get(i).setState(1);
                        s.refreshElection(9,"1",elections.get(i).getTitle());
                    }
                } catch (ParseException | RemoteException e) {
                    e.printStackTrace();
                }
                try {
                    if(elections.get(i).getState()!=2 && elections.get(i).getEnd_date().before(ft.parse(ft.format((now)))) || elections.get(i).getState()!=2 && elections.get(i).getEnd_date().equals(ft.parse(ft.format((now))))){
                        System.out.println("INFO: election : "+elections.get(i).getTitle()+" has ended");
                        //set the state to 2(has ended)
                        s.refreshElection(9,"2",elections.get(i).getTitle());
                        s.count_votes(elections.get(i));
                    }
                } catch (ParseException | RemoteException e) {
                    e.printStackTrace();
                }
            }
            try {
                sleep(59000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}





