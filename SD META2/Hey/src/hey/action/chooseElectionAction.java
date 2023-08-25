package hey.action;

import com.opensymphony.xwork2.ActionSupport;
import org.apache.struts2.interceptor.SessionAware;

import java.rmi.RemoteException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import hey.model.HeyBean;
import rmiserver.Election;

public class chooseElectionAction extends ActionSupport implements SessionAware {
    Map<String, Object> session;

    public String getEleicao() {
        return eleicao;
    }

    public void setEleicao(String eleicao) {
        this.eleicao = eleicao;
    }
    private String eleicao=null;


    @Override
    public String execute() throws RemoteException {
        System.out.println("eleicao no choose "+eleicao);
        Election election=this.getHeyBean().getElection(eleicao);
        session.put("eleicao",election);

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
