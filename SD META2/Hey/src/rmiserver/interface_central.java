package rmiserver;

import ws.WebSocketAnnotation;

import java.io.IOException;
import java.rmi.NotBoundException;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

public interface interface_central extends Remote {

	public HashMap<String, String> process_message(HashMap<String, String> identi, Client_Interface server, String current_voter_id) throws IOException;

	public void autentication(String autenticator) throws RemoteException;

	public String regist_person(Person person) throws java.rmi.RemoteException;

	public String regist_new_election(Election election) throws java.rmi.RemoteException;

	public ArrayList<Election> readElectionFile() throws RemoteException;

	public String refreshElection(int param, String new_param, String old_title) throws RemoteException, ParseException;

	public void Register_admin(Interface_Admin Ad) throws RemoteException, NotBoundException;

	public void Update_Votes(String ref, String Voted_list, Date now, String received_voter_id,int is_on) throws RemoteException, NotBoundException, ParseException;

	public void printVotes(int Votes) throws RemoteException;

	public void readPeopleFile() throws RemoteException;

	public void writeMesaFile(HashMap<String, Boolean> dep) throws RemoteException;

	public HashMap<String, Boolean> readMesaFile() throws RemoteException;

	public ArrayList<Election> getElections() throws RemoteException;

	public ArrayList<Person> getPeople() throws RemoteException;

	public void count_votes(Election election) throws RemoteException, ParseException;

	public String check_dep(String nome) throws RemoteException;

	public void update_terminals_states(int num_on,String mesa_name) throws RemoteException, NotBoundException, ParseException;

	public int verify_candidate(Lista l, String cc) throws RemoteException;

	public int verify_list(Election e, String name) throws RemoteException;

	public String refreshList(int type, Election el, String nome, String cc, String list) throws RemoteException;

	public Person search_person(String id) throws RemoteException;

	public Person getUser(String username, String password) throws RemoteException;

	public ArrayList<String> get_options() throws RemoteException;

	public String exists_dep(String nome) throws RemoteException;

	public ArrayList<Election> list_elections_online(String id_user) throws RemoteException;

	public Election Search_election(String title) throws RemoteException;

	public Lista search_list(Election e,String name) throws RemoteException;

    public void subscribe_web(web_interface ws) throws RemoteException;
}