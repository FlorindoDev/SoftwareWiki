package main.java.Model;
import java.util.Date;

public class OperazioneAutore {

    private Date Data;
    private String Testo;
    private boolean Link;
    private Integer LinkPagina;

    public OperazioneAutore(Date Data, String Testo, boolean Link, Integer LinkPagina){
        this.Data=Data;
        this.Testo=Testo;
        this.Link=Link;
        this.LinkPagina=LinkPagina;
    }

    public Date getData() {
        return Data;
    }

    public Integer getLinkPagina() {
        return LinkPagina;
    }

    public String getTesto() {
        return Testo;
    }

    public boolean getLink(){
        return Link;
    }
}
