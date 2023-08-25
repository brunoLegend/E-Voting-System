
package hey.action;

import com.opensymphony.xwork2.ActionSupport;
import org.apache.struts2.interceptor.SessionAware;

import java.rmi.RemoteException;
import java.util.Map;
import hey.model.HeyBean;

public class RegisterAction extends ActionSupport implements SessionAware {
    private static final long serialVersionUID = 4L;
    private static final String REGISTER = "register";

    private Map<String, Object> session;
    private String nome=null,cc=null,departamento=null,password=null,funcao=null;

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getCc() {
        return cc;
    }

    public void setCc(String cc) {
        this.cc = cc;
    }

    public String getDepartamento() {
        return departamento;
    }

    public void setDepartamento(String departamento) {
        this.departamento = departamento;
    }

    public String getPassword() {
        return password;
    }

    public String getFuncao() {
        return funcao;
    }

    public void setFuncao(String funcao) {
        this.funcao = funcao;
    }

    @Override
    public String execute() throws RemoteException {

        System.out.println(" "+this.nome+" "+this.password);

        if(this.nome!=null && this.password!=null && this.getHeyBean().getUser(nome,password)!=null) {
            System.out.println("existsss");
            /*
            this.getHeyBean().setUsername(this.nome);
            this.getHeyBean().setPassword(this.password);
            session.put("username", nome);
            session.put("loggedin", true); // this marks the user as logged in
            */
            //se já existir
            return REGISTER;





        }
        //se entra aqui é porque as variaveis nao estao a null e a pessoa ainda n existe
        else if(this.nome!=null && this.password!=null && this.cc!=null  && this.departamento!=null && this.funcao!=null) {
            System.out.println("newww");
            this.getHeyBean().update_register(nome,cc,  password, departamento, funcao);
            return SUCCESS;
        }
        return REGISTER;
    }



    public void setPassword(String password) {
        this.password = password; // what about this input?
    }

    public HeyBean getHeyBean() {
        if(!session.containsKey("heyBean"))
            this.setHeyBean(new HeyBean());
        return (HeyBean) session.get("heyBean");
    }

    public void setHeyBean(HeyBean heyBean) {
        this.session.put("heyBean", heyBean);
    }

    @Override
    public void setSession(Map<String, Object> session) {
        this.session = session;
    }
}
