
package hey.action;

import com.opensymphony.xwork2.ActionSupport;
import org.apache.struts2.interceptor.SessionAware;

import java.rmi.RemoteException;
import java.util.Date;
import java.util.Map;
import hey.model.HeyBean;

public class createElectionAction extends ActionSupport implements SessionAware {
    private static final long serialVersionUID = 4L;
    private static final String ELECTION = "election";

    private Map<String, Object> session;
    private String departamento=null,funcao=null,titulo=null,data_init=null,data_fim=null,mesa=null,descricao=null;

    private Date init=null,end=null;

    public String getDepartamento() {
        return departamento;
    }

    public void setDepartamento(String departamento) {
        this.departamento = departamento;
    }

    public String getFuncao() {
        return funcao;
    }

    public void setFuncao(String funcao) {
        this.funcao = funcao;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getData_init() {
        return data_init;
    }

    public void setData_init(String data_init) {
        this.data_init = data_init;
    }

    public String getData_fim() {
        return data_fim;
    }

    public void setData_fim(String data_fim) {
        this.data_fim = data_fim;
    }

    public String getMesa() {
        return mesa;
    }

    public void setMesa(String mesa) {
        this.mesa = mesa;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    @Override
    public String execute() throws RemoteException {

        System.out.println(" " + this.titulo + " " + this.descricao);
        //primeiro verificamos se foi tudo preenchido
        if (this.departamento != null && this.descricao != null && this.titulo != null && this.funcao != null && this.data_fim != null && this.data_init != null && this.mesa != null) {
            //depois verificamos se os dados foram introduzidos corretamente
            if (this.getHeyBean().check_dep(departamento) == true && this.getHeyBean().check_dep(mesa) == true && this.getHeyBean().check_funcao(funcao) == true && (init = this.getHeyBean().verify_date_ini(data_init)) != null && (end = this.getHeyBean().verify_date_ini(data_fim)) != null) {
                this.getHeyBean().createElection(titulo, init, end, descricao, mesa, funcao, departamento);
                System.out.println("election created");
                return SUCCESS;
            }

            return ELECTION;


        }
        return ELECTION;
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
