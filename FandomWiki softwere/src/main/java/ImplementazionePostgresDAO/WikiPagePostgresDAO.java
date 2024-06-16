package main.java.ImplementazionePostgresDAO;

import main.java.DAO.WikiPageDAO;
import main.java.Database.ConnessionePostges;

import java.sql.*;

public class WikiPagePostgresDAO implements WikiPageDAO {

    /**
     * Propone l'inserimento / inserisce una nuova frase (a seconda dell'utente) nella pagina specificata.
     * Se la frase include un riferimento a un'altra pagina (link), verifica l'esistenza della pagina referenziata.
     * Se l'utente è un autore, esegue una stored procedure per inserire direttamente la frase nella pagina.
     * Se l'utente non è un autore, esegue una stored procedure per richiedere l'operazione agli amministratori.
     * @param isAutore True se l'utente è un autore, altrimenti False.
     * @param idPagina L'ID della pagina in cui inserire la frase.
     * @param email L'indirizzo email dell'utente che propone l'inserimento.
     * @param text Il testo della frase da inserire.
     * @param posizione La posizione in cui inserire la frase nella pagina.
     * @param selected True se la frase contiene un link a un'altra pagina, altrimenti False.
     * @param riferimentoLink Il titolo della pagina a cui la frase fa riferimento, se presente.
     * @return Un messaggio di conferma o di errore sull'avvenuto inserimento o sulla richiesta effettuata.
     * @throws SQLException Se si verifica un errore durante l'esecuzione delle query SQL.
     */
    @Override
    public String proponiInserimento(boolean isAutore, int idPagina, String email, String text, int posizione, boolean selected, String riferimentoLink) throws SQLException {

        Connection con = new ConnessionePostges().openConnection();
        String MessageReturn = new String();
        String query;

        if (selected){
            int linkValue;
            query = "SELECT id_pagina FROM pagina WHERE titolo = ?";
            try (PreparedStatement checkPageStatement = con.prepareStatement(query)) {
                checkPageStatement.setString(1, riferimentoLink);
                try (ResultSet checkPage = checkPageStatement.executeQuery()) {
                    if (!checkPage.next()) {
                        con.close();
                        return MessageReturn += "La pagina a cui la frase fa riferimento è inesistente<br>";
                    }
                    linkValue = checkPage.getInt("id_pagina");
                    checkPage.close();
                }
                checkPageStatement.close();
            }

            if (isAutore) {
                query = "CALL insertfraseautore('%d', '%s'::VARCHAR(255), '%s'::TEXT, 1::bit(1), '%s'::VARCHAR(255), %d)".formatted(idPagina, email, text, riferimentoLink, posizione);
            }else {
                query = "CALL operazioneutenterichiesta('%s'::VARCHAR(255), '%s'::TEXT, %d, 0::bit(1), %d, %d, 1::bit(1), %d)".formatted(email, text, posizione, idPagina, posizione, linkValue);
            }



        }else{

            if (isAutore) {
                query = "CALL insertfraseautore('%d', '%s'::VARCHAR(255), '%s'::TEXT, 0::bit(1), '%s'::VARCHAR(255), %d)".formatted(idPagina, email, text, null, posizione);
            }else {
                query = "CALL operazioneutenterichiesta('%s'::VARCHAR(255), '%s'::TEXT, %d, 0::bit(1), %d, %d, 0::bit(1), %d)".formatted(email, text, posizione, idPagina, posizione, null);
            }

        }

        Statement stm = con.createStatement();
        boolean result = stm.execute(query);

        System.out.println(result);
        if (!result){ //la procedura non deve ritornare valori
            MessageReturn = "Richiesta avvenuta con successo<br>";
        }else{
            ResultSet rs = stm.getResultSet();
            System.out.println(rs);
        }

        con.close();
        stm.close();
        return MessageReturn;
    }

    /**
     * Propone la modifica / modifica nuova frase (a seconda dell'utente) nella pagina specificata.
     * Se la frase include un riferimento a un'altra pagina (link), verifica l'esistenza della pagina referenziata.
     * Se l'utente è un autore, esegue una stored procedure per mofificare direttamente la frase nella pagina.
     * Se l'utente non è un autore, esegue una stored procedure per richiedere la modifica agli amministratori.
     * @param isAutore True se l'utente è un autore, altrimenti False.
     * @param idPagina L'ID della pagina in cui inserire la frase.
     * @param email L'indirizzo email dell'utente che propone l'inserimento.
     * @param text Il testo della frase da inserire.
     * @param posizione La posizione in cui inserire la frase nella pagina.
     * @param selected True se la frase contiene un link a un'altra pagina, altrimenti False.
     * @param riferimentoLink Il titolo della pagina a cui la frase fa riferimento, se presente.
     * @return Un messaggio di conferma o di errore sull'avvenuta modifica/proposta.
     * @throws SQLException Se si verifica un errore durante l'esecuzione delle query SQL.
     */
    @Override
    public String proponiModifica(boolean isAutore, int idPagina, String email, String text, int posizione, boolean selected, String riferimentoLink) throws SQLException {

        /*System.out.println("isAutore: " + isAutore);
        System.out.println("idPagina: " + idPagina);
        System.out.println("email: " + email);
        System.out.println("text: " + text);
        System.out.println("posizione: " + posizione); //reale
        System.out.println("selected: " + selected);
        System.out.println("riferimentoLink: " + riferimentoLink);*/

        Connection con = new ConnessionePostges().openConnection();
        String MessageReturn = new String();
        String query;

        if (selected){
            int linkValue;
            query = "SELECT id_pagina FROM pagina WHERE titolo = ?";
            //System.out.println("[D] è un link e il titolo: "+riferimentoLink+" e la query: "+query);
            try (PreparedStatement checkPageStatement = con.prepareStatement(query)) {
                checkPageStatement.setString(1, riferimentoLink);
                try (ResultSet checkPage = checkPageStatement.executeQuery()) {
                    if (!checkPage.next()) {
                        con.close();
                        return MessageReturn += "La pagina a cui la frase fa riferimento è inesistente<br>";
                    }
                    linkValue = checkPage.getInt("id_pagina");
                    checkPage.close();
                }
                checkPageStatement.close();
            }

            if (isAutore) {
                query = "CALL modificafraseautore('%d', '%s'::VARCHAR(255), '%s'::TEXT, 1::bit(1), '%s'::VARCHAR(255), %d)".formatted(idPagina, email, text, riferimentoLink, posizione);
            }else {
                query = "CALL operazioneutenterichiesta('%s'::VARCHAR(255), '%s'::TEXT, %d, 1::bit(1), %d, %d, 1::bit(1), %d)".formatted(email, text, posizione, idPagina, posizione, linkValue);
            }

        }else{

            if (isAutore) {
                query = "CALL modificafraseautore('%d', '%s'::VARCHAR(255), '%s'::TEXT, 0::bit(1), '%s'::VARCHAR(255), %d)".formatted(idPagina, email, text, null, posizione);
            }else {
                query = "CALL operazioneutenterichiesta('%s'::VARCHAR(255), '%s'::TEXT, %d, 1::bit(1), %d, %d, 0::bit(1), %d)".formatted(email, text, posizione, idPagina, posizione, null);
            }

        }


        Statement stm = con.createStatement();
        boolean result = stm.execute(query);

        //System.out.println(result);
        if (!result){ //la procedura non deve ritornare valori
            MessageReturn = "Richiesta avvenuta con successo<br>";
        }else{
            ResultSet rs = stm.getResultSet();
            System.out.println(rs);
        }

        con.close();
        stm.close();
        return MessageReturn;
    }

}
