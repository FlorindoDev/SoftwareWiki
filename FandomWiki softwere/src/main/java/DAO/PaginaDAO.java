package main.java.DAO;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;

public interface PaginaDAO {

    HashMap<Integer, ArrayList<String>> getWikiPage(int idPagina) throws SQLException;

    ArrayList<String> getWikiInfo(int idPagina) throws SQLException;

    String createPage(String email, String titolo, String frase, boolean link, String TitoloLink) throws SQLException;

    ArrayList<ArrayList<String>> getMyPage(String email) throws SQLException;

    ArrayList<ArrayList<String>> getStroicitaSpecifica(int idPagina, String data) throws SQLException;

    ArrayList<String> getDateAvailable(int idPagina) throws SQLException;

    ArrayList<ArrayList<String>> SearchPage(String ricerca) throws SQLException;
}
