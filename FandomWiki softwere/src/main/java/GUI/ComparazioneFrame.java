package main.java.GUI;

import main.java.Controller.Controller;

import javax.swing.*;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;
import javax.swing.plaf.FontUIResource;
import javax.swing.text.StyleContext;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Locale;

public class ComparazioneFrame extends JFrame {
    private JPanel MainJPanel;
    private JButton AcceptButton;
    private JButton RejectButton;
    private JPanel OldTextPanel;
    private JLabel OldTextJLabel;
    private JLabel NewTextJLabel;
    private JPanel ButtonBox;
    private JLabel OldLink;
    private JLabel OldLinkPagina;
    private JLabel NewLink;
    private JPanel NewLinkPage;
    private JLabel NewLinkPagina;
    private JLabel UtenteRichiesta;
    private JLabel Modifica;
    private JLabel OldPosizione;
    private JLabel NewPosizione;
    private JPanel OldLinkPage;
    private JPanel Box;
    private JPanel BoxUtente;
    private JPanel ModificaBox;
    private JPanel OldTextBox;
    private JPanel DivisoreOldBox;
    private JPanel NewTextBox;
    private JPanel DivisoreNewBox;
    private JPanel ToolBar;
    private JPanel BarIcon;
    private JLabel Icon;
    private JLabel NameApp;
    private JPanel NoToolBarComponet;
    private JPanel OldLabelBox;
    private JLabel Oldver;
    private JLabel New;
    private JPanel ExsternalModificaBox;

    private Color semiBack = new Color(199, 111, 91);

    public ComparazioneFrame(String Nome, MainJFrame frame, Controller controller, NotificheFrame PanelloNotifiche, int id_operazione, String testo, boolean visionata, boolean modifica, boolean link, String utente) {
        super(Nome);
        ComparazioneFrame f = this; //questo frame
        GuiPresetComponet t = new GuiPresetComponet(frame);
        NameApp.setFont(frame.getFontToolBar());
        NameApp.setForeground(Color.BLACK);
        this.setDefaultCloseOperation(HIDE_ON_CLOSE);
        this.pack();
        this.setVisible(true);
        this.add(MainJPanel);
        this.setSize(1500, 700);
        this.setIconImage(frame.getIconImage());
        this.setResizable(false);
        this.TipoConfronto(modifica, controller, id_operazione, f);
        UtenteRichiesta.setText(utente);
        BoxUtente.setBackground(frame.getColorBack());
        OldLinkPage.setBackground(semiBack);
        OldLinkPage.setBorder(new LineBorder(Color.BLACK, 2));
        NewLinkPage.setBackground(semiBack);
        NewLinkPage.setBorder(new LineBorder(Color.BLACK, 2));
        DivisoreOldBox.setBorder(new LineBorder(Color.BLACK, 1));
        MainJPanel.setBackground(frame.getColorBack());
        t.SetIcon(Icon, new ImageIcon(t.ResizeIcon(65, 65, frame.getIcon())));
        OldTextBox.setBackground(semiBack);
        NewTextBox.setBackground(semiBack);
        DivisoreOldBox.setBackground(semiBack);
        t.LabelSetFontAndColorLower(NewLink);
        t.LabelSetFontAndColorLower(NewLinkPagina);
        t.LabelSetFontAndColorLower(NewPosizione);
        t.LabelSetFontAndColorLower(Modifica);
        DivisoreNewBox.setBorder(new LineBorder(Color.BLACK, 1));
        t.LabelSetFontAndColorLower(OldLink);
        t.LabelSetFontAndColorLower(OldPosizione);
        t.LabelSetFontAndColorLower(OldLinkPagina);
        UtenteRichiesta.setFont(frame.getFontToolBarLower());
        UtenteRichiesta.setForeground(Color.BLACK);
        t.GenericButton(AcceptButton);
        t.GenericButton(RejectButton);
        t.LabelSetFontAndColorLower(New);
        t.LabelSetFontAndColorLower(Oldver);
        ModificaBox.setBorder(new LineBorder(Color.BLACK, 2));

        AcceptButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                super.mouseEntered(e);
                AcceptButton.setBackground(semiBack);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                super.mouseExited(e);
                AcceptButton.setBackground(frame.getColorToolBar());
            }
        });

        RejectButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                super.mouseEntered(e);
                RejectButton.setBackground(semiBack);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                super.mouseExited(e);
                RejectButton.setBackground(frame.getColorToolBar());
            }
        });

        AcceptButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                f.dispose();
                controller.ModificaProposta(id_operazione, 1);
                PanelloNotifiche.RefreshAndLoad(controller, frame, PanelloNotifiche);

            }
        });

        RejectButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                f.dispose();
                controller.ModificaProposta(id_operazione, 0);
                PanelloNotifiche.RefreshAndLoad(controller, frame, PanelloNotifiche);
                f.dispose();
            }
        });


    }


    public void TipoConfronto(boolean modifica, Controller controller, int id_operazione, ComparazioneFrame f) {

        ArrayList<String[]> Confronti = controller.LoadConfronto(id_operazione); //index[0] contenuto frase sulla wiki index[1] contenuto frase proposta
        if (modifica) {
            //ArrayList<String[]> Confronti = controller.LoadConfronto(id_operazione); //index[0] contenuto frase sulla wiki index[1] contenuto frase proposta

            OldTextJLabel.setText(Confronti.get(0)[0]);
            NewTextJLabel.setText((Confronti.get(1)[0]));
            OldLink.setText("Link: " + Confronti.get(0)[2]);
            OldPosizione.setText("Posizione: " + Confronti.get(0)[1]);
            OldLinkPagina.setText("Titolo Pagina di Riferimento: " + Confronti.get(1)[4]);
            NewLinkPagina.setText("Titolo Pagina di Riferimento: " + Confronti.get(1)[4]);
            NewLink.setText("Link: " + Confronti.get(1)[2]);
            NewPosizione.setText("Posizione: " + Confronti.get(1)[1]);
            Modifica.setText("Modifica: " + String.valueOf(modifica));

        } else {

            OldTextJLabel.setText(Confronti.get(0)[0]);
            OldLink.setText("Link: " + Confronti.get(0)[2]);
            OldPosizione.setText("Posizione: " + Confronti.get(0)[1]);
            OldLinkPagina.setText("Titolo Pagina di Riferimento: " + Confronti.get(0)[4]);
            Modifica.setText("Modifica: " + String.valueOf(modifica));
            NewLinkPage.setVisible(false);
            New.setVisible(false);
            Modifica.setVisible(false);
            Oldver.setText("Frase Da inserire");
            ModificaBox.setVisible(false);
            ExsternalModificaBox.setVisible(false);
        }

    }

    ;

}
