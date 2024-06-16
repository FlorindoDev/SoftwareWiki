package main.java.DAO;

import java.sql.SQLException;
import java.util.ArrayList;

public interface ModificheDAO {

    ArrayList<ArrayList> LoadModifiche(String EmailUtente) throws SQLException;
}
