package rmiserver;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;

public abstract class Person implements Serializable {
    private static final long serialVersionUID = 1L;
    private String name;
    private String placeofVote;
    private String id_card;
    private String department;
    private String password;
    private String logged;

    public String getLogged() {
        return logged;
    }

    public void setLogged(String logged) {
        this.logged = logged;
    }

    private boolean is_admin;
    private String job;
    ArrayList<String> voted=new ArrayList<>();
    private Date time_of_vote;

    public Date getTime_of_vote() {
        return time_of_vote;
    }

    public void setTime_of_vote(Date time_of_vote) {
        this.time_of_vote = time_of_vote;
    }



    public abstract String getJob();

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getId_card() {
        return id_card;
    }

    public void setId_card(String id_card) {
        this.id_card = id_card;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }




    public String getPlaceofVote() {
        return placeofVote;
    }

    public void setPlaceofVote(String placeofVote) {
        this.placeofVote = placeofVote;
    }

    public Person(String name, String cc){
        this.name = name;
        this.id_card = cc;
    }

    public ArrayList<String> getVoted() {
        return voted;
    }

    public void setVoted(ArrayList<String> voted) {
        this.voted = voted;
    }

    public boolean getIs_admin() {
        return is_admin;
    }

    public void setIs_admin(boolean is_admin) {
        this.is_admin = is_admin;
    }

    public Person(String name, String id_card, String department, String password, String placeofVote) {
        this.name = name;
        this.id_card = id_card;
        this.department = department;
        this.password = password;
        this.placeofVote=placeofVote;
        this.is_admin=false;
        this.logged="false";

    }

}
