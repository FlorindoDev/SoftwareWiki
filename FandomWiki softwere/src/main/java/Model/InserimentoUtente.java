package main.java.Model;

import java.sql.Timestamp;

public class InserimentoUtente extends OperazioneUtente{

    private int posizione;
    public InserimentoUtente(int id_operazione,Timestamp DataA, Timestamp DataR, String Testo, boolean accettata, boolean visionata,boolean modifica, boolean link, int link_pagina, int posizione,String Utente){
        super(id_operazione,DataA,DataR,Testo,accettata,visionata,modifica,link,link_pagina,Utente);
        this.posizione = posizione;
    }

    public InserimentoUtente(int id_operazione, Timestamp DataR, String Testo, boolean accettata, boolean visionata,boolean modifica, boolean link, int link_pagina, int posizione,String Utente){
        super(id_operazione,DataR,Testo,accettata,visionata,modifica,link,link_pagina,Utente);
        this.posizione = posizione;
    }

    public InserimentoUtente(int id_operazione,Timestamp DataA, Timestamp DataR, String Testo, boolean accettata, boolean visionata,boolean modifica, boolean link, int link_pagina, int posizione,String Utente,String Autore,String Titolo_pagina_link, String  Titolo){
        super(id_operazione,DataA,DataR,Testo,accettata,visionata,modifica,link,link_pagina,Utente,Autore, Titolo_pagina_link, Titolo);
        this.posizione = posizione;

    }

    public int getPosizione() {
        return posizione;
    }
}
