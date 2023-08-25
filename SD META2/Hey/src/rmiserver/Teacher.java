package rmiserver;

import java.io.Serializable;

public class Teacher extends Person implements Serializable {
    private static final long serialVersionUID = 1L;
    public Teacher(String name, String id_card, String department, String password) {
        super(name, id_card, department, password,"");
    }

    public String getJob() {
        return "Docente";
    }
}
