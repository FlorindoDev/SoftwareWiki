package main.java.Model;

public class Link extends Frase{

    private Pagina PaginaLinkata;

    public  Link(String testo, int posizione, Pagina pagina){
        super(testo,posizione);
        this.PaginaLinkata = pagina;

    }

    public int getPaginaId(){
        return PaginaLinkata.getID();
    }

}
