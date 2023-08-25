package hey.action;

import com.opensymphony.xwork2.ActionSupport;
import org.apache.struts2.interceptor.SessionAware;

import java.rmi.RemoteException;
import java.text.ParseException;
import java.util.Map;
import hey.model.HeyBean;
import rmiserver.Person;

public class consultardetalhesAction extends ActionSupport implements SessionAware {
    private String pessoa=null;

    public String getPessoa() {
        return pessoa;
    }

    public void setPessoa(String pessoa) {
        this.pessoa = pessoa;
    }

    Map<String, Object> session;

    @Override
    public String execute() throws RemoteException, ParseException {
        //depois tirar a pessoa da sessao quando ele carregar voltar para tras!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
        String id =this.getHeyBean().getPersonid(pessoa);
        Person p=this.getHeyBean().getPerson(id);
        session.put("person",p);
        System.out.println("sessoao: "+session.get("person"));
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

