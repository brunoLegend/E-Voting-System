
package hey.action;

import com.opensymphony.xwork2.ActionSupport;
import org.apache.struts2.interceptor.SessionAware;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.text.ParseException;
import java.util.Map;
import hey.model.HeyBean;
import rmiserver.Election;
import rmiserver.Person;

public class VoteAction extends ActionSupport implements SessionAware {
    private static final long serialVersionUID = 4L;
    private static final String ADMIN = "admin";

    private Map<String, Object> session;

    public String getLista() {
        return lista;
    }

    public void setLista(String lista) {
        this.lista = lista;
    }

    private String lista=null;

    @Override
    public String execute() throws RemoteException, NotBoundException, ParseException {
        System.out.println("___________________________________");
        System.out.println("ola"+lista);
        Person p=this.getHeyBean().getUser((String)session.get("username"),(String)session.get("password"));
        if(p.getPlaceofVote().equals("") || p.getPlaceofVote()==null) {
            this.getHeyBean().setElection((Election) session.get("eleicao"));
            this.getHeyBean().setVoted_list(lista);
            this.getHeyBean().vote();
        }
       return SUCCESS;

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
