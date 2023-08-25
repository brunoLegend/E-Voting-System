package hey.action;

import com.opensymphony.xwork2.ActionSupport;
import org.apache.struts2.interceptor.SessionAware;

import java.rmi.RemoteException;
import java.text.ParseException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import hey.model.HeyBean;
import rmiserver.Election;

public class chooseDepAction extends ActionSupport implements SessionAware {

    Map<String, Object> session;
    private String dep=null;

    public String getDep() {
        return dep;
    }

    public void setDep(String dep) {
        this.dep = dep;
    }

    @Override
    public String execute() throws RemoteException, ParseException {
        if(session.containsKey("election_add")) {
            Election election = (Election) session.get("election_add");

            this.getHeyBean().update_election(7, dep, election.getTitle());
            session.remove("election_add");
        }
        else if(session.containsKey("election_rem")){
            Election election = (Election) session.get("election_rem");

            this.getHeyBean().update_election(7, "", election.getTitle());
            session.remove("election_rem");
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
