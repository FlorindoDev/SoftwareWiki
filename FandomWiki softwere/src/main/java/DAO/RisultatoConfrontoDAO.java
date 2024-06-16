package main.java.DAO;

import java.sql.SQLException;

public interface RisultatoConfrontoDAO {

    void Accettazione(int id_operazione, String Email);

    void Rifiuto(int id_operazione, String Email);

}
