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

public class gerirListasAction extends ActionSupport implements SessionAware {


    private static final String ERROR_FILL = "error_fill";

    public String getNome_add() {
        return nome_add;
    }

    public void setNome_add(String nome_add) {
        this.nome_add = nome_add;
    }

    public String getCc_add() {
        return cc_add;
    }

    public void setCc_add(String cc_add) {
        this.cc_add = cc_add;
    }

    public String getNome_rem() {
        return nome_rem;
    }

    public void setNome_rem(String nome_rem) {
        this.nome_rem = nome_rem;
    }

    public String getCc_rem() {
        return cc_rem;
    }

    public void setCc_rem(String cc_rem) {
        this.cc_rem = cc_rem;
    }

    Map<String, Object> session;
    private String nome_add=null,cc_add=null,nome_rem=null,cc_rem=null;








    @Override
    public String execute() throws RemoteException {
        System.out.println(" ver aqui "+nome_add+" "+cc_add+" "+nome_rem+" "+cc_rem);
       if((nome_add == null || nome_add.equals("")) && (nome_rem==null|| nome_rem.equals("")) && (cc_add==null || cc_add.equals("")) && (cc_rem==null || cc_rem.equals(""))){
           return ERROR_FILL;
       }
       Election choosen= (Election) session.get("election");
       Lista list= (Lista) session.get("lista");
       if(nome_add!=null && !nome_add.equals("") && cc_add!=null && !cc_add.equals("")){
           System.out.println("adding person");
           //addperson in refresh the election list if everything correct
           this.getHeyBean().add_person_list(list,choosen,cc_add,nome_add);
           session.replace("lista",this.getHeyBean().search_list(list.getName()));
           System.out.println("sssss: "+((Lista) session.get("lista")).getName());

       }

       if(nome_rem!=null && !nome_rem.equals("") && cc_rem!=null && !cc_rem.equals("")){
           System.out.println("removing person");
           this.getHeyBean().removepersonlist(list,choosen,cc_rem, nome_rem);
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
