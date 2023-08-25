/**
 * Raul Barbosa 2014-11-07
 */
package hey.model;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import rmiserver.*;

public class HeyBean {
	private interface_central server;
	private String username; // username and password supplied by the user
	private String password;
	private Election election;
	private String voted_list;

	public String getVoted_list() {
		return voted_list;
	}

	public void setVoted_list(String voted_list) {
		this.voted_list = voted_list;
	}

	public Election getElection() {
		return election;
	}

	public void setElection(Election election) {
		this.election = election;
	}

	public HeyBean() {
		try {
			server = (interface_central) LocateRegistry.getRegistry("127.0.0.1",7000).lookup("central");
		}
		catch(RemoteException | NotBoundException e) {

			// what happens *after* we reach this line?
		}
	}
/*
	public ArrayList<String> getAllUsers() throws RemoteException {
		return server.getAllUsers(); // are you going to throw all exceptions?
	}

	public boolean getUserMatchesPassword() throws RemoteException {
		return server.userMatchesPassword(this.username, this.password);
	}
*/

	public void update_register(String nome, String cc, String password,String departamento, String funcao) throws RemoteException {

		Person person=null;
		if (funcao.equalsIgnoreCase("estudante")) {
			person = new Student(nome, cc, departamento, password);
		} else if (funcao.equalsIgnoreCase("funcionario")) {
			person = new Worker(nome, cc, departamento, password);
		} else if (funcao.equalsIgnoreCase("docente")) {
			person = new Teacher(nome, cc, departamento, password);
		}

		server.regist_person(person);
	}

	public Election getElection(String Election) throws RemoteException{
		return server.Search_election(Election);
	}

	public String getPersonid(String nome) throws RemoteException {
		ArrayList<Person> pessoas= server.getPeople();
		for(Person p: pessoas){
			if(p.getName().equalsIgnoreCase(nome)){
				return p.getId_card();
			}
		}
		return "";
	}

	//devolve null se o size for = 0 ou seja se ele nao puder votar em nenhuma eleicao
	public ArrayList<Election> getUserlistelections(String username,String password) throws RemoteException {
		System.out.println("IDDDDDD: "+username);
		Person p=server.getUser(username,password);
		System.out.println("id"+p);
		ArrayList<Election> elections=server.list_elections_online(p.getId_card());
		System.out.println("pasoou");
		if(elections.size()!=0)
			return elections;
		System.out.println("null");
		return null;
	}

	public ArrayList<Election> getListelections() throws RemoteException {
		return server.getElections();
	}

	public boolean check_dep(String departamento) throws RemoteException {
		if(server.exists_dep(departamento).equalsIgnoreCase("true")){
			return true;
		}else{
			return false;
		}
	}


	public boolean check_list(Election e,String name) throws RemoteException {
		if(server.verify_list(e,name)==1)return true;
		return false;
	}

	public boolean check_funcao(String funcao){
		if(funcao.equalsIgnoreCase("Estudante") || funcao.equalsIgnoreCase("Funcionario") || funcao.equalsIgnoreCase("Docente")){
			return true;
		}else{
			return false;
		}
	}

	public void update_election(int param,String change,String old_title) throws ParseException, RemoteException {
		server.refreshElection(param,change,old_title);
	}



	public Date verify_date_end(String end_date, Date init) throws ParseException {
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


	public Date verify_date_ini(String init_date)  {

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

	public ArrayList<Election> getList_rem() throws RemoteException {
		ArrayList<Election> elections=server.getElections();
		ArrayList<Election> rem=new ArrayList<>();

		for (int i = 0; i < elections.size(); i++) {
			if (!(elections.get(i).getnome_mesa().equals("")) && elections.get(i).getState()==1) {

				System.out.println(i + " - " + elections.get(i).getTitle());
				rem.add(elections.get(i));
			}
		}

		if(rem.size()==0) rem=null;
		System.out.println("returning");
	return rem;

	}


	public ArrayList<Election> getList_add() throws RemoteException {

		ArrayList<Election> add=new ArrayList<>();

		ArrayList<Election> elections=server.getElections();

		for (int i = 0; i < elections.size(); i++) {
			if (elections.get(i).getnome_mesa().equals("") && elections.get(i).getState()==1) {

				System.out.println(i + " - " + elections.get(i).getTitle());
				add.add(elections.get(i));
			}
		}
		if(add.size()==0) add=null;
		return add;

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






	public Person getPerson(String id) throws RemoteException {
		return server.search_person(id);
	}

	public ArrayList<Person> getPeople() throws RemoteException {
		return server.getPeople();
	}

	public void createElection(String title, Date init, Date end, String description, String nome_Mesa, String voter_type, String nucleo) throws RemoteException {
		Election election = new Election(title, init, end, description, nome_Mesa, voter_type.toLowerCase(), nucleo);
		server.regist_new_election(election);
	}

	public HashMap<String, Boolean> getMesa() throws RemoteException {
		return server.readMesaFile();
	}

	public void putMesa(HashMap<String, Boolean> dep) throws RemoteException {
		server.writeMesaFile(dep);
	}


	public Person getUser(String username,String password) throws RemoteException {
		return server.getUser(username,password); // are you going to throw all exceptions?
	}

	public ArrayList<String> getOptions() throws RemoteException {
		return server.get_options();
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public void setPassword(String password) {
		this.password = password;
	}

    public void vote() throws RemoteException, NotBoundException, ParseException {
		String title= election.getTitle();
		SimpleDateFormat ft = new SimpleDateFormat("yyyy/MM/dd HH:mm");
		Date now = new Date();
		String p_id=getPersonid(username);
		server.Update_Votes(title,voted_list,now,p_id,1);
    }

	public Lista search_list(String name) throws RemoteException{
		System.out.println("el: "+election+" name "+name);
		return server.search_list(election,name);
	}



	public void add_person_list(Lista list, Election choosen, String cc_add, String nome_add) throws RemoteException {

		if (server.verify_candidate(list, cc_add) == 0)
			server.refreshList(2, choosen, nome_add, cc_add, list.getName());
	}

	public void removepersonlist(Lista list, Election choosen, String cc_rem, String nome_rem) throws RemoteException {
		server.refreshList(1, election, nome_rem, cc_rem, list.getName());

	}
}
