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

public class EditElectionAction extends ActionSupport implements SessionAware {
    private static final String EDIT = "edit";
    private static final String CONSULT = "consult";
    Map<String, Object> session;

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public String getNome_mesa() {
        return nome_mesa;
    }

    public void setNome_mesa(String nome_mesa) {
        this.nome_mesa = nome_mesa;
    }

    public String getFuncao() {
        return funcao;
    }

    public void setFuncao(String funcao) {
        this.funcao = funcao;
    }

    private String titulo=null,descricao=null,nome_mesa=null,funcao=null,lista_add=null,lista_remove=null;

    public String getLista_add() {
        return lista_add;
    }

    public void setLista_add(String lista_add) {
        this.lista_add = lista_add;
    }

    public String getLista_remove() {
        return lista_remove;
    }

    public void setLista_remove(String lista_remove) {
        this.lista_remove = lista_remove;
    }

    @Override
    public String execute() throws RemoteException, ParseException {

        if((titulo==null ||titulo.equals("")) && (descricao==null || descricao.equals("")) && (nome_mesa==null || nome_mesa.equals("")) && (funcao==null || funcao.equals("")) && (lista_add==null || lista_add.equals("")) && (lista_remove==null || lista_remove.equals(""))){
            System.out.println("tudo null");
            return EDIT;
        }

        Election el= (Election) session.get("election");

        if(titulo!=null && !titulo.equals("")){
            System.out.println("titulo atualizado");
            this.getHeyBean().update_election(1,titulo, el.getTitle());
        }
        if(descricao!=null && !descricao.equals("")){
            System.out.println("descricao atualizada");
            this.getHeyBean().update_election(4,descricao, el.getTitle());
        }
        if(nome_mesa!=null && !nome_mesa.equals("")){
            if(this.getHeyBean().check_dep(nome_mesa)==true) {
                System.out.println("nome_mesa atualizada");
                this.getHeyBean().update_election(7, nome_mesa, el.getTitle());
            }
        }
        if(funcao!=null && !funcao.equals("")){
            if(this.getHeyBean().check_funcao(funcao)==true) {
                System.out.println("funcao atualizada");
                this.getHeyBean().update_election(14, funcao, el.getTitle());
            }
        }
        if(lista_add!=null && !lista_add.equals("")){
            //check if the list doesn't already exist in the current election
            if(this.getHeyBean().check_list(el,lista_add)==false){
                System.out.println("lista_add atualizada");
                this.getHeyBean().update_election(6, lista_add, el.getTitle());
            }
        }
        if(lista_remove!=null && !lista_remove.equals("")){
            //check if the list exists in the current election
            if(this.getHeyBean().check_list(el,lista_remove)==true){
                System.out.println("lista_remove atualizada");
                this.getHeyBean().update_election(5, lista_remove, el.getTitle());
            }
        }
        session.replace("election",el);
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

