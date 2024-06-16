package main.java.Model;

import java.util.ArrayList;

public class Autore extends Utente{

    private ArrayList<Pagina> Pagine = new ArrayList<Pagina>();

    private ArrayList<Notifica> Notifiche = new ArrayList<Notifica>();

    private ArrayList<OperazioneAutore> OperazioniAutore = new ArrayList<OperazioneAutore>();

    public Autore(String Email, String Password, String Nome, String Cognome, char genere) {
        super(Email, Password, Nome, Cognome, genere);

    }

    public void addPagina(Pagina p){
        Pagine.add(p);
    }

    public void addNotifica(Notifica notifica){
        Notifiche.add(notifica);

    }

    public ArrayList<Notifica> getNotifiche(){
        return Notifiche;
    }

    public void ResetNotifiche(){
        Notifiche.clear();
    }

    public void addOperazione(OperazioneAutore p){
        OperazioniAutore.add(p);
    }



}
