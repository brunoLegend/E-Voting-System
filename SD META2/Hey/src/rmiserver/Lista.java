package rmiserver;


import java.io.Serializable;
import java.util.ArrayList;

public class Lista implements Serializable {
    private static final long serialVersionUID = 1L;
    private String name;
    private ArrayList<Candidate> candidates = new ArrayList<>();
    public Lista(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ArrayList<Candidate> getCandidates() {
        return candidates;
    }

    public void setCandidates(ArrayList<Candidate> candidates) {
        this.candidates = candidates;
    }

    public void addCandidates(Candidate candidate) {
        this.candidates.add(candidate);
    }

    public void removeCandidates(Candidate candidate) {
        for(int i=0;i<candidates.size();i++){
            if(candidates.get(i).getId_card().equals(candidate.getId_card())) {
                this.candidates.remove(i);
                break;
            }
        }

    }

}

