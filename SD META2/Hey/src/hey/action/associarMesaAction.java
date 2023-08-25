package hey.action;

import com.opensymphony.xwork2.ActionSupport;
import org.apache.struts2.interceptor.SessionAware;

import java.rmi.RemoteException;
import java.text.ParseException;
import java.util.Map;
import hey.model.HeyBean;
import rmiserver.Election;
import rmiserver.Person;

public class associarMesaAction extends ActionSupport implements SessionAware {
    public String getElection_add() {
        return election_add;
    }

    public void setElection_add(String election_add) {
        this.election_add = election_add;
    }

    private String election_add=null;



    Map<String, Object> session;

    @Override
    public String execute() throws RemoteException, ParseException {
        //depois tirar a pessoa da sessao quando ele carregar voltar para tras!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
        Election el =this.getHeyBean().getElection(election_add);
        session.put("election_add",el);
        System.out.println("sessoao: "+session.get("election_add"));
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


