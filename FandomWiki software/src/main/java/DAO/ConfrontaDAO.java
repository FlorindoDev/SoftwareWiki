package main.java.DAO;

import java.sql.SQLException;
import java.util.ArrayList;

public interface ConfrontaDAO {

    String LoadConfronto(int id_operazione, String email_autore) throws SQLException;
}
