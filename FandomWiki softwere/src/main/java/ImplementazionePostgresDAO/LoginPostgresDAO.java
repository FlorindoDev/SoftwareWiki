package main.java.ImplementazionePostgresDAO;

import main.java.DAO.LoginDAO;
import main.java.Database.ConnessionePostges;

import java.sql.*;
import java.util.ArrayList;

public class LoginPostgresDAO implements LoginDAO {

    /**
     * Effettua il login dell'utente utilizzando l'email e la password fornite.
     * Esegue una query sul database per trovare un utente con l'email e la password specificate.
     * @param email L'email dell'utente per il login.
     * @param password La password dell'utente per il login.
     * @return Array di informazioni sull'utente se il login è riuscito, altrimenti null.
     * @throws SQLException Se si verifica un errore durante l'esecuzione della query SQL.
     */
    @Override
    public ArrayList<String> Login(String email, String password) throws SQLException {

        Connection con = new ConnessionePostges().openConnection();
        PreparedStatement statement = con.prepareStatement("SELECT * FROM utente WHERE email = ? AND password_utente = ?");
        statement.setString(1, email);
        statement.setString(2, password);
        ResultSet resultSet = statement.executeQuery();

        con.close();

        ArrayList<String> Contenuto = null;

        if (resultSet.next()) {//se contiene qualcosa allora email e password combaciano

            Contenuto = new ArrayList<String>();

            Contenuto.add(resultSet.getString("autore"));
            Contenuto.add(resultSet.getString("email"));
            Contenuto.add(resultSet.getString("password_utente"));
            Contenuto.add(resultSet.getString("nome"));
            Contenuto.add(resultSet.getString("cognome"));
            Contenuto.add(resultSet.getString("genere"));

        }

        resultSet.close();
        statement.close();
        return Contenuto;
    }
}
