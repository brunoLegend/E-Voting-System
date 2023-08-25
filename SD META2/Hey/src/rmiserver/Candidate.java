package rmiserver;

import java.io.Serializable;

public class Candidate extends Person implements Serializable {
    private static final long serialVersionUID = 1L;
    public Candidate(String name, String cc){
        super(name,cc);
    }
    public Candidate(String name, String id_card, String department, String password) {
        super(name, id_card, department, password,"");
    }

    public String getJob() {
        return "Candidato";
    }
}
