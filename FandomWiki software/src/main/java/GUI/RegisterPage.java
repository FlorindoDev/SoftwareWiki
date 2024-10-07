package main.java.GUI;

import main.java.Controller.Controller;

import javax.swing.*;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class RegisterPage {

    private JPanel MainPanel;
    private JButton BackButton;
    private JTextField EmailField;
    private JPasswordField passwordField;
    private JComboBox GenereBox;
    private JTextField NomeField;
    private JTextField CognomeField;
    private JButton RegisterButton;
    private JLabel MessageError;
    private JPanel ToolBar;
    private JPanel IconBox;
    private JLabel Icon;
    private JLabel NameApp;
    private JPanel GoBack;
    private JPanel InternalBox;
    private JLabel IconBack;
    private JLabel Logo;
    private JPanel Divisore;
    private JPanel Box;
    private JLabel TextNome;
    private JLabel TextCognome;
    private JLabel TextEmail;
    private JLabel TextPassword;
    private JLabel TextGenere;
    private JPanel BoxNome;
    private JPanel BoxCognome;
    private JPanel BoxEmail;
    private JPanel BoxPassword;
    private JLabel IconName;
    private JLabel IconCognome;
    private JLabel IconEmail;
    private JLabel IconPassword;
    private boolean bool = true;

    public RegisterPage(MainJFrame frame, JPanel OldPanel, Controller controller) {

        GenereBox.addItem("M");
        GenereBox.addItem("F");
        GenereBox.setFont(frame.getFontToolBar());
        GuiPresetComponet t = new GuiPresetComponet(frame);

        t.LabelSetFontAndColorUpper(TextGenere);
        t.ToolBarButton(BackButton);
        t.SetIcon(Icon, new ImageIcon(t.ResizeIcon(65, 65, frame.getIcon())));
        t.SetIcon(IconBack, new ImageIcon(t.ResizeIcon(20, 20, new ImageIcon("src\\main\\resources\\back.png"))));
        t.SetIcon(Logo, new ImageIcon(t.ResizeIcon(100, 100, frame.getIcon())));
        t.GenericButton(RegisterButton);
        t.LabelSetFontAndColorUpper(NameApp);
        t.LabelSetFontAndColorUpper(TextNome);
        t.LabelSetFontAndColorUpper(TextCognome);
        t.LabelSetFontAndColorUpper(TextEmail);
        t.LabelSetFontAndColorUpper(TextPassword);
        BoxNome.setBorder(new LineBorder(Color.BLACK, 2));
        BoxCognome.setBorder(new LineBorder(Color.BLACK, 2));
        BoxEmail.setBorder(new LineBorder(Color.BLACK, 2));
        BoxPassword.setBorder(new LineBorder(Color.BLACK, 2));
        MessageError.setForeground(Color.RED);
        passwordField.setBackground(new Color(199, 111, 91));
        passwordField.setForeground(Color.BLACK);
        passwordField.setFont(frame.getFontToolBar());
        passwordField.setBorder(null);
        EmailField.setFont(frame.getFontToolBar());
        EmailField.setFont(frame.getFontToolBar());
        EmailField.setBorder(null);
        EmailField.setForeground(Color.BLACK);
        EmailField.setBackground(new Color(199, 111, 91));
        MainPanel.setBackground(frame.getColorBack());
        t.SetIcon(IconEmail, new ImageIcon(t.ResizeIcon(50, 50, new ImageIcon("src/main/resources/email2.png"))));
        t.SetIcon(IconName, new ImageIcon(t.ResizeIcon(50, 50, new ImageIcon("src/main/resources/id-card.png"))));
        t.SetIcon(IconCognome, new ImageIcon(t.ResizeIcon(50, 50, new ImageIcon("src/main/resources/id-card.png"))));
        Image lockchiuso = t.ResizeIcon(50, 50, new ImageIcon("src\\main\\resources\\padlock2.png"));
        Image lockaperto = t.ResizeIcon(50, 50, new ImageIcon("src\\main\\resources\\open-padlock2.png"));
        t.SetIcon(IconPassword, new ImageIcon(lockchiuso));
        BoxEmail.setBackground(new Color(199, 111, 91));
        BoxPassword.setBackground(new Color(199, 111, 91));
        BoxNome.setBackground(new Color(199, 111, 91));
        NomeField.setFont(frame.getFontToolBar());
        CognomeField.setBorder(null);
        CognomeField.setBackground(new Color(199, 111, 91));
        NomeField.setBackground(new Color(199, 111, 91));
        BoxCognome.setBackground(new Color(199, 111, 91));
        CognomeField.setFont(frame.getFontToolBar());
        NomeField.setBorder(null);
        CognomeField.setForeground(Color.BLACK);
        NomeField.setForeground(Color.BLACK);

        BackButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                frame.SetNewPanel(OldPanel, MainPanel);
                frame.Resize(1400, 700);
            }
        });

        RegisterButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String Message = controller.register(NomeField.getText(), CognomeField.getText(), GenereBox.getSelectedItem(), EmailField.getText(), passwordField.getText());
                if (!Message.equals("<html></html>")) {
                    MessageError.setText(Message);
                } else {
                    frame.SetNewPanel(OldPanel, MainPanel);
                    frame.Resize(1400, 700);
                }

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
                InternalBox.setBackground(new Color(199, 111, 91));
                BackButton.setBackground(new Color(199, 111, 91));

            }

            @Override
            public void mouseExited(MouseEvent e) {
                super.mouseExited(e);
                InternalBox.setBackground(frame.getColorToolBar());
                BackButton.setBackground(frame.getColorToolBar());
            }

            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                frame.SetNewPanel(OldPanel, MainPanel);
                frame.Resize(1400, 700);
            }
        });

        IconPassword.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                super.mouseEntered(e);
                if (bool) {
                    t.SetIcon(IconPassword, new ImageIcon(lockaperto));
                } else {
                    t.SetIcon(IconPassword, new ImageIcon(lockchiuso));
                }
            }

            @Override
            public void mouseExited(MouseEvent e) {
                super.mouseExited(e);
                if (bool) {
                    t.SetIcon(IconPassword, new ImageIcon(lockchiuso));
                } else {
                    t.SetIcon(IconPassword, new ImageIcon(lockaperto));
                }
            }

            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                if (bool) {
                    bool = false;
                    passwordField.setEchoChar((char) 0);
                } else {
                    bool = true;
                    passwordField.setEchoChar('•');
                }

            }
        });

    }

    public JPanel getPanel() {
        return MainPanel;
    }

}
