package main.java.ImplementazionePostgresDAO;

import main.java.DAO.RisultatoConfrontoDAO;
import main.java.Database.ConnessionePostges;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class RisultatoConfrontoPostgresDAO implements RisultatoConfrontoDAO {


    /**
     * Questa funzione crea una conessione con il database e richiama una funzione del database 'accettaproposta()'
     * che accettera la proposta se accettata = 1
     * @param id_operazione
     * @param Email
     */
    public void Accettazione(int id_operazione, String Email){

        Connection con = new ConnessionePostges().openConnection();
        Statement statement = null;
        try {
            statement = con.createStatement();
        } catch (SQLException e) {
            System.out.printf("[+] Errore durante creazione statment: CODE %d\n", e.getErrorCode());
        }

        String query="call accettaproposta('%s'::d_email, %d, 1::bit(1))".formatted(Email, id_operazione);
        try {
            statement.executeQuery(query);
        } catch (SQLException e) {
            System.out.printf("[+] %s: CODE %d\n",e.getMessage(), e.getErrorCode());
        }


    }

    /**
     * Questa funzione crea una conessione con il database e richiama una funzione del database 'accettaproposta()'
     *  che accettera la proposta se accettata = 0
     * @param id_operazione
     * @param Email
     */
    @Override
    public void Rifiuto(int id_operazione, String Email) {
        Connection con = new ConnessionePostges().openConnection();
        Statement statement = null;
        try {
            statement = con.createStatement();
        } catch (SQLException e) {
            System.out.printf("[+] Errore durante creazione statment: CODE %d\n", e.getErrorCode());
        }

        String query="call accettaproposta('%s'::d_email , %d, 0::bit(1))".formatted(Email, id_operazione);
        try {
            statement.executeQuery(query);
        } catch (SQLException e) {
            System.out.printf("[+] %s: CODE %d\n",e.getMessage(), e.getErrorCode());
        }
    }


}
