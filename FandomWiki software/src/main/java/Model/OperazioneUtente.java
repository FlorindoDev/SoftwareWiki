package main.java.Model;
import java.sql.Timestamp;
import java.util.Date;
public class OperazioneUtente {

    private int id_operazione;
    private Timestamp DataA;
    private Timestamp DataR;
    private String Testo;
    private boolean accettata;
    private boolean visionata;
    private boolean link;
    private int link_pagina;
    private String Utente;
    private String utente_notificato;
    private boolean modifica;
    private String Titolo_pagina_link;
    private String Titolo;

    public OperazioneUtente(int id_operazione,Timestamp DataA, Timestamp DataR, String Testo, boolean accettata, boolean visionata,boolean modifica, boolean link, int link_pagina, String Utente){
        this.DataA = DataA;
        this.DataR = DataR;
        this.Testo = Testo;
        this.accettata = accettata;
        this.visionata = visionata;
        this.link = link;
        this.link_pagina = link_pagina;
        this.modifica = modifica;
        this.id_operazione = id_operazione;
        this.Utente = Utente;

    }

    public OperazioneUtente(int id_operazione, Timestamp DataR, String Testo, boolean accettata, boolean visionata,boolean modifica, boolean link, int link_pagina, String Utente){

        this.DataR = DataR;
        this.Testo = Testo;
        this.accettata = accettata;
        this.visionata = visionata;
        this.link = link;
        this.link_pagina = link_pagina;
        this.modifica = modifica;
        this.id_operazione = id_operazione;
        this.Utente = Utente;

    }

    public OperazioneUtente(int id_operazione,Timestamp DataA, Timestamp DataR, String Testo, boolean accettata, boolean visionata,boolean modifica, boolean link, int link_pagina, String Utente, String Autore, String Titolo_pagina_link, String titolo){
        this.DataA = DataA;
        this.DataR = DataR;
        this.Testo = Testo;
        this.accettata = accettata;
        this.visionata = visionata;
        this.link = link;
        this.link_pagina = link_pagina;
        this.modifica = modifica;
        this.id_operazione = id_operazione;
        this.Utente = Utente;
        this.utente_notificato = Autore;
        this.Titolo_pagina_link = Titolo_pagina_link;
        this.Titolo = titolo;

    }

    public Timestamp getDataR() {
        return DataR;
    }

    public Timestamp getDataA() {
        return DataA;
    }

    public boolean getAccettata() {
        return accettata;
    }

    public boolean getVisionata() {
        return visionata;
    }

    public boolean getModifica() {
        return modifica;
    }

    public int getLink_pagina() {
        return link_pagina;
    }

    public String getTesto(){
        return Testo;
    }

    public boolean getLink(){
        return link;
    }

    public String getTitolo(){return Titolo;}
    public int getIdOperazione(){
        return id_operazione;
    }

    public String getUtente(){
        return Utente;
    }

    public String getUtenteNotificato(){
        return utente_notificato;
    }

    public String getTitoloPaginaLink(){
        return Titolo_pagina_link;
    }

    public void SetDataA(Timestamp d){DataR = d;}

    public void SetAutore(String d){utente_notificato = d;}

    public void SetTitoloLink(String d){Titolo_pagina_link = d;}

    public void SetVisionata(boolean d){visionata = d;}

    public void SetTitolo(String d){Titolo = d;}
}
