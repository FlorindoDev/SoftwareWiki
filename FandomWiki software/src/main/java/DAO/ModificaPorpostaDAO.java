package main.java.DAO;

import java.sql.SQLException;

public interface ModificaPorpostaDAO {

    void AggironamentoProposta(int id_proposta, String Email, String Testo);

    int NumeroModifiche(String email) throws SQLException;

}
