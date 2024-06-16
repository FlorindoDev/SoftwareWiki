package main.java.ImplementazionePostgresDAO;

import main.java.DAO.ModificaPorpostaDAO;
import main.java.Database.ConnessionePostges;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class ModificaPorpostaPostgresDAO implements ModificaPorpostaDAO {

    /**
     * Questa funzione crea una conessione con il database e richiama una funzione del database 'modificarichestaproposta()'
     * che modifica la proposta fatta a una pagina wiki
     * @param id_proposta
     * @param Email
     * @param Testo
     */
    @Override
    public void AggironamentoProposta(int id_proposta, String Email, String Testo) {
        Connection con = new ConnessionePostges().openConnection();
        Statement statement = null;
        try {
            statement = con.createStatement();
            String query="call modificarichestaproposta(%d,'%s'::VARCHAR(41),'%s'::text)".formatted(id_proposta,Email,Testo);
            statement.executeQuery(query);
            con.close();
            statement.close();
        } catch (SQLException e) {
            System.out.printf("[] Messaggio:%s CODE: %d\n",e.getMessage(),e.getErrorCode());
        }


    }

    /**
     * Crea una connessione con il database e esegue una query che restituisce il numero di modifiche proposte su altre pagine.
     * @param email
     * @return restituisce il numero di modifche su latre pagine di un utente(tramite email)
     *
     * @throws SQLException
     */

    @Override
    public int NumeroModifiche(String email) throws SQLException {

        Connection con = new ConnessionePostges().openConnection();
        Statement statement = con.createStatement();
        String query="SELECT count(*) as sum from operazione_utente where utente like '%s' group by utente".formatted(email);
        ResultSet num = statement.executeQuery(query);
        con.close();
        if(!num.next()){
            return 0;
        }
        //System.out.print("\n|"+num.getInt("sum")+"\n|");
        return num.getInt("sum");

    }
}
