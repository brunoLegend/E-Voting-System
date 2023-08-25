package hey.action;

import com.opensymphony.xwork2.ActionSupport;
import org.apache.struts2.interceptor.SessionAware;

import java.rmi.RemoteException;
import java.util.Date;
import java.util.Map;
import hey.model.HeyBean;
import rmiserver.Election;
import rmiserver.Lista;

public class chooseListasAction extends ActionSupport implements SessionAware {
    private String lista=null;
    Map<String, Object> session;
    public String getLista() {
        return lista;
    }

    public void setLista(String lista) {
        this.lista = lista;
    }

    @Override
    public String execute() throws RemoteException {
        System.out.println("escolhi a lista: "+lista);
        Lista l= getHeyBean().search_list(lista);
        session.put("lista",l);
        System.out.println(l.getCandidates().get(0).getName());
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
