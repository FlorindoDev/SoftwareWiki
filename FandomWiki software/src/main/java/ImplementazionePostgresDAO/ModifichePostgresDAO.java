package main.java.ImplementazionePostgresDAO;

import main.java.DAO.ModificheDAO;
import main.java.Database.ConnessionePostges;

import java.sql.*;
import java.util.ArrayList;

public class ModifichePostgresDAO implements ModificheDAO {


    /**
     * Creiamo una matrice dove ogni riga è un dato e ogni colonna e un oggetto che andremo a creare.
     * Questa funzione farà tre query:
     * 1) per prendere tutte le informazioni sulle modfiche
     * 2) per prendere il titolo della pagina in cui abbiamo fatto la richiesta di modifica
     * 3) per prendere il titolo della pagina a cui ci riferiamo nel caso sia un link.
     * I dati sono: Id_operazionme, DataR(data Richiesta), Testo, accettata, Visionata, Modifica, Link, Link_pagina, posizione,utente,
     * DataA(data Accettazione) , utenteNotificato, titolo pagina link(titolo della pagina in cui mi riferisco) e titolo pagina
     *
     *
     * dopo aver preso tutti i dati li carichiamo nella matrice
     * @param EmailUtente
     * @return Matrice dove le righe è un tipo di dato e le colonne un oggetto da creare
     * @throws SQLException
     */
    public ArrayList<ArrayList> LoadModifiche(String EmailUtente) throws SQLException {

        ArrayList<ArrayList> Dati = new ArrayList<>();

        Dati.add(new ArrayList<Integer>());
        Dati.add(new ArrayList<Timestamp>());
        Dati.add(new ArrayList<String>());
        Dati.add(new ArrayList<Boolean>());
        Dati.add(new ArrayList<Boolean>());
        Dati.add(new ArrayList<Boolean>());
        Dati.add(new ArrayList<Boolean>());
        Dati.add(new ArrayList<Integer>());
        Dati.add(new ArrayList<Integer>());
        Dati.add(new ArrayList<String>());
        Dati.add(new ArrayList<Timestamp>());
        Dati.add(new ArrayList<String>());
        Dati.add(new ArrayList<String>());
        Dati.add(new ArrayList<String>());


        Connection con = new ConnessionePostges().openConnection();
        Statement statement = con.createStatement();
        Statement statement2 = con.createStatement();
        Statement statement3 = con.createStatement();

        String query="SELECT id_operazione,dataa,datar,testo,accettata,visionata,posizioneins,modifica,link,link_pagina,autore_notificato,utente, pagina_frase FROM Operazione_utente WHERE utente = '%s'".formatted(EmailUtente);
        ResultSet Modifiche = statement.executeQuery(query);


        while(Modifiche.next()){
            int row = 0;
            query = "SELECT titolo FROM Pagina where id_pagina = %d".formatted(Modifiche.getInt("link_pagina"));
            ResultSet TitoloLink = statement2.executeQuery(query);

            query = "SELECT titolo FROM Pagina where id_pagina = %d".formatted(Modifiche.getInt("pagina_frase"));
            ResultSet Titolo = statement3.executeQuery(query);

            for (ArrayList l : Dati){
                if(row == 0){
                    l.add(Modifiche.getInt("id_operazione"));
                }else if(row == 1){
                    l.add(Modifiche.getTimestamp("datar"));
                }else if(row == 2){
                    l.add(Modifiche.getString("testo"));
                }else if(row == 3){
                    l.add(Modifiche.getBoolean("accettata"));
                }else if(row == 4){
                    l.add(Modifiche.getBoolean("visionata"));
                }else if(row == 5){
                    l.add(Modifiche.getBoolean("modifica"));
                }else if(row == 6){
                    l.add(Modifiche.getBoolean("link"));
                }else if(row == 7){
                    l.add(Modifiche.getInt("link_pagina"));
                }else if(row == 8){
                    //System.out.printf("\n"+Modifiche.getInt("posizioneins"));
                    l.add(Modifiche.getInt("posizioneins"));
                }else if(row == 9){
                    l.add(Modifiche.getString("utente"));
                }else if(row == 10){
                    l.add(Modifiche.getTimestamp("dataa"));
                }else if(row == 11){
                    l.add(Modifiche.getString("autore_notificato"));
                }
                else if(row == 12){
                    if(Modifiche.getInt("link_pagina") == 0){
                        l.add("null");
                    }else {
                        TitoloLink.next();
                        l.add(TitoloLink.getString("titolo"));
                    }
                }else if(row == 13){
                    Titolo.next();
                    l.add(Titolo.getString("titolo"));
                }
                row++;
            }
            TitoloLink.close();
            Titolo.close();
        }

        con.close();
        Modifiche.close();
        statement.close();
        statement2.close();
        statement3.close();


        return Dati;
    }
}
