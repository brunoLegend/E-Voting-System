package hey.action;

import com.opensymphony.xwork2.ActionSupport;
import org.apache.struts2.interceptor.SessionAware;

import java.rmi.RemoteException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import hey.model.HeyBean;
import rmiserver.Election;
import rmiserver.Lista;

public class criarMesaAction extends ActionSupport implements SessionAware {
    private String mesa=null;
    Map<String, Object> session;

    public String getMesa() {
        return mesa;
    }

    public void setMesa(String mesa) {
        this.mesa = mesa;
    }

    @Override
    public String execute() throws RemoteException {
        System.out.println("escolhi a mesa: "+mesa);
        HashMap<String, Boolean> mesas = this.getHeyBean().getMesa();
        mesas.replace(mesa,true);
        this.getHeyBean().putMesa(mesas);
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
