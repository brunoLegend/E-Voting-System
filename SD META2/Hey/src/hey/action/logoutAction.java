
package hey.action;

import com.opensymphony.xwork2.ActionSupport;
import org.apache.struts2.interceptor.SessionAware;

import java.rmi.RemoteException;
import java.util.Map;
import hey.model.HeyBean;

public class logoutAction extends ActionSupport implements SessionAware {
    private static final long serialVersionUID = 4L;

    private Map<String, Object> session;


    @Override
    public String execute() throws RemoteException {

       session.clear();
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
