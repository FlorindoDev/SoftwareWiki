package main.java.GUI;

import main.java.Controller.Controller;

import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.Timestamp;

public class PanelRichieste extends JPanel {

    private JFrame ModificaFrame;
    private JPanel MainPanel;
    private JLabel DataAccettazione;
    private JLabel DataRichiesta;
    private JLabel Accettata;
    private JLabel Visionata;
    private JLabel Modifica;
    private JLabel Link;
    private JLabel PropetarioPagina;
    private JLabel TitoloPagina;

    public PanelRichieste(MainJFrame frame, Controller controller, FrameRichieste FrameModifche, int Id_operazione, Timestamp dataa, Timestamp datar, String testo, boolean accettata, boolean visionata, boolean modifica, boolean link, int link_pagina, int posizione, String Utente, String Autore, String Titolo_pagina_link, String Titolo) {

        GuiPresetComponet t = new GuiPresetComponet(frame);

        PropetarioPagina.setText("Autore Pagina: " + Autore);
        if(datar != null){
            DataRichiesta.setText("Data Richesta: " + datar.toString());
        }else{
            DataRichiesta.setText("Data Richesta: ");
        }

        if (dataa != null) {
            DataAccettazione.setText("Data Accettazione: " + dataa.toString());
        } else {
            DataAccettazione.setText("Data Accettazione: " + "Non c'è");
        }

        Modifica.setText("Modifica: " + String.valueOf(modifica));
        Accettata.setText("Accettata: " + String.valueOf(accettata));
        Visionata.setText("Visionata: " + String.valueOf(visionata));
        Link.setText("Link: " + String.valueOf(link));
        MainPanel.setBackground(frame.getColorToolBar());
        MainPanel.setBorder(new LineBorder(Color.BLACK, 2));
        TitoloPagina.setText("Titolo: " + Titolo);
        this.add(MainPanel);
        this.setBackground(frame.getColorBack());
        MainPanel.setForeground(Color.BLACK);
        t.LabelSetTextBlack(PropetarioPagina);
        t.LabelSetTextBlack(DataRichiesta);
        t.LabelSetTextBlack(Modifica);
        t.LabelSetTextBlack(Accettata);
        t.LabelSetTextBlack(Visionata);
        t.LabelSetTextBlack(Link);
        t.LabelSetTextBlack(DataAccettazione);
        t.LabelSetTextBlack(TitoloPagina);


        MainPanel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                super.mouseEntered(e);
                MainPanel.setBackground(frame.getColorBack());
            }

            @Override
            public void mouseExited(MouseEvent e) {
                super.mouseExited(e);
                MainPanel.setBackground(frame.getColorToolBar());
            }

            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);

                ModificaFrame = new ModificaPropostaFrame("Comparazione", frame, controller, FrameModifche, Id_operazione, testo, visionata, modifica, link, posizione, Utente, Autore, Titolo_pagina_link, Titolo);
                controller.SetVisionataNotificheModel(Id_operazione);


            }
        });
    }


}
