package main.java.ImplementazionePostgresDAO;

import main.java.DAO.RegisterDAO;
import main.java.Database.ConnessionePostges;

import java.sql.*;

public class RegisterPostgerDAO implements RegisterDAO {

    /**
     * Registra un nuovo utente nel DB.
     * Verifica che i parametri Nome, Cognome e Password non siano vuoti o nulli.
     * Esegue una query sul database per controllare se l'indirizzo email fornito è già associato a un altro utente.
     * Se l'indirizzo email non è già presente nel database, inserisce i dettagli dell'utente nel database.
     * Restituisce un messaggio di errore che descrive eventuali problemi riscontrati durante il processo di registrazione.
     * @param Nome Il nome dell'utente da registrare.
     * @param Cognome Il cognome dell'utente da registrare.
     * @param Genere Il genere dell'utente da registrare.
     * @param Email L'indirizzo email dell'utente da registrare.
     * @param Password La password dell'utente da registrare.
     * @return Un messaggio di errore che descrive eventuali problemi riscontrati durante il processo di registrazione.
     * @throws SQLException Se si verifica un errore durante l'esecuzione delle query SQL.
     */
    public String RegisterUser(String Nome, String Cognome, String Genere, String Email, String Password) throws SQLException {
        String messageError="";
        if (Nome.isBlank()){
            messageError += "nome non valido<br>";
        }
        if (Cognome.isBlank()){
            messageError += "cognome non valido<br>";
        }
        if (Password.isBlank()){
            messageError += "password non valida<br>";
        }

        Connection con = new ConnessionePostges().openConnection();

        PreparedStatement statement = con.prepareStatement("SELECT email FROM utente WHERE email = ?");
        statement.setString(1, Email);
        ResultSet resultSet = statement.executeQuery();
        if (resultSet.next()){
            messageError += "email gia esistente<br>";
        }else {

            PreparedStatement Pstatement = con.prepareStatement("INSERT INTO utente VALUES (?, ?, ?, ?, ?, 0::bit(1))");
            Pstatement.setString(1, Email);
            Pstatement.setString(2, Nome);
            Pstatement.setString(3, Cognome);
            Pstatement.setString(4, Password);
            Pstatement.setString(5, Genere);
            try {
                Pstatement.executeUpdate();//ritorna il numero di righe inserite
                Pstatement.close();
            } catch (SQLException e) { // se duplicato o dominio non valido
                System.out.println(e.getMessage());
                messageError += "email errata<br>";
            }

        }

        statement.close();
        resultSet.close();
        con.close();
        return messageError;

    }

}
