package rmiserver;

import java.io.IOException;
import java.rmi.ConnectException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.TimeUnit;

public class AdminConsole  extends UnicastRemoteObject implements Interface_Admin {
    public  interface_central getS() {
        return s;
    }

    private static interface_central s;
    private static AdminConsole AdminInt;
    HashMap<String, Integer> mesas = new HashMap<>();
    HashMap<String, Boolean> dep = new HashMap<>();
    private ArrayList<Election> elections = new ArrayList<>();
    private static String ip;


    private boolean Votes_on;
    private  boolean Status_on;

    public AdminConsole() throws RemoteException, NotBoundException {

        this.Votes_on=false;
        this.Status_on=false;
        //lista de departamentos possiveis de receber eleição
        while (true) {
            try {
                s = (interface_central) LocateRegistry.getRegistry(ip,7000).lookup("central");
                dep = s.readMesaFile();
                elections=s.readElectionFile();

                break;
            } catch (ConnectException con) {

            }
        }



    }


    public static void main(String args[]) throws IOException, NotBoundException, InterruptedException {

		/* This might be necessary if you ever need to download classes:
		System.getProperties().put("java.security.policy", "policy.all");
		System.setSecurityManager(new RMISecurityManager());

		*/
        if(args.length!=1) {
            System.out.println("Insira o ip");
            System.exit(0);
        }
        ip=args[0];
        System.setProperty("java.rmi.server.hostname", ip);

        AdminInt = new AdminConsole();
        Registry r = LocateRegistry.createRegistry(1100);
        r.rebind("admin",AdminInt);
        new admins_regist(s,AdminInt,ip);
        //Register Admin
        //s.printVotes(1);
        AdminInt.menu(s);


    }






    public void menu(interface_central s) throws IOException {
        int c;
        try {
            while (true) {
                s = (interface_central) LocateRegistry.getRegistry(ip,7000).lookup("central");

                Votes_on = false;
                System.out.println("-------------------- ADMIN CONSOLE ------------------");
                System.out.println("\t1 - Registar pessoa.");
                System.out.println("\t2 - Criar eleição");
                System.out.println("\t3 - Gerir Eleição...");
                System.out.println("\t4 - Gerir mesas de voto");
                System.out.println("\t5 - Visualizar Votos em tempo real");
                System.out.println("\t6 - Visualizar onde e quando cada pessoa votou");
                System.out.println("\t7 - Consultar detalhes de eleições passadas");
                System.out.println("-------------------- ADMIN CONSOLE ------------------");
                Scanner sc = new Scanner(System.in);
                c = sc.nextInt();
                if (c == 1) {
                    sc.nextLine();
                    System.out.print("\t\tNome: ");
                    String name = sc.nextLine();
                    System.out.print("\n\t\tCartao de Cidadao: ");
                    String cc = sc.nextLine();
                    System.out.print("\n\t\tDepartamento: ");
                    String dep = sc.nextLine();
                    System.out.print("\n\t\t Password: ");
                    String pwd = sc.nextLine();
                    System.out.println("\n\t\tFunção (Estudante/Funcionario/Docente): ");
                    String type = sc.nextLine();
                    Person person = null;
                    if (type.equalsIgnoreCase("estudante")) {
                        person = new Student(name, cc, dep, pwd);
                    } else if (type.equalsIgnoreCase("funcionario")) {
                        person = new Worker(name, cc, dep, pwd);
                    } else if (type.equalsIgnoreCase("docente")) {
                        person = new Teacher(name, cc, dep, pwd);
                    }
                    while(true){
                        try{
                            System.out.println(s.regist_person(person));
                            break;
                        } catch (ConnectException ex) {

                            s = (interface_central) LocateRegistry.getRegistry(ip,7000).lookup("central");
                        }
                    }
                } else if (c == 2) {
                    String title;
                    String nucleo;
                    String votantes;
                    String init_date;
                    Date init;
                    String end_date;
                    Date end;
                    String description;
                    String mesa;

                    sc.nextLine();
                    System.out.print("\t\tDepartamento dos votantes: ");
                    nucleo = sc.nextLine();
                    System.out.print("\t\tFunção dos votantes (Estudante/Funcionario/Docente): ");
                    votantes = sc.nextLine();
                    System.out.print("\t\tTitulo da eleição: ");
                    title = sc.nextLine();
                    while(true) {
                        System.out.print("\n\t\tData de inicio da eleição: ");
                        init_date = sc.nextLine();
                        init = verify_date_ini(init_date);
                        if (init != null) break;
                    }
                    while(true) {
                        System.out.print("\n\t\tData de fim da eleição: ");
                        end_date = sc.nextLine();
                        end = verify_date_end(end_date, init);
                        if(end!=null) break;
                    }
                    System.out.print("\t\tmesa da eleição: ");
                    mesa = sc.nextLine();
                    System.out.print("\t\tDescrição da eleição: ");
                    description = sc.nextLine();

                    Election election = new Election(title, init, end, description, mesa, votantes.toLowerCase(), nucleo);
                    while(true){
                        try{
                            System.out.println(s.regist_new_election(election));
                            break;
                        } catch (ConnectException ex) {

                            s = (interface_central) LocateRegistry.getRegistry(ip,7000).lookup("central");
                        }
                    }
                    //s.readElectionFile();

                } else if (c == 3) {
                    while(true) {
                        try {
                            s = (interface_central) LocateRegistry.getRegistry(ip,7000).lookup("central");

                            int c1;
                            System.out.println("Eleição: ");
                            ArrayList<Election> elections = s.readElectionFile();
                            for (int i = 0; i < elections.size(); i++) {
                                System.out.print("\t" + (i + 1) + " - " + elections.get(i).getTitle());
                            }
                            //try catch p inteiro
                            c1 = sc.nextInt();
                            System.out.println(c1);
                            Election elec = elections.get(c1 - 1);
                            System.out.println(elec);
                            System.out.println(elections.get(c1 - 1));
                            System.out.println("a) Titulo: " + elections.get(c1 - 1).getTitle());
                            System.out.println("d) Descrição: " + elections.get(c1 - 1).getDescriprion());
                            System.out.println("e) Nome da mesa: "+elections.get(c1 - 1).getnome_mesa());
                            System.out.println("f) Função do eleitor: "+elections.get(c1 - 1).getVoter_type());
                            System.out.println("g) Listas Candidatas: ");
                            Election selected_election = elections.get(c1-1);
                            elections.get(c1 - 1).printCandidates(1);
                            while (true) {
                                int fail_op=0;
                                System.out.println("(9 to quit) Choose 'a','d','e','f','g' to change: ");
                                //try catch
                                sc.nextLine();
                                String c2 = sc.nextLine();
                                String new_one = null;
                                try {

                                    if (c2.equals("a")) {
                                        fail_op=1;
                                        System.out.print("Novo título");
                                        new_one = sc.nextLine();
                                        s.refreshElection(1, new_one, elections.get(c1 - 1).getTitle());
                                        break;
                                    } else if (c2.equals("b")) {
                                        fail_op=2;
                                        System.out.print("Nova data de inicio");
                                        new_one = sc.nextLine();
                                        s.refreshElection(2, new_one, elections.get(c1 - 1).getTitle());
                                        break;
                                    } else if (c2.equals("c")) {
                                        fail_op=3;
                                        System.out.print("Nova data de fim");
                                        new_one = sc.nextLine();
                                        s.refreshElection(3, new_one, elections.get(c1 - 1).getTitle());
                                        break;
                                    } else if (c2.equals("d")) {
                                        fail_op=4;
                                        System.out.print("Nova descrição");
                                        new_one = sc.nextLine();
                                        s.refreshElection(4, new_one, elections.get(c1 - 1).getTitle());
                                        break;
                                    } else if(c2.equalsIgnoreCase("e")) {
                                        System.out.print("Nova mesa de eleição ");
                                        new_one = sc.nextLine();
                                        s.refreshElection(7, new_one, elections.get(c1 - 1).getTitle());
                                        break;

                                    }  else if(c2.equalsIgnoreCase("f")) {
                                        System.out.print("Nova função do eleitor ");
                                        new_one = sc.nextLine();
                                        s.refreshElection(14, new_one, elections.get(c1 - 1).getTitle());
                                        break;
                                    }else if (c2.equalsIgnoreCase("g")) {
                                        while (true) {
                                            System.out.print("Remover {Número} / Adicionar.... / Editar {Nome}\n-");
                                            //try catch
                                            String c3 = sc.nextLine();
                                            String[] action;
                                            action = c3.split(" ", 2);
                                            if (action[0].equals("Remover")) {
                                                String num = action[1];
                                                while(true) {
                                                    try{
                                                        s.refreshElection(5, num, elections.get(c1 - 1).getTitle());
                                                        break;
                                                    }catch(ConnectException e){
                                                        s = (interface_central) LocateRegistry.getRegistry(ip,7000).lookup("central");
                                                    }

                                                }
                                                break;
                                            } else if (action[0].equalsIgnoreCase("Adicionar")) {

                                                while(true) {
                                                    System.out.println("Insira o nome da nova lista candidata a esta eleição");
                                                    String scan = sc.nextLine();
                                                    int erro = 0;
                                                    while (true) {
                                                        try {
                                                            if (s.verify_list(elections.get(c1 - 1), scan) == 1) {
                                                                System.out.println("Lista já existe, tente novamente");
                                                                erro = 1;
                                                            } else
                                                                s.refreshElection(6, scan, elections.get(c1 - 1).getTitle());
                                                            break;
                                                        } catch (ConnectException e) {
                                                            s = (interface_central) LocateRegistry.getRegistry(ip,7000).lookup("central");
                                                        }
                                                    }
                                                    if(erro == 0) break;
                                                }
                                                break;
                                            } else if (action[0].equalsIgnoreCase("Editar")) {
                                                String name = action[1];
                                                HashMap<Lista, Integer> list = null;
                                                list = selected_election.getCandidates();
                                                Lista ll = null;
                                                for(Lista l1: list.keySet()){
                                                    if(l1.getName().equals(name))  ll = l1;
                                                }
                                                if(ll == null){
                                                    System.out.println("Lista inexistente");
                                                    continue;
                                                }
                                                int k = 0;
                                                for (Candidate ca: ll.getCandidates()){
                                                    k++;
                                                    System.out.println((k) + " - " + ca.getName());
                                                }
                                                System.out.println("Remover {nome cc} / Adicionar {nome cc} (9 to quit)");
                                                String another = sc.nextLine();
                                                String[] spl = another.split(" ");

                                                while(true) {
                                                    if (spl[0].equalsIgnoreCase("REMOVER")) {
                                                        String to_remove_name = spl[1];
                                                        String to_remove_cc = spl[2];
                                                        //to remove - Nome da pessoa(candidato)
                                                        while (true) {
                                                            try {
                                                                s.refreshList(1, selected_election, to_remove_name, to_remove_cc, ll.getName());
                                                                System.out.println("..........................................");
                                                                break;
                                                            } catch (ConnectException ee) {
                                                                s = (interface_central) LocateRegistry.getRegistry(ip,7000).lookup("central");
                                                            } catch (RemoteException ec) {
                                                            }
                                                        }
                                                        break;
                                                    } else if (spl[0].equalsIgnoreCase("Adicionar")) {

                                                        String to_add_name = spl[1];
                                                        String to_add_cc = spl[2];

                                                        while (true) {
                                                            try {
                                                                if (s.verify_candidate(ll, to_add_cc) == 0)
                                                                    s.refreshList(2, selected_election, to_add_name, to_add_cc, ll.getName());
                                                                else {
                                                                    System.out.println("Já existe");
                                                                }
                                                                break;
                                                            } catch (ConnectException ee) {
                                                                s = (interface_central) LocateRegistry.getRegistry(ip,7000).lookup("central");
                                                            }
                                                        }
                                                        break;
                                                    } else if (spl[0].equalsIgnoreCase("9")) break;
                                                    else System.out.println("Erro! Tente novamente.");
                                                }
                                            } else if(action[0].equals("9")){
                                                break;
                                            } else {
                                                System.out.println("Introduza uma opção válida");
                                            }
                                        }
                                        break;
                                    } else if (c2.equals("9")) {
                                        //checks if server is still on
                                        break;
                                    } else {
                                        System.out.println("Por favor insira uma opcão valida!");
                                    }
                                } catch (ConnectException e) {
                                    TimeUnit.SECONDS.sleep(1);
                                    s = (interface_central) LocateRegistry.getRegistry(ip,7000).lookup("central");

                                    //resume the state of the Edit election if server goes down
                                    resume_fail_op(fail_op,new_one,elections.get(c1 - 1).getTitle());



                                }
                            }
                            break;
                        }catch (ConnectException e) {
                            TimeUnit.SECONDS.sleep(3);
                            s = (interface_central) LocateRegistry.getRegistry(ip,7000).lookup("central");
                            s.Register_admin((Interface_Admin) AdminInt);

                        }
                    }
                } else if (c == 4) {
                    ArrayList<Election> elections = null;
                    while(true) {
                        try {
                            elections = s.getElections();
                            break;
                        } catch (ConnectException exp) {
                            s = (interface_central) LocateRegistry.getRegistry(ip,7000).lookup("central");
                        }
                    }
                    System.out.println("Carregue : ");
                    System.out.println("0 - Visualizar estado das mesas de voto");
                    System.out.println("1 - Criar mesa de voto");
                    System.out.println("2 - Associar mesa de voto a uma eleição");
                    System.out.println("3 - Desassociar mesa de voto a uma eleição");
                    int c4 = sc.nextInt();

                    if(c4==0){
                        Status_on=true;
                        String c4_1;
                        System.out.println("press <<0>> to exit");
                        while (!(c4_1 = sc.next()).equals("0")) {

                        }
                        Status_on=false;


                    }

                    else if (c4 == 1) {
                        while (true) {
                            System.out.print("Nome Departamento: ");
                            list_dep();
                            String new_one = sc.next();
                            int aux=0;
                            for(String i:dep.keySet()){
                                String [] Split=i.split(" ");
                                if(Split[0].equals(new_one.toUpperCase())){
                                    aux=1;
                                    if (dep.get(i) == true) {
                                        System.out.println("já existe uma mesa neste departamento");
                                        break;

                                    }
                                    else {


                                        //criar mesa

                                        dep.replace(i, true);
                                        //escrever fich
                                        while(true){
                                            try{
                                                s.writeMesaFile(dep);
                                                break;
                                            } catch (ConnectException ex) {

                                                s = (interface_central) LocateRegistry.getRegistry(ip,7000).lookup("central");
                                            }
                                        }

                                        System.out.println("New table at " + new_one.toUpperCase() + "created");
                                        break;
                                    }
                                }
                            }
                            if(aux==0)
                                System.out.println("Nome de departamento inválido");
                            break;


                        }
                    } else if (c4 == 2) {

                        int ex = list_elections(elections, 0);
                        if (ex == 1) {
                            while (true) {
                                Election el = choose_election(elections);
                                System.out.println("escolha uma mesa de voto: ");
                                list_dep();


                                String new_one = sc.next();
                                int aux=0;
                                for(String i:dep.keySet()) {
                                    String[] Split = i.split(" ");

                                    if (Split[0].equals(new_one)) {
                                        aux = 1;
                                        while (true) {
                                            try {
                                                s.refreshElection(7, new_one.toUpperCase(), el.getTitle());
                                                break;
                                            } catch (ConnectException exi) {

                                                s = (interface_central) LocateRegistry.getRegistry(ip,7000).lookup("central");
                                            }
                                        }
                                    }
                                }

                                if(aux==0) {
                                    System.out.println("Mesa de voto com esse nome não existe");
                                }else {
                                    break;
                                }
                            }

                        }

                    } else if (c4 == 3) {

                        int ex = list_elections(elections, 1);
                        if (ex == 1) {

                            Election el = choose_election(elections);
                            while(true){
                                try{
                                    s.refreshElection(7, "", el.getTitle());
                                    break;
                                } catch (ConnectException exe) {

                                    s = (interface_central) LocateRegistry.getRegistry(ip,7000).lookup("central");
                                }
                            }
                        }

                    }


                } else if (c == 5) {
                    s = (interface_central) LocateRegistry.getRegistry(ip,7000).lookup("central");
                    ArrayList<Election> elections;
                    while(true){
                        try{
                            elections = s.getElections();
                            break;
                        } catch (ConnectException ex) {

                            s = (interface_central) LocateRegistry.getRegistry(ip,7000).lookup("central");
                        }
                    }


                    System.out.println("<<pressione 0 para sair>>");
                    Votes_on = true;

                    for (String i : dep.keySet()) {
                        System.out.println("----------------------------");
                        String [] Split=i.split(" ");
                        System.out.println("Mesa " + Split[0]);

                        for (int j = 0; j < elections.size(); j++) {
                            if (elections.get(j).getnome_mesa().equalsIgnoreCase(Split[0])) {
                                System.out.print("Votos (" + elections.get(j).getTitle() + ") :" + elections.get(j).getNum_votes());
                                if(elections.get(j).getState()==0) System.out.println(" (não iniciada)");
                                else if(elections.get(j).getState()==1) System.out.println(" (a decorrer)");
                                else if(elections.get(j).getState()==2) System.out.println(" (terminada)");
                            }
                        }
                        System.out.println("----------------------------");
                    }


                    String c5;
                    while (!(c5 = sc.next()).equals("0")) {

                    }
                }else if(c==6){
                    ArrayList<Person> people;
                    while(true) {
                        try {
                            people = s.getPeople();
                            break;
                        }catch (ConnectException b){
                            s = (interface_central) LocateRegistry.getRegistry(ip,7000).lookup("central");
                        }
                    }
                    for(int i=0;i<s.getPeople().size();i++){
                        System.out.println(people.get(i).getName()+": "+people.get(i).getPlaceofVote()+" "+people.get(i).getTime_of_vote());
                    }
                } else if(c==7){
                    ArrayList<Election> elections=s.getElections();
                    for(int i=0;i<elections.size();i++){

                        if(elections.get(i).getState()==2){
                            System.out.println("_________________ELEIÇÃO: "+elections.get(i).getTitle());
                            System.out.println("Numero total de votos "+elections.get(i).getFinal_votes());
                            System.out.println("Votos brancos: "+elections.get(i).getBrancos());
                            System.out.println("Votos nulos: "+elections.get(i).getNulos());
                            System.out.println("Lista vencedora: "+elections.get(i).getWinner());
                            for(Lista l: elections.get(i).getCandidates().keySet()){
                                System.out.println(l.getName()+"com :"+elections.get(i).getCandidates().get(l)+" votos");
                            }

                        }
                    }
                }
            }

        } catch (Exception e) {

            e.printStackTrace();
        }

    }

    private void resume_fail_op(int fail_op,String new_one,String title) throws RemoteException, ParseException {
        if(fail_op==1){
            s.refreshElection(1, new_one, title);
        }else if(fail_op==2){
            s.refreshElection(2, new_one, title);
        }else if(fail_op==3){
            s.refreshElection(3, new_one, title);
        }else if(fail_op==4){
            s.refreshElection(3, new_one, title);
        }
    }


    public Date verify_date_end(String end_date,Date init) throws ParseException {
        SimpleDateFormat ft = new SimpleDateFormat("yyyy/MM/dd HH:mm");
        Date now = new Date();

        Date t;
        try {
            t=ft.parse(end_date);

            if(t.before(now) && t.before(init)){
                System.out.println("Data não pode ser inferior à atual ou à antiga");
                return null;
            }
        }catch (ParseException e){
            System.out.println("Data no formato errado, introduza uma data no formato: yyyy/MM/dd HH:mm");
            return null;
        }
        return t;



    }


    private Date verify_date_ini(String init_date)  {

        SimpleDateFormat ft = new SimpleDateFormat("yyyy/MM/dd HH:mm");
        Date now=new Date();



        Date t;
        try {
            t=ft.parse(init_date);
            if(t.before(now)){
                System.out.println("Data não pode ser inferior à atual");
                return null;
            }
        }catch (ParseException e){
            System.out.println("Data no formato errado, introduza uma data no formato: yyyy/MM/dd HH:mm");
            return null;
        }
        return t;



    }

    private Election choose_election(ArrayList<Election> elections) {
        Election el=null;
        while(true) {
            System.out.println("Escolha o número da eleição que pretende associar: ");
            Scanner sc = new Scanner(System.in);

            int num = sc.nextInt();

            if (num < 0 || num >= elections.size()) {
                System.out.println("Número inválido");
            } else {
                el = elections.get(num);
                break;
            }

        }
        return el;
    }

    private int list_elections(ArrayList<Election> elections,int choose) throws RemoteException {

        Election el=null;
        int aux=0;
        if(choose==0) {
            for (int i = 0; i < elections.size(); i++) {
                if (elections.get(i).getnome_mesa().equals("")) {
                    aux = 1;
                    System.out.println(i + " - " + elections.get(i).getTitle());
                }
            }
            if (aux == 0) System.out.println("Todas as eleições já têm mesas associadas");
        }
        else{
            for (int i = 0; i < elections.size(); i++) {
                System.out.println();
                if (!(elections.get(i).getnome_mesa().equals(""))) {
                    aux = 1;
                    System.out.println(i + " - " + elections.get(i).getTitle());
                }
            }
            if (aux == 0) System.out.println("Nenhuma eleição tem mesa associada");
        }


        return aux;
    }

    private void list_dep() {
        for(String i:dep.keySet()){
            String[] Split=i.split(" ");
            System.out.println(Split[0]);
        }
    }


    public void getVote(String election_tile,String Voted_List) throws RemoteException, NotBoundException, ParseException {


        s = (interface_central) LocateRegistry.getRegistry(ip,7000).lookup("central");

        //no need for the new parameter, the sum of votes is done inside the refresh election
        ArrayList<Election> elections;
        while(true){
            try{
                elections = s.getElections();
                break;
            } catch (ConnectException ex) {

                s = (interface_central) LocateRegistry.getRegistry(ip,7000).lookup("central");
            }
        }
        if(Votes_on==true) {

            for(String i: dep.keySet()){
                System.out.println("----------------------------");
                String [] Split=i.split(" ");
                System.out.println("Mesa "+Split[0]);


                for(int j=0;j<elections.size();j++){
                    if(elections.get(j).getnome_mesa().equalsIgnoreCase(Split[0])){
                        System.out.println("Votos ("+elections.get(j).getTitle()+") :"+elections.get(j).getNum_votes());
                    }
                }
                System.out.println("----------------------------");
            }
        }



    }

    public  void setS(interface_central s) {
        AdminConsole.s = s;
    }

    @Override
    public void terminal_states(int num_on, String mesa_name) throws RemoteException, NotBoundException, ParseException {

        if(Status_on==true) {
            System.out.println(mesa_name + ": ON with " + num_on + " terminals");
            System.out.flush();
        }
    }
}


class admins_regist extends Thread{

    private AdminConsole AdminInt;
    private interface_central s;
    private String ip;
    public admins_regist(interface_central s, AdminConsole AdminInt,String ip) throws InterruptedException, RemoteException {

        this.AdminInt=AdminInt;
        this.s=s;
        this.ip=ip;

        this.start();
    }
    public void run(){
        while(true){

            try {
                s.Register_admin(AdminInt);
                sleep(3000);
            } catch (ConnectException i){
                try {
                    s=(interface_central) LocateRegistry.getRegistry(ip,7000).lookup("central");
                } catch (RemoteException |  NotBoundException e) {

                }
            } catch(InterruptedException | RemoteException e) {

            } catch (NotBoundException e) {
                e.printStackTrace();
            }


        }
    }
}


