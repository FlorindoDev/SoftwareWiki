package main.java.ImplementazionePostgresDAO;

import main.java.DAO.ConfrontaDAO;
import main.java.Database.ConnessionePostges;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class ConfrontaPostgersDAO implements ConfrontaDAO {

    /**
     * Questa funzione crea una conessione con il database e richiama una funzione del database 'visualizzapropostaandconfronta()'
     * che ristituisce una stringa formatata nel seguente modo se è una modifica Titolo+testo-posizione-link-
     * id pagina riferita-titolo pagina riferita | proposta testo-proposta posizione-
     * proposta link-proposta id pagina riferita-proposta titolo pagina riferita.
     * altrimenti se è un inserimento Titolo+testo-posizione-link-
     * id pagina riferita-titolo pagina riferita
     *
     * @param id_operazione
     * @param email_autore
     * @return questa funzione restituisce il risultato di 'visualizzapropostaandconfronta()'
     * @throws SQLException
     */
    public String LoadConfronto(int id_operazione, String email_autore) throws SQLException {

        Connection con = new ConnessionePostges().openConnection();
        Statement statement = con.createStatement();
        //System.out.print(id_operazione + email_autore);

        String query="SELECT visualizzapropostaandconfronta(%d,'%s')".formatted(id_operazione, email_autore);
        ResultSet resultSet = statement.executeQuery(query);

        con.close();

        resultSet.next();
        String Confronto = resultSet.getString("visualizzapropostaandconfronta");
        System.out.println(Confronto);

        resultSet.close();
        statement.close();
        return Confronto;
    }

}
