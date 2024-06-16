package main.java.ImplementazionePostgresDAO;

import main.java.DAO.VisualizzaDAO;
import main.java.Database.ConnessionePostges;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class VisualizzaPostgersDAO implements VisualizzaDAO {

    public void Visionata(int id_operazione) throws SQLException {

        Connection con = new ConnessionePostges().openConnection();
        Statement statement = con.createStatement();

        String query="UPDATE operazione_utente SET visionata = 1::bit(1) where id_operazione = %s".formatted(id_operazione);
        ResultSet resultSet = statement.executeQuery(query);

        con.close();

        resultSet.close();
        statement.close();
    }

}
