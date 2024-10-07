package main.java.GUI;

import main.java.Controller.Controller;

import java.sql.SQLException;

public class Main {
    private static final Controller controller = new Controller();
    public static void main(String[] args) throws SQLException {

       MainJFrame frame = new MainJFrame("Wikipedia", 1500,700);
       frame.SetPanel(new Home(frame, controller).getPanel());
       frame.Resize(1400,700);

    }

}