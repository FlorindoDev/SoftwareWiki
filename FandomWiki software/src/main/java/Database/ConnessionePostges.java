package main.java.Database;

//sql
import java.io.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
//file
import java.util.ArrayList;
import java.util.Scanner;


public class ConnessionePostges {

    public Connection openConnection() {

        ArrayList<String> Credenziali = new ArrayList<>();
        String filePath = "src/main/java/Database/credenziali.txt";
        File file = new File(filePath);

        try {
            Scanner scanner = new Scanner(file);
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                Credenziali.add(line);
            }
            scanner.close();
        } catch (FileNotFoundException e) {
            System.out.println("File non trovato: " + e.getMessage());
        }

        try {
            Class.forName("org.postgresql.Driver");
            Connection con = DriverManager.getConnection("jdbc:postgresql://"+ Credenziali.get(0), Credenziali.get(1), Credenziali.get(2));
            return con;
        } catch (ClassNotFoundException e) {
            System.out.println("[-] DB driver not found \n");
        } catch (SQLException e) {
            System.out.println("[-] Connessione Fallita \n");
        }
        return null;
    }

}