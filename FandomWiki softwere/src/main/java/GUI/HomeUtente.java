package main.java.GUI;

import main.java.Controller.Controller;

import javax.swing.*;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;
import javax.swing.plaf.FontUIResource;
import javax.swing.text.StyleContext;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Locale;

public class HomeUtente {
    private JPanel MainPanel;
    private JPanel ToolBar;
    private JPanel BarIcon;
    private JLabel Icon;
    private JLabel NameApp;
    private JPanel BarraDiRicerca;
    private JPanel SerchBorder;
    private JTextField SerchBar;
    private JLabel IconaLente;
    private JPanel ButtonPanel;
    private JMenu Menu;
    private JMenuBar MenuBox;
    private JPanel MenuButton;
    private JLabel IconMenu;
    private JPanel Homescreen;
    private JLabel Logo;
    private JLabel Scritta;
    private Boolean bool = true;

    public HomeUtente(MainJFrame frame, Controller controller, JPanel oldPanel) {
        GuiPresetComponet t = new GuiPresetComponet(frame);


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

        Menu = new JMenu("Menu");
        MenuBox = new Menu(frame, Menu, oldPanel, MainPanel, controller);


        t.SetIcon(IconMenu, new ImageIcon(t.ResizeIcon(20, 20, new ImageIcon("src\\main\\resources\\dots.png"))));

        MenuButton.add(MenuBox);
        MenuButton.setBackground(frame.getColorToolBar());
        t.SetIcon(Logo, new ImageIcon(t.ResizeIcon(120, 120, frame.getIcon())));
        t.LabelSetFontAndColorUpper(Scritta);


        Menu.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                super.mouseEntered(e);
                Menu.setBackground(new Color(199, 111, 91));
                MenuButton.setBackground(new Color(199, 111, 91));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                super.mouseExited(e);
                MenuButton.setBackground(frame.getColorToolBar());
                Menu.setBackground(frame.getColorToolBar());
            }

        });

        MenuButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                super.mouseEntered(e);
                MenuButton.setBackground(new Color(199, 111, 91));
                Menu.setBackground(new Color(199, 111, 91));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                super.mouseExited(e);
                MenuButton.setBackground(frame.getColorToolBar());
                Menu.setBackground(frame.getColorToolBar());
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

