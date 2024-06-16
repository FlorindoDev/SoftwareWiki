package main.java.GUI;

import main.java.Controller.Controller;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.plaf.FontUIResource;
import javax.swing.text.StyleContext;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Locale;

public class NotificheFrame extends JFrame {

    private JPanel MainJPanel;
    private JPanel Notifiche;
    private JScrollPane MainScrollPane;
    private JPanel ToolBar;
    private JPanel BarIcon;
    private JLabel Icon;
    private JLabel NameApp;
    private JPanel BarraDiRicerca;
    private JPanel ButtonPanel;
    private JPanel RefreshBox;
    private JLabel IconRefresh;
    private JButton RefreshButton;
    private ArrayList<JPanel> NotificheOnPanel = new ArrayList<>();
    private int NumNot;

    public NotificheFrame(String Nome, MainJFrame frame, Controller controller) {

        super(Nome);
        JFrame f = this;
        this.setDefaultCloseOperation(HIDE_ON_CLOSE);
        this.pack();
        this.setVisible(false);
        this.add(MainJPanel);
        this.setSize(600, 800);
        this.setIconImage(frame.getIconImage());
        this.setResizable(false);
        //this.setUndecorated(true);
        this.getRootPane().setWindowDecorationStyle(JRootPane.NONE);

        GuiPresetComponet t = new GuiPresetComponet(frame);
        ButtonPanel.setBackground(frame.getColorToolBar());

        ToolBar.setBackground(frame.getColorToolBar());

        t.SetIcon(Icon, new ImageIcon(t.ResizeIcon(65, 65, frame.getIcon())));

        t.SetIcon(IconRefresh, new ImageIcon(t.ResizeIcon(30, 30, new ImageIcon("src/main/resources/refresh.png"))));

        BarraDiRicerca.setBackground(frame.getColorToolBar());

        NameApp.setFont(frame.getFontToolBar());

        NameApp.setForeground(Color.BLACK);

        RefreshBox.setBackground(frame.getColorToolBar());
        t.ToolBarButton(RefreshButton);

        MainScrollPane.setBorder(null);

        Notifiche.setBackground(frame.getColorBack());
        MainScrollPane.setBackground(frame.getColorBack());
        MainJPanel.setBackground(frame.getColorBack());

        try {
            controller.LoadNotifiche();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        load(controller, frame);


        RefreshBox.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                super.mouseEntered(e);
                RefreshBox.setBackground(new Color(199, 111, 91));
                RefreshButton.setBackground(new Color(199, 111, 91));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                super.mouseExited(e);
                RefreshBox.setBackground(frame.getColorToolBar());
                RefreshButton.setBackground(frame.getColorToolBar());
            }

            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                RefreshAndReload(controller, frame, f);
                RefreshAndLoad(controller, frame, f);
            }
        });

        RefreshButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                super.mouseEntered(e);
                RefreshBox.setBackground(new Color(199, 111, 91));
                RefreshButton.setBackground(new Color(199, 111, 91));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                super.mouseExited(e);
                RefreshBox.setBackground(frame.getColorToolBar());
                RefreshButton.setBackground(frame.getColorToolBar());
            }

        });

        RefreshButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (controller.NumerOfNotifiche() != NumNot) {
                    RefreshAndLoad(controller, frame, f);
                } else {
                    RefreshAndReload(controller, frame, f);
                }
            }

        });

    }

    public void load(Controller controller, MainJFrame frame) {
        NotificheOnPanel.clear();
        ArrayList<ArrayList> s = controller.GetNotifiche();
        //System.out.print(s);
        //System.out.printf("%d", s.get(0).size());
        NumNot = s.get(0).size();
        for (int i = 0; i < NumNot; i++) {

            GridBagConstraints gbc;
            gbc = new GridBagConstraints();
            gbc.gridx = 0;
            gbc.gridy = i;
            NotifichePanel toadd = new NotifichePanel(frame, controller, this, (int) s.get(0).get(i), (Timestamp) s.get(1).get(i), (String) s.get(2).get(i), (Boolean) s.get(3).get(i), (Boolean) s.get(4).get(i), (Boolean) s.get(5).get(i), (Boolean) s.get(6).get(i), (int) s.get(7).get(i), (String) s.get(9).get(i));
            NotificheOnPanel.add(toadd);
            Notifiche.add(toadd, gbc);

        }
    }

    public void RefreshAndLoad(Controller controller, MainJFrame frame, JFrame f) {
        //if (controller.NumerOfNotifiche() != NumNot) {
            for (int i = 0; i < NumNot; i++) {
                Notifiche.remove(NotificheOnPanel.get(i));
                //Notifiche.add(NotificheOnPanel.get(i));
            }
            controller.Resize(600, 800, f);
            try {
                controller.LoadNotifiche();
            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            }
            load(controller, frame);
        //}
    }

    public void RefreshAndReload(Controller controller, MainJFrame frame, JFrame f) {
        //if (controller.NumerOfNotifiche() == NumNot) {
            for (int i = 0; i < NumNot; i++) {
                Notifiche.remove(NotificheOnPanel.get(i));
                //Notifiche.add(NotificheOnPanel.get(i));
            }
            controller.Resize(600, 800, f);
            load(controller, frame);
        //}
    }


}
