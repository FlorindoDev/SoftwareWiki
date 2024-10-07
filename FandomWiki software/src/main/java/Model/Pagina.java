package main.java.Model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Pagina {

    private int idPagina;
    private String titolo = new String();
    private String emailAutore;
    private String Generalita_Autore = new String();
    private String Data_Ultima_Modifica;
    private String Crezione_Pagina;
    private HashMap<Integer, Frase> Frasi; //l'indice parte da 1

    public Pagina(String titolo, String emailAutore, String Generalita_Autore, String DataUltimaModifica, String Crezione_Pagina, int idPagina){
        this.titolo = titolo;
        this.emailAutore=emailAutore;
        this.Generalita_Autore = Generalita_Autore;
        this.Data_Ultima_Modifica = DataUltimaModifica;
        this.Crezione_Pagina = Crezione_Pagina;
        this.idPagina=idPagina;

    }

    public String getEmailAutore(){
        return emailAutore;
    }

    public void AddFrase(Frase fraseToInsert, int idx){

        if (Frasi == null){
            Frasi = new HashMap<Integer, Frase>();
        }

        Frasi.put(idx, fraseToInsert);

    }

    public HashMap<Integer, Frase> getFrasi(){return Frasi;}

    public Frase getFrase(int n){

        Frase StoredFrase = Frasi.get(n);
        if (StoredFrase==null){// la frase n non esiste
            return null;
        }else{
            return StoredFrase;
        }

    }

    public int getLastIdxFrase(){//ritorna indice rinumerato

        int last_idx = 0;
        for (Integer key : Frasi.keySet()) {
            if (key>=last_idx){
                last_idx=key;
            }
        }
        System.out.println("Chiave dell'ultima frase: " + last_idx);
        return  last_idx;
    }

    public int getID(){
        return idPagina;
    }

}