package rmiserver;

import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;

public class Election implements Serializable {
    private static final long serialVersionUID = 1L;
    private String title;
    private String descriprion;
    private Date init_date;
    private Date end_date;
    private String num_votes;
    private int final_votes;
    private String nome_Mesa;
    private String winner;

    public String getWinner() {
        return winner;
    }

    public void setWinner(String winner) {
        this.winner = winner;
    }

    //an election has 3 states -> 0 has not began; ->1 is on ; 2-> has ended
    private int state;
    private int nulos;
    private int brancos;

    public int getNulos() {
        return nulos;
    }

    public void setNulos(int nulos) {
        this.nulos = nulos;
    }

    public int getBrancos() {
        return brancos;
    }

    public void setBrancos(int brancos) {
        this.brancos = brancos;
    }

    public String getVoter_type() {
        return voter_type;
    }

    public void setVoter_type(String voter_type) {
        this.voter_type = voter_type;
    }

    public String getNucleo() {
        return nucleo;
    }

    public void setNucleo(String nucleo) {
        this.nucleo = nucleo;
    }

    public void setCandidates(HashMap <Lista,Integer> candidates) {
        this.candidates = candidates;
    }

    private String voter_type;
    private String nucleo;
    //HASH MAP com lista e votos da lista
    private HashMap <Lista,Integer> candidates=new HashMap<>();
    // private ArrayList<String> candidates = new ArrayList<>();

    public Election() {
    }

    public String getNum_votes() {
        return num_votes;
    }

    public void setNum_votes(String num_votes) {
        this.num_votes = num_votes;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public int getFinal_votes() {
        return final_votes;
    }

    public void setFinal_votes(int final_votes) {
        this.final_votes = final_votes;
    }

    public Election(String title, Date init, Date end, String description, String nome_Mesa, String voter_type, String nucleo) {
        this.init_date = init;
        this.end_date = end;
        this.title = title;
        this.descriprion = description;
        this.nome_Mesa=nome_Mesa;
        this.voter_type=voter_type;
        this.nucleo=nucleo;
        this.num_votes="0";
        this.state=0;
        this.nulos=0;
        this.brancos=0;

    }

    public String getTitle() {
        return title;
    }

    public String getnome_mesa() {
        return nome_Mesa;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setNome_Mesa(String mesa) {
        this.nome_Mesa = mesa;
    }

    public String getDescriprion() {
        return descriprion;
    }

    public void setDescriprion(String descriprion) {
        this.descriprion = descriprion;
    }

    public Date getInit_date() {
        return init_date;
    }

    public void setInit_date(Date init_date) {
        this.init_date = init_date;
    }

    public Date getEnd_date() {
        return end_date;
    }

    public HashMap<Lista, Integer> getCandidates() {
        return candidates;
    }

    public void setEnd_date(Date end_date) {
        this.end_date = end_date;
    }

    public void printCandidates(int tabs) {
        int j=0;
        for (Lista i: this.candidates.keySet()) {
            j++;
            System.out.print(j + " - ");
            if (j == this.candidates.size()) System.out.println(i.getName());
            else System.out.print(i.getName() + ", ");
        }
    }

    public void add_vote(Lista list){



        System.out.println("LIST  "+list.getName());
        if(list.getName().equals("Nulo")) nulos=nulos+1;

        else if(list.getName().equals("Branco")) brancos=brancos+1;
        else {
            if (candidates != null) {
                for(Lista i: candidates.keySet()){
                    System.out.println(i.getName());
                    if(i.getName().equalsIgnoreCase(list.getName())){

                        candidates.replace(i, candidates.get(i) + 1);
                    }
                }

            }
        }
    }

    public void addCandidate(Lista cand) {
        this.candidates.put(cand,0);
    }


    public void removeList(String l){
        for(Lista aux: this.candidates.keySet()){
            if(aux.getName().equalsIgnoreCase(l)) {
                this.candidates.remove(aux);
                break;
            }
        }
    }
}
