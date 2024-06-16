package main.java.ImplementazionePostgresDAO;

import main.java.Database.ConnessionePostges;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;

public class PaginaPostgresDAO implements main.java.DAO.PaginaDAO {

    /**
     * Funzione che permette di ottiene le frasi di una pagina wiki identificata dall'ID.
     * Esegue una query sul database per recuperare le frasi associate alla pagina, ordinandole per posizione.
     * @param idPagina L'ID della pagina wiki di cui si desidera ottenere le frasi.
     * @return Restuisce un HashMapdove dove la chiave è l'indice di posizione non reale (ordine crescente)
     *         della frase e il valore è un ArrayList di stringhe contenente i dati della frase.
     * @throws SQLException Se si verifica un errore durante l'esecuzione della query SQL.
     */
    @Override
    public HashMap<Integer, ArrayList<String>> getWikiPage(int idPagina) throws SQLException {
        Connection con = new ConnessionePostges().openConnection();
        PreparedStatement statement = con.prepareStatement("SELECT * FROM frase WHERE pagina = ? order by posizione");
        statement.setInt(1, idPagina);
        ResultSet WikiPage = statement.executeQuery();

        HashMap<Integer, ArrayList<String>> Frasi = new HashMap<>(); // ci sta sempre un elemento nella pagina
        Integer index = 0;

        while(WikiPage.next()){
            index+=1;
            ArrayList<String> frase = new ArrayList<>();
            frase.add(WikiPage.getString("pagina"));
            frase.add(WikiPage.getString("posizione"));
            frase.add(WikiPage.getString("testo"));
            frase.add(WikiPage.getString("link"));
            frase.add(WikiPage.getString("linkpagina"));

            Frasi.put(index, frase);
        }
        //System.out.println(Frasi);

        WikiPage.close();
        statement.close();
        con.close();

        return Frasi;
    }


    /**
     * Funzione che permette di ottiene tutte le informazioni di una pagina wiki identificata dall'ID.
     * Esegue una query sul database per recuperare le informazioni associate alla pagina.
     * @param idPagina L'ID della pagina wiki di cui si desidera ottenere le informazioni.
     * @return Restituisce un Array contenente i dati della pagina.
     * @throws SQLException Se si verifica un errore durante l'esecuzione della query SQL.
     */
    @Override
    public ArrayList<String> getWikiInfo(int idPagina) throws SQLException{

        Connection con = new ConnessionePostges().openConnection();
        Statement statement = con.createStatement();
        String query="SELECT * FROM pagina WHERE id_pagina = %d".formatted(idPagina);
        ResultSet result = statement.executeQuery(query);

        ArrayList<String> pagina_cercata = null;
        if (result.next()){
            pagina_cercata = new ArrayList<>();
            pagina_cercata.add(result.getString("titolo"));
            pagina_cercata.add(result.getString("emailautore"));
            pagina_cercata.add(result.getString("generalita_autore"));
            pagina_cercata.add(result.getString("dataultimamodifica"));
            pagina_cercata.add(result.getString("datacreazione"));
            pagina_cercata.add(result.getString("id_pagina"));
        }

        return pagina_cercata;
    }

    /**
     * Funzione che permette di creare una nuova pagina Wiki.
     * @param email L'email dell'utente che sta creando la pagina.
     * @param titolo Il titolo della pagina wiki da creare.
     * @param frase Prima frase della pagina wiki.
     * @param link True se la frase è un link a un'altra pagina, altrimenti False.
     * @param TitoloLink Il titolo della pagina wiki a cui si desidera collegare, se specificato.
     * @return Un messaggio che indica l'esito dell'operazione di creazione della pagina.
     * @throws SQLException Se si verifica un errore durante l'esecuzione della query SQL.
     */
    @Override
    public String createPage(String email, String titolo, String frase, boolean link, String TitoloLink)throws SQLException{
        Connection con = new ConnessionePostges().openConnection();
        String MessageReturn = new String();
        String query;

        if (link){
                query = "SELECT id_pagina FROM pagina WHERE titolo = ?";
                PreparedStatement stm = con.prepareStatement(query);
                stm.setString(1, TitoloLink);
                ResultSet exist = stm.executeQuery();

                if (!exist.next()) {
                    con.close();
                    stm.close();
                    exist.close();
                    return MessageReturn += "La pagina a cui la frase fa riferimento è inesistente<br>";
                }

                query = "CALL creazionepagina('%s'::VARCHAR(255), '%s'::VARCHAR(255), '%s'::TEXT, 1::bit(1), '%s'::VARCHAR(255), %d)".formatted(titolo, email, frase, TitoloLink, 1);

                stm.close();
                exist.close();
        }else{

            query = "CALL creazionepagina('%s'::VARCHAR(255), '%s'::VARCHAR(255), '%s'::TEXT, 0::bit(1), '%s'::VARCHAR(255), %d)".formatted(titolo, email, frase, null, 1);

        }

        Statement stm = con.createStatement();
        boolean result = stm.execute(query);

        if (!result){ //la procedura non deve ritornare valori
            MessageReturn = "Pagina creata con successo<br>";
        }//else{
            //ResultSet rs = stm.getResultSet();
            //System.out.println(rs);
        //}

        return MessageReturn;
    }

    /**
     * Funzione che permette di ottenere le pagine create dall'utente corrispondente all'indirizzo email specificato.
     * Esegue una query sul database per recuperare le pagine create dall'utente.
     * @param email L'indirizzo email dell'utente di cui si vogliono ottenere le pagine create.
     * @return Restituisce una matrice di stringhe contenente le informazioni delle pagine trovate.
     *         Ogni array interno contiene l'ID della pagina e il titolo della pagina.
     *         Se non vengono trovate pagine per l'utente specificato o si verifica un'eccezione durante l'accesso al database, restituisce una lista vuota.
     * @throws SQLException Se si verifica un errore durante l'esecuzione della query SQL.
     */
    @Override
    public ArrayList<ArrayList<String>> getMyPage(String email) throws SQLException {
        Connection con = new ConnessionePostges().openConnection();
        Statement stm = con.createStatement();
        String query = "SELECT * FROM pagina WHERE emailautore = '%s'".formatted(email);
        ResultSet rs = stm.executeQuery(query);

        ArrayList<ArrayList<String>> result = new ArrayList<>();
        while (rs.next()){
            ArrayList<String> tmp = new ArrayList<>();
            tmp.add(rs.getString("id_pagina"));
            tmp.add(rs.getString("titolo"));
            result.add(tmp);
        }

        con.close();
        stm.close();
        rs.close();
        return result;

    }

    /**
     * Ottiene tutte le frasi di una pagina che erano presenti fino ad una certa data spefica.
     * Esegue una query sul database per recuperare le frasi associate alla pagina wiki specificata, eventualmente filtrate per data.
     * @param idPagina L'ID della pagina wiki di cui si desidera ottenere le frasi.
     * @param data La data fino alla quale si desidera ottenere le frasi, o "null" per ottenere tutte le frasi senza restrizioni di data.
     * @return Una matrice di frasi associate alla pagina wiki specificata, contenente il testo e la posizione di ciascuna frase.
     * @throws SQLException Se si verifica un errore durante l'esecuzione della query SQL.
     */
    @Override
    public ArrayList<ArrayList<String>> getStroicitaSpecifica(int idPagina, String data) throws SQLException {
        Connection con = new ConnessionePostges().openConnection();
        Statement stm = con.createStatement();

        String query;

        if(!(data.isEmpty() || data.equals("null"))) {
            query=  "SELECT t1.id_pagina, t1.data_accettazione, t1.data_richiesta, t1.posizione, t1.testo, t1.utente, t1.accettata, t1.modifica, t1.link, t1.link_pagina " +
                    "FROM storicita_totale t1 JOIN " +
                    "(SELECT posizione, MAX(data_accettazione) AS max_data_accettazione " +
                    " FROM storicita_totale " +
                    " WHERE id_pagina = %d AND (accettata != 0::bit(1) OR accettata is null) ".formatted(idPagina) +
                    " GROUP BY posizione) t2 ON t1.posizione = t2.posizione AND t1.data_accettazione = t2.max_data_accettazione " +
                    "WHERE data_accettazione <= '%s' ".formatted(data) +
                    "ORDER BY posizione";
        }else{
            query=  "SELECT t1.id_pagina, t1.data_accettazione, t1.data_richiesta, t1.posizione, t1.testo, t1.utente, t1.accettata, t1.modifica, t1.link, t1.link_pagina " +
                    "FROM storicita_totale t1 JOIN " +
                    "(SELECT posizione, MAX(data_accettazione) AS max_data_accettazione " +
                    " FROM storicita_totale " +
                    " WHERE id_pagina = %d AND (accettata != 0::bit(1) OR accettata is null) ".formatted(idPagina) +
                    " GROUP BY posizione) t2 ON t1.posizione = t2.posizione AND t1.data_accettazione = t2.max_data_accettazione " +
                    "ORDER BY posizione";
        }

        ResultSet rs = stm.executeQuery(query);

        ArrayList<ArrayList<String>> result = new ArrayList<>();
        while (rs.next()){
            ArrayList<String> tmp = new ArrayList<>();
            tmp.add(rs.getString("testo"));
            tmp.add(rs.getString("posizione"));
            result.add(tmp);
        }

        con.close();
        stm.close();
        rs.close();
        return result;
    }

    /**
     * Permette di ottenere tutte le date disponibili per la visualizzazione di una pagina
     * @param idPagina L'ID della pagina wiki di cui si desiderano ottenere le date disponibili.
     * @return Array che contiene tutte le date in ordine decrescente su cui si è accettata una Modifica o Inserimento, di una pagina specifica.
     * @throws SQLException Se si verifica un errore durante l'esecuzione della query SQL.
     */
    @Override
    public ArrayList<String> getDateAvailable(int idPagina) throws SQLException {
        Connection con = new ConnessionePostges().openConnection();
        Statement stm = con.createStatement();
        String query = "SELECT DISTINCT data_accettazione FROM storicita_totale WHERE data_accettazione IS NOT NULL AND id_pagina = %d order by data_accettazione DESC".formatted(idPagina);
        ResultSet rs = stm.executeQuery(query);

        ArrayList<String> result = new ArrayList<>();
        while (rs.next()){
            result.add(rs.getString("data_accettazione"));
        }

        if (result.isEmpty()) {
            return null;
        }else{
            return result;
        }

    }

    /**
     * Esegue una ricerca di pagine wiki in base alla stringa di ricerca specificata.
     * Chiama una Stored Procedure che cerca le pagine wiki che corrispondono alla stringa di ricerca fornita.
     * @param ricerca La stringa di ricerca per trovare le pagine wiki.
     * @return Restituisce una matrice di stringhe, ogni sotto-array contiene le informazioni delle pagine wiki trovate, come:
     *         titolo, generalita autore, data dell'ultima modifica e ID della pagina.
     *         Se la stringa di ricerca non trova corrispondenze, restituisce null.
     * @throws SQLException Se si verifica un errore durante l'esecuzione della query SQL.
     */
    @Override
    public ArrayList<ArrayList<String>> SearchPage(String ricerca) throws SQLException {

        Connection con = new ConnessionePostges().openConnection();
        Statement statement = con.createStatement();

        String query="SELECT * FROM trovapagina('%s')".formatted(ricerca);
        ResultSet idPagine = statement.executeQuery(query);

        //-1 ritorna se non trova pagine
        // '' ritorna tutte le pagine

        idPagine.next();
        String idPagineReturn = idPagine.getString("trovapagina");
        if (idPagineReturn.equals("-1")){
            idPagine.close();
            statement.close();
            con.close();
            return null;
        }else {

            ArrayList<ArrayList<String>> DataPages = new ArrayList<>();

            String query2="SELECT * FROM pagina WHERE id_pagina = %s";

            String[] pagina = idPagineReturn.split("-");
            for (String part : pagina) {
                ResultSet data = statement.executeQuery(query2.formatted(part));

                ArrayList<String> dataList = new ArrayList<>();
                while(data.next()){
                    dataList.add(data.getString("titolo"));
                    dataList.add(data.getString("generalita_autore"));
                    dataList.add(data.getString("dataultimamodifica"));
                    dataList.add(data.getString("id_pagina"));
                }
                DataPages.add(dataList);
            }

            idPagine.close();
            statement.close();
            con.close();
            return DataPages;

        }

    }
}
