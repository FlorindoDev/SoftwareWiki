package main.java.GUI;

import main.java.Controller.Controller;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class SearchPanelPage extends JPanel {
    private JButton LinkToPage;
    private JLabel Intestazione;
    private JPanel MainPanel;
    private JPanel Link;
    private Boolean clic = true;

    public SearchPanelPage(MainJFrame frame, Controller controller, JPanel OldPanel, String Titolo, String Id_pagina, String NomeAutore, String Ultima_modifica) {
        GuiPresetComponet t = new GuiPresetComponet(frame);
        this.add(MainPanel);
        LinkToPage.setActionCommand(Id_pagina);
        String a[] = NomeAutore.split(";");
        Intestazione.setText("<html> Nome Autore: " + a[0] + " " + a[1] + " <br> Ultima Modifica: " + Ultima_modifica + "</html>");
        LinkToPage.setText(Titolo);
        this.setBorder(null);
        Link.setBackground(frame.getColorBack());
        Link.setBorder(null);
        this.setBackground(frame.getColorBack());
        LinkToPage.setBorder(null);
        LinkToPage.setFont(frame.getFontToolBarLower());
        LinkToPage.setBackground(frame.getColorBack());
        LinkToPage.setForeground(new Color(0, 0, 0));

        LinkToPage.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                super.mouseEntered(e);
                if(clic){
                    LinkToPage.setForeground(new Color(199, 111, 91));
                }

            }

            @Override
            public void mouseExited(MouseEvent e) {
                super.mouseExited(e);
                if(clic){
                    LinkToPage.setForeground(new Color(0, 0, 0));
                }

            }
        });

        LinkToPage.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                LinkToPage.setForeground(new Color(199, 111, 91));
                clic = false;
            }
        });


        LinkToPage.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int buttonHidden = Integer.parseInt(((JButton) e.getSource()).getActionCommand());
                //System.out.println("Hai premuto il link con id: " + e.getActionCommand());
                frame.SetNewPanel(new WikiPage(frame, OldPanel, controller, buttonHidden).getPanel(), MainPanel);
            }
        });


    }


}
