
package hey.action;

import com.opensymphony.xwork2.ActionSupport;
import org.apache.struts2.interceptor.SessionAware;

import java.rmi.RemoteException;
import java.util.Map;
import hey.model.HeyBean;

public class LoginAction extends ActionSupport implements SessionAware {
	private static final long serialVersionUID = 4L;
	private static final String ADMIN = "admin";

	private Map<String, Object> session;
	private String username = null, password = null;

	@Override
	public String execute() throws RemoteException {

		System.out.println(" "+this.username+" "+this.password);
		if(this.username!=null && this.password!=null && this.getHeyBean().getUser(username,password)!=null) {
			System.out.println("existsss");
			this.getHeyBean().setUsername(this.username);
			this.getHeyBean().setPassword(this.password);
			session.put("username", username);
			session.put("password",password);
			session.put("loggedin", true); // this marks the user as logged in
			
			if(this.getHeyBean().getUser(username,password).getIs_admin()==true){
				return ADMIN;
			}
			
			
			
			
			return SUCCESS;
		}
		else {
			System.out.println("errrrrouuuuuu");
			return LOGIN;
		}
	}

	public void setUsername(String username) {
		this.username = username; // will you sanitize this input? maybe use a prepared statement?
	}

	public void setPassword(String password) {
		this.password = password; // what about this input? 
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
