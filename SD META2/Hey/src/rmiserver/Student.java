package rmiserver;

import java.io.Serializable;

public class Student extends Person implements Serializable {
    private static final long serialVersionUID = 1L;
    public Student(String name, String id_card, String department, String password) {
        super(name, id_card, department, password,"");
    }

    public String getJob() {
        return "Estudante";
    }

}
