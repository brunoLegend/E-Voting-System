package hey.action;

import com.opensymphony.xwork2.ActionSupport;
import org.apache.struts2.interceptor.SessionAware;

import java.rmi.RemoteException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import hey.model.HeyBean;
import rmiserver.Election;

public class GerirEleicaoAction extends ActionSupport implements SessionAware {
    private static final String EDIT = "edit";
    private static final String CONSULT = "consult";
    Map<String, Object> session;
    private String nome_elec=null;

    public String getNome_elec() {
        return nome_elec;
    }

    public void setNome_elec(String nome_elec) {
        this.nome_elec = nome_elec;
    }

    @Override
    public String execute() throws RemoteException {
        Election election=this.getHeyBean().getElection(nome_elec);
        System.out.println(nome_elec);
        if(nome_elec!=null && election!=null) {
            session.put("election",election);
            this.getHeyBean().setElection(election);
            //eleicao a decorrer
            if(this.getHeyBean().getElection(nome_elec).getState()==1){
                return EDIT;
            }
            //eleicao terminada
            else if(this.getHeyBean().getElection(nome_elec).getState()==2){
                return CONSULT;
            }

            return SUCCESS;
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
