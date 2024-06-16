package main.java.GUI;

import main.java.Controller.Controller;

import javax.swing.*;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;
import javax.swing.plaf.FontUIResource;
import javax.swing.text.StyleContext;

import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.sql.SQLException;
import java.util.Locale;

public class Home {

    private boolean bool = true;
    private JPanel MainPanel;
    private JButton registerbutton;
    private JButton loginbutton;
    private JPanel ToolBar;
    private JPanel ButtonPanel;
    private JLabel Icon;
    private JPanel BarraDiRicerca;
    private JTextField SerchBar;
    private JLabel IconaLente;
    private JLabel NameApp;
    private JPanel BarIcon;
    private JPanel SerchBorder;
    private JPanel Homescreen;
    private JLabel Logo;
    private JLabel Scritta;

    public Home(MainJFrame frame, Controller controller) throws SQLException {

        GuiPresetComponet t = new GuiPresetComponet(frame);

        t.ToolBarButton(loginbutton);

        t.ToolBarButton(registerbutton);

        ButtonPanel.setBackground(frame.getColorToolBar());

        ToolBar.setBackground(frame.getColorToolBar());

        t.SetIcon(Icon, new ImageIcon(t.ResizeIcon(65, 65, frame.getIcon())));

        t.SetIcon(IconaLente, new ImageIcon(t.ResizeIcon(20, 20, new ImageIcon("src\\main\\resources\\magnifying-glass.png"))));

        BarraDiRicerca.setBackground(frame.getColorToolBar());

        MainPanel.setBackground(frame.getColorBack());

        //SerchBar.setText(" ".repeat(120));

        NameApp.setFont(frame.getFontToolBar());

        NameApp.setForeground(Color.BLACK);

        //SerchBar.setBorder(new LineBorder(Color.BLACK,1));
        SerchBar.setBorder(null);

        SerchBar.setPreferredSize(new Dimension(500, 30));

        SerchBar.setBackground(new Color(199, 111, 91));

        SerchBorder.setBorder(new LineBorder(Color.BLACK, 2));

        SerchBar.setFont(frame.getFontToolBar());

        t.SetIcon(Logo, new ImageIcon(t.ResizeIcon(120, 120, frame.getIcon())));
        t.LabelSetFontAndColorUpper(Scritta);

        loginbutton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                frame.SetNewPanel(new LoginPage(frame, MainPanel, controller).getPanel(), MainPanel);
                loginbutton.setBackground(frame.getColorToolBar());
                frame.Resize(700, 850);

            }
        });

        loginbutton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                super.mouseEntered(e);
                loginbutton.setBackground(new Color(199, 111, 91));


            }

            @Override
            public void mouseExited(MouseEvent e) {
                super.mouseExited(e);
                loginbutton.setBackground(frame.getColorToolBar());
            }
        });

        registerbutton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                super.mouseEntered(e);
                registerbutton.setBackground(new Color(199, 111, 91));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                super.mouseExited(e);
                registerbutton.setBackground(frame.getColorToolBar());
            }

        });

        registerbutton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                frame.SetNewPanel(new RegisterPage(frame, MainPanel, controller).getPanel(), MainPanel);
                registerbutton.setBackground(frame.getColorToolBar());
                frame.Resize(700, 950);

                // frame.SetNewPanel(new HomeAutore(frame, controller, MainPanel).getPanel(), MainPanel);

            }
        });

        SerchBar.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                super.mouseEntered(e);
                if (bool) {
                    bool = false;
                    SerchBar.setText("");
                }

            }
        });

        IconaLente.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                super.mouseEntered(e);
                frame.SetNewPanel(new SearchPage(frame, MainPanel, controller, SerchBar.getText()).getPanel(), MainPanel);
            }
        });


        /*
        IconaLente.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                super.mouseEntered(e);
                frame.SetNewPanel(new SearchPage(frame, MainPanel,controller, SerchBar.getText()).getPanel(), MainPanel);
            }
        });
        */
    }

    public JPanel getPanel() {
        return MainPanel;
    }

}