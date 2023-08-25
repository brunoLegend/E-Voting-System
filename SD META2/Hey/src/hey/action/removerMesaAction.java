package hey.action;

import com.opensymphony.xwork2.ActionSupport;
import org.apache.struts2.interceptor.SessionAware;

import java.rmi.RemoteException;
import java.text.ParseException;
import java.util.Map;
import hey.model.HeyBean;
import rmiserver.Election;
import rmiserver.Person;

public class removerMesaAction extends ActionSupport implements SessionAware {

    private String election_rem=null;

    public String getElection_rem() {
        return election_rem;
    }

    public void setElection_rem(String election_rem) {
        this.election_rem = election_rem;
    }

    Map<String, Object> session;

    @Override
    public String execute() throws RemoteException, ParseException {
        //depois tirar a pessoa da sessao quando ele carregar voltar para tras!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
        Election el =this.getHeyBean().getElection(election_rem);
        session.put("election_rem",el);
        System.out.println("sessoao: "+session.get("election_rem"));
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


