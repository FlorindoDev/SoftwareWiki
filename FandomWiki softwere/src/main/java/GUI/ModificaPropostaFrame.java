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

public class ModificaPropostaFrame extends JFrame {

    private JPanel NoToolBarComponet;
    private JPanel Box;
    private JPanel OldLinkPage;
    private JPanel OldTextPanel;
    private JPanel OldTextBox;
    private JEditorPane OldTextJLabel;
    private JPanel OldLabelBox;
    private JLabel OldLinkPagina;
    private JLabel OldLink;
    private JLabel OldPosizione;
    private JPanel DivisoreOldBox;
    private JPanel ExsternalModificaBox;
    private JPanel ModificaBox;
    private JLabel Modifica;
    private JPanel ButtonBox;
    private JButton RejectButton;
    private JButton AcceptButton;
    private JLabel Oldver;
    private JPanel ToolBar;
    private JPanel BarIcon;
    private JLabel Icon;
    private JLabel NameApp;
    private JPanel BoxUtente;
    private JLabel UtenteRichiesta;
    private JPanel MainJPanel;
    private JLabel TitoloPagina;
    private JLabel Avviso;
    private Color semiBack = new Color(199, 111, 91);

    public ModificaPropostaFrame(String Nome, MainJFrame frame, Controller controller, FrameRichieste FrameModifica, int id_operazione, String testo, boolean visionata, boolean modifica, boolean link, int posizione, String Utente, String Autore, String TitoloLinkPagina, String Titolo) {

        super(Nome);
        ModificaPropostaFrame f = this; //questo frame
        GuiPresetComponet t = new GuiPresetComponet(frame);
        NameApp.setFont(frame.getFontToolBar());
        NameApp.setForeground(Color.BLACK);


        OldTextJLabel.setText(testo);
        OldLink.setText("Link: " + link);
        OldPosizione.setText("Posizione: " + posizione);
        OldLinkPagina.setText("Titolo Pagina di Riferimento: " + TitoloLinkPagina);
        Modifica.setText("Modifica: " + String.valueOf(modifica));
        Modifica.setVisible(true);
        Oldver.setText("Frase Da inserire");
        ModificaBox.setVisible(true);
        //ExsternalModificaBox.setVisible(false);
        this.setDefaultCloseOperation(HIDE_ON_CLOSE);
        this.pack();
        this.setVisible(true);
        this.add(MainJPanel);
        this.setSize(1500, 700);
        this.setIconImage(frame.getIconImage());
        this.setResizable(false);
        Avviso.setVisible(false);
        if (visionata) {
            AcceptButton.setVisible(false);
            OldTextJLabel.setEditable(false);
            Avviso.setVisible(true);
        }
        //this.TipoConfronto(modifica,controller,id_operazione,f);
        UtenteRichiesta.setText("Pagina di:" + Autore);
        TitoloPagina.setText("Titolo Pagina: " + Titolo);
        BoxUtente.setBackground(frame.getColorBack());
        OldLinkPage.setBackground(semiBack);
        OldLinkPage.setBorder(new LineBorder(Color.BLACK, 2));
        DivisoreOldBox.setBorder(new LineBorder(Color.BLACK, 1));
        MainJPanel.setBackground(frame.getColorBack());
        t.SetIcon(Icon, new ImageIcon(t.ResizeIcon(65, 65, frame.getIcon())));
        OldTextBox.setBackground(semiBack);
        DivisoreOldBox.setBackground(semiBack);
        t.LabelSetFontAndColorLower(Modifica);
        t.LabelSetFontAndColorLower(OldLink);
        t.LabelSetFontAndColorLower(OldPosizione);
        t.LabelSetFontAndColorLower(OldLinkPagina);
        t.LabelSetFontAndColorLower(TitoloPagina);
        UtenteRichiesta.setFont(frame.getFontToolBarLower());
        UtenteRichiesta.setForeground(Color.BLACK);
        t.GenericButton(AcceptButton);
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

        AcceptButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                f.dispose();
                FrameModifica.RefreshAndLoad(controller, frame, FrameModifica);
                controller.ModificaPropsostaEffetuata(id_operazione, OldTextJLabel.getText());
            }
        });

    }

}

