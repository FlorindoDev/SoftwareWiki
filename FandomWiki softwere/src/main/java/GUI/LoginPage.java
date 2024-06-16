package main.java.GUI;

import main.java.Controller.Controller;

import javax.swing.*;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.*;

public class LoginPage {

    private JPanel MainPanel;
    private JButton Backbutton;
    private JPanel ToolBar;
    private JPanel LoginBox;
    private JTextField emailField;
    private JPasswordField passwordField;
    private JLabel Logo;
    private JButton loginButton;
    private JLabel errorMessage;
    private JPanel GoBack;
    private JPanel IconBox;
    private JLabel Icon;
    private JLabel NameApp;
    private JLabel IconBack;
    private JPanel DivisioreToolBar;
    private JPanel InternalBox;
    private JPanel LogoBox;
    private JPanel DivisoreImgEmail;
    private JPanel EmailBox;
    private JPanel PassBox;
    private JPanel LoginButtonBox;
    private JLabel IconLock;
    private JLabel IconName;
    private JLabel TextEmail;
    private JLabel TextPass;
    private JLabel RegisterButtonLink;
    private JPanel EmailLine;
    private boolean bool = true;

    public LoginPage(MainJFrame frame, JPanel OldPanel, Controller controller) {

        GuiPresetComponet t = new GuiPresetComponet(frame);

        t.ToolBarButton(Backbutton);

        ToolBar.setBackground(frame.getColorToolBar());

        MainPanel.setBackground(frame.getColorBack());

        t.SetIcon(Logo, new ImageIcon(t.ResizeIcon(150, 150, frame.getIcon())));

        t.SetIcon(Icon, new ImageIcon(t.ResizeIcon(65, 65, frame.getIcon())));

        t.SetIcon(IconBack, new ImageIcon(t.ResizeIcon(20, 20, new ImageIcon("src\\main\\resources\\back.png"))));

        Image lockchiuso = t.ResizeIcon(50, 50, new ImageIcon("src\\main\\resources\\padlock2.png"));
        Image lockaperto = t.ResizeIcon(50, 50, new ImageIcon("src\\main\\resources\\open-padlock2.png"));

        t.SetIcon(IconLock, new ImageIcon(lockchiuso));

        errorMessage.setVisible(false);

        LoginBox.setBackground(frame.getColorBack());

        DivisioreToolBar.setBackground(frame.getColorBack());

        NameApp.setFont(frame.getFontToolBar());

        NameApp.setForeground(Color.BLACK);

        LogoBox.setBackground(frame.getColorBack());

        DivisoreImgEmail.setBackground(frame.getColorBack());

        //emailField.setPreferredSize(new Dimension(1000,200));

        LoginButtonBox.setBackground(frame.getBackground());
        EmailBox.setBackground(frame.getColorBack());
        PassBox.setBackground(new Color(199, 111, 91));
        passwordField.setBackground(new Color(199, 111, 91));
        passwordField.setForeground(Color.BLACK);
        passwordField.setFont(frame.getFontToolBar());
        emailField.setFont(frame.getFontToolBar());
        emailField.setForeground(Color.BLACK);
        emailField.setBackground(new Color(199, 111, 91));
        EmailBox.setBackground(new Color(199, 111, 91));
        emailField.setFont(frame.getFontToolBar());
        EmailBox.setBorder(new LineBorder(Color.BLACK, 2));
        emailField.setBorder(null);
        PassBox.setBorder(new LineBorder(Color.BLACK, 2));
        passwordField.setBorder(null);
        t.SetIcon(IconName, new ImageIcon(t.ResizeIcon(50, 50, new ImageIcon("src/main/resources/email2.png"))));
        t.GenericButton(loginButton);

        TextEmail.setFont(frame.getFontToolBar());
        TextPass.setFont(frame.getFontToolBar());
        TextEmail.setForeground(Color.BLACK);
        TextPass.setForeground(Color.BLACK);
        LoginButtonBox.setBackground(frame.getColorBack());


        Backbutton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                frame.SetNewPanel(OldPanel, MainPanel);
                frame.Resize(1400, 700);
            }
        });

        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                if (controller.Login(emailField.getText(), passwordField.getText())) {
                    if (controller.isAutore()) {
                        frame.SetNewPanel(new HomeAutore(frame, controller, OldPanel).getPanel(), MainPanel);
                        frame.Resize(1400, 700);
                    } else {
                        frame.SetNewPanel(new HomeUtente(frame, controller, OldPanel).getPanel(), MainPanel);
                        frame.Resize(1400, 700);
                    }

                } else {
                    errorMessage.setVisible(true);
                }
            }
        });

        loginButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                super.mouseEntered(e);
                loginButton.setBackground(new Color(199, 111, 91));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                super.mouseExited(e);
                loginButton.setBackground(frame.getColorToolBar());
            }
        });

        Backbutton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                super.mouseEntered(e);
                Backbutton.setBackground(new Color(199, 111, 91));
                InternalBox.setBackground(new Color(199, 111, 91));

            }

            @Override
            public void mouseExited(MouseEvent e) {
                super.mouseExited(e);
                Backbutton.setBackground(frame.getColorToolBar());
                InternalBox.setBackground(frame.getColorToolBar());

            }
        });


        InternalBox.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                super.mouseEntered(e);
                InternalBox.setBackground(new Color(199, 111, 91));
                Backbutton.setBackground(new Color(199, 111, 91));

            }

            @Override
            public void mouseExited(MouseEvent e) {
                super.mouseExited(e);
                InternalBox.setBackground(frame.getColorToolBar());
                Backbutton.setBackground(frame.getColorToolBar());
            }

            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                frame.SetNewPanel(OldPanel, MainPanel);
                frame.Resize(1400, 700);
            }
        });

        IconLock.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                super.mouseEntered(e);
                if (bool) {
                    t.SetIcon(IconLock, new ImageIcon(lockaperto));
                } else {
                    t.SetIcon(IconLock, new ImageIcon(lockchiuso));
                }
            }

            @Override
            public void mouseExited(MouseEvent e) {
                super.mouseExited(e);
                if (bool) {
                    t.SetIcon(IconLock, new ImageIcon(lockchiuso));
                } else {
                    t.SetIcon(IconLock, new ImageIcon(lockaperto));
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

        RegisterButtonLink.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                frame.Resize(700, 950);
                frame.SetNewPanel(new RegisterPage(frame, OldPanel, controller).getPanel(), MainPanel);

            }
        });

    }

    public JPanel getPanel() {
        return MainPanel;
    }

}
