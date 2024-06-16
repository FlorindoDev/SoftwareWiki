package main.java.GUI;

import main.java.Controller.Controller;

import javax.swing.*;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.*;

public class CreaPagina {
    private JButton BackButton;
    private JTextField titoloTextField;
    private JTextField fraseTextField;
    private JButton CreateButton;
    private JPanel MainPanel;
    private JCheckBox linkCheckBox;
    private JTextField PaginaLinkRef;
    private JLabel LabelPaginaLinkRef;
    private JPanel ToolBar;
    private JPanel IconBox;
    private JLabel Icon;
    private JLabel NameApp;
    private JPanel GoBack;
    private JPanel InternalBox;
    private JLabel IconBack;
    private JLabel Logo;
    private JPanel MainBox;
    private JLabel TitoloLabel;
    private JLabel FraseLabel;
    private JPanel Divisore;
    private JLabel Message;

    public CreaPagina(MainJFrame frame, JPanel OldPanel, Controller controller) {

        GuiPresetComponet t = new GuiPresetComponet(frame);
        PaginaLinkRef.setVisible(false);
        LabelPaginaLinkRef.setVisible(false);
        Divisore.setVisible(false);
        NameApp.setFont(frame.getFontToolBar());
        NameApp.setForeground(Color.BLACK);
        titoloTextField.setFont(frame.getFontToolBar());
        titoloTextField.setForeground(Color.BLACK);
        titoloTextField.setBackground(new Color(199, 111, 91));
        titoloTextField.setBorder(new LineBorder(Color.BLACK, 2));

        fraseTextField.setBorder(new LineBorder(Color.BLACK, 2));
        fraseTextField.setFont(frame.getFontToolBar());
        fraseTextField.setForeground(Color.BLACK);
        fraseTextField.setBackground(new Color(199, 111, 91));
        linkCheckBox.setFont(frame.getFontToolBar());
        linkCheckBox.setForeground(Color.BLACK);

        PaginaLinkRef.setBorder(new LineBorder(Color.BLACK, 2));
        PaginaLinkRef.setFont(frame.getFontToolBar());
        PaginaLinkRef.setForeground(Color.BLACK);
        PaginaLinkRef.setBackground(new Color(199, 111, 91));

        MainPanel.setBackground(frame.getColorBack());
        t.LabelSetFontAndColorUpper(TitoloLabel);
        t.LabelSetFontAndColorUpper(FraseLabel);
        t.LabelSetFontAndColorUpper(LabelPaginaLinkRef);
        t.GenericButton(CreateButton);


        t.ToolBarButton(BackButton);
        t.SetIcon(Icon, new ImageIcon(t.ResizeIcon(65, 65, frame.getIcon())));
        t.SetIcon(IconBack, new ImageIcon(t.ResizeIcon(20, 20, new ImageIcon("src\\main\\resources\\back.png"))));
        t.SetIcon(Logo, new ImageIcon(t.ResizeIcon(120, 120, frame.getIcon())));
        Message.setVisible(false);

        BackButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                frame.SetNewPanel(OldPanel, MainPanel);
                frame.Resize(1400, 700);
            }
        });

        CreateButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Message.setText(controller.creaPagina(titoloTextField.getText(), fraseTextField.getText(), linkCheckBox.isSelected(), PaginaLinkRef.getText()));
                System.out.println(Message.getText());
                if (Message.getText().equals("<html>Pagina creata con successo<br></html>")) {
                    Message.setForeground(Color.green);
                    frame.remove(MainPanel);
                    frame.remove(OldPanel);
                    controller.Login(controller.getUtenteLoggato().getEmail(), controller.getUtenteLoggato().getPassword());
                    frame.SetNewPanel(new HomeAutore(frame, controller, OldPanel).getPanel(), MainPanel);
                    frame.Resize(1400, 700);



                }else{ Message.setForeground(Color.red);}
                Message.setVisible(true);
            }
        });

        linkCheckBox.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                if (e.getStateChange() == ItemEvent.SELECTED) {
                    PaginaLinkRef.setVisible(true);
                    LabelPaginaLinkRef.setVisible(true);
                    Divisore.setVisible(true);
                } else if (e.getStateChange() == ItemEvent.DESELECTED) {
                    PaginaLinkRef.setVisible(false);
                    LabelPaginaLinkRef.setVisible(false);
                    Divisore.setVisible(false);
                }
            }
        });

        CreateButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                super.mouseEntered(e);
                CreateButton.setBackground(new Color(199, 111, 91));

            }

            @Override
            public void mouseExited(MouseEvent e) {
                super.mouseExited(e);
                CreateButton.setBackground(frame.getColorToolBar());
            }
        });

        BackButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                super.mouseEntered(e);
                BackButton.setBackground(new Color(199, 111, 91));
                InternalBox.setBackground(new Color(199, 111, 91));

            }

            @Override
            public void mouseExited(MouseEvent e) {
                super.mouseExited(e);
                BackButton.setBackground(frame.getColorToolBar());
                InternalBox.setBackground(frame.getColorToolBar());

            }
        });
        InternalBox.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                super.mouseEntered(e);
                BackButton.setBackground(new Color(199, 111, 91));
                InternalBox.setBackground(new Color(199, 111, 91));

            }

            @Override
            public void mouseExited(MouseEvent e) {
                super.mouseExited(e);
                BackButton.setBackground(frame.getColorToolBar());
                InternalBox.setBackground(frame.getColorToolBar());

            }

            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                frame.SetNewPanel(OldPanel, MainPanel);
                frame.Resize(1400, 700);
            }
        });
    }

    public JPanel getPanel() {
        return MainPanel;
    }

}
