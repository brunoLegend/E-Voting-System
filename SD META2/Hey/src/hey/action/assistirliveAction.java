package hey.action;

import com.opensymphony.xwork2.ActionSupport;
import org.apache.struts2.interceptor.SessionAware;

import java.rmi.RemoteException;
import java.text.ParseException;
import java.util.Map;
import hey.model.HeyBean;
import rmiserver.Election;
import rmiserver.Person;

public class assistirliveAction extends ActionSupport implements SessionAware {
    public String getNome_live() {
        return nome_live;
    }

    public void setNome_live(String nome_live) {
        this.nome_live = nome_live;
    }

    private String nome_live=null;



    Map<String, Object> session;

    @Override
    public String execute() throws RemoteException, ParseException {
        //depois tirar a pessoa da sessao quando ele carregar voltar para tras!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!

        session.put("live",nome_live);
        System.out.println("sessoao: "+session.get("live"));
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


