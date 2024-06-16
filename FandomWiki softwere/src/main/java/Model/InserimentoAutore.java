package main.java.Model;

import java.util.Date;

public class InserimentoAutore extends OperazioneAutore{

    private Integer Posizione;
    public InserimentoAutore(Date Data, String Testo, boolean Link, Integer LinkPagina, Integer Posizione){
        super(Data, Testo, Link, LinkPagina);
        this.Posizione=Posizione;
    }

    public Integer getPosizione() {
        return Posizione;
    }
}
