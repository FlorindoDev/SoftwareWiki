package main.java.GUI;

import main.java.Controller.Controller;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class WikiPage {

    private JPanel MainPanel;
    private final MainJFrame frame;
    private final JPanel OldPanel;
    private JScrollPane ScrollPanel;
    private JPanel ContentContentPane;
    private JPanel InsertPanel;
    private JButton InsertButton;
    private JTextField PositionField;
    private JTextField TextField;
    private JCheckBox LinkBox;
    private JTextField PageLinkRefFiled;
    private JButton ProponiButton;
    private JLabel LabelPaginaRef;
    private JLabel MessageError;
    private JLabel LoginNedded;
    private JPanel ModPanel;
    private JTextField TestoModField;
    private JButton ModificaButton;
    private JLabel MessageErrorMod;
    private JTextField PaginaLinkRefModField;
    private JLabel ModSelectedLabel;
    private JCheckBox LinkModBox;
    private JLabel LabelPaginaRefMod;
    private JButton BackButton;
    private JPanel ToolBar;
    private JPanel IconBox;
    private JLabel Icon;
    private JLabel NameApp;
    private JPanel GoBack;
    private JPanel InternalBox;
    private JLabel IconBack;
    private JPanel DivisioreSx;
    private JPanel DivisioreDx;
    private JButton RefreshButton;
    private JLabel IconRefresh;
    private JPanel RefreshBox;
    private JPanel InserisciBox;
    private JPanel InternalBox2;
    private final Controller controller;
    private final int idPagina;
    HashMap<Integer, ArrayList<String>> Frasi;

    //locali
    int last_key = -1;


    public WikiPage(MainJFrame frame, JPanel OldPanel, Controller controller, int idPagina) {

        this.controller = controller;
        this.frame = frame;
        this.OldPanel = OldPanel;
        this.idPagina = idPagina;
        DivisioreDx.setVisible(false);
        DivisioreSx.setVisible(false);


        this.InsertPanel.setVisible(false); //dx panel
        this.PageLinkRefFiled.setVisible(false);
        this.LabelPaginaRef.setVisible(false);
        this.MessageError.setVisible(false);
        this.LoginNedded.setVisible(false);

        this.ModPanel.setVisible(false); //sx panel
        this.PaginaLinkRefModField.setVisible(false);
        this.LabelPaginaRefMod.setVisible(false);
        MainPanel.setBackground(frame.getColorBack());

        GuiPresetComponet t = new GuiPresetComponet(frame);
        t.ToolBarButton(BackButton);

        t.SetIcon(Icon, new ImageIcon(t.ResizeIcon(65, 65, frame.getIcon())));
        t.SetIcon(IconBack, new ImageIcon(t.ResizeIcon(20, 20, new ImageIcon("src\\main\\resources\\back.png"))));
        t.LabelSetFontAndColorUpper(NameApp);
        ScrollPanel.setBorder(null);
        t.ToolBarButton(InsertButton);
        t.GenericButton(ModificaButton);
        t.GenericButton(ProponiButton);
        t.ToolBarButton(RefreshButton);
        t.SetIcon(IconRefresh, new ImageIcon(t.ResizeIcon(30, 30, new ImageIcon("src/main/resources/refresh.png"))));


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
                //Contenuto refresh
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
                //Contenuto refresh
                controller.removePage(idPagina);
                frame.SetNewPanel(new WikiPage(frame, OldPanel, controller, idPagina).getPanel(), OldPanel);
            }

        });


        ProponiButton.addMouseMotionListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                super.mouseEntered(e);
                ProponiButton.setBackground(new Color(199, 111, 91));
            }
            @Override
            public void mouseExited(MouseEvent e) {
                super.mouseExited(e);
                ProponiButton.setBackground(frame.getColorToolBar());
            }
        });

        ModificaButton.addMouseMotionListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                super.mouseEntered(e);
                ModificaButton.setBackground(new Color(199, 111, 91));
            }
            @Override
            public void mouseExited(MouseEvent e) {
                super.mouseExited(e);
                ModificaButton.setBackground(frame.getColorToolBar());
            }
        });

        BackButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                frame.SetNewPanel(OldPanel, MainPanel);
            }
        });

        BackButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                super.mouseEntered(e);
                BackButton.setBackground(new Color(199, 111, 91));
                InternalBox2.setBackground(new Color(199, 111, 91));

            }

            @Override
            public void mouseExited(MouseEvent e) {
                super.mouseExited(e);
                BackButton.setBackground(frame.getColorToolBar());
                InternalBox2.setBackground(frame.getColorToolBar());

            }
        });


        InternalBox2.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                super.mouseEntered(e);
                InternalBox2.setBackground(new Color(199, 111, 91));
                BackButton.setBackground(new Color(199, 111, 91));

            }

            @Override
            public void mouseExited(MouseEvent e) {
                super.mouseExited(e);
                InternalBox2.setBackground(frame.getColorToolBar());
                BackButton.setBackground(frame.getColorToolBar());
            }

            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                frame.SetNewPanel(OldPanel, MainPanel);
            }
        });

        InsertButton.addMouseMotionListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                super.mouseEntered(e);
                InsertButton.setBackground(new Color(199, 111, 91));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                super.mouseExited(e);
                InsertButton.setBackground(frame.getColorToolBar());
            }
        });

        InsertButton.addActionListener(new ActionListener() {
            boolean clicked = false;

            @Override
            public void actionPerformed(ActionEvent e) {

                if (controller.isUserLogged()) {
                    if (clicked == false) {
                        InsertPanel.setVisible(true);
                        DivisioreDx.setVisible(true);
                        clicked = true;
                    } else {
                        InsertPanel.setVisible(false);
                        DivisioreDx.setVisible(false);
                        clicked = false;
                    }
                } else {
                    LoginNedded.setText("Devi effettuare l'accesso per poter inserire");
                    LoginNedded.setVisible(true);
                }
            }
        });

        LinkBox.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                if (e.getStateChange() == ItemEvent.SELECTED) {
                    PageLinkRefFiled.setVisible(true);
                    LabelPaginaRef.setVisible(true);
                } else if (e.getStateChange() == ItemEvent.DESELECTED) {
                    PageLinkRefFiled.setVisible(false);
                    LabelPaginaRef.setVisible(false);
                }
            }
        });

        ProponiButton.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                MessageError.setText(controller.ProponiInserimento(idPagina, PositionField.getText(), TextField.getText(), LinkBox.isSelected(), PageLinkRefFiled.getText()));
                if (MessageError.getText().equals("<html>Richiesta avvenuta con successo<br></html>"))
                    MessageError.setForeground(Color.green);
                else MessageError.setForeground(Color.red);
                MessageError.setVisible(true);
            }
        });

        LinkModBox.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                if (e.getStateChange() == ItemEvent.SELECTED) {
                    PaginaLinkRefModField.setVisible(true);
                    LabelPaginaRefMod.setVisible(true);
                } else if (e.getStateChange() == ItemEvent.DESELECTED) {
                    PaginaLinkRefModField.setVisible(false);
                    LabelPaginaRefMod.setVisible(false);
                }
            }
        });

        ModificaButton.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                MessageErrorMod.setText(controller.ProponiModifica(idPagina, last_key, TestoModField.getText(), LinkModBox.isSelected(), PaginaLinkRefModField.getText()));
                if (MessageErrorMod.getText().equals("<html>Richiesta avvenuta con successo<br></html>"))
                    MessageErrorMod.setForeground(Color.green);
                else MessageErrorMod.setForeground(Color.red);
                MessageErrorMod.setVisible(true);
            }
        });

    }

    public JPanel getPanel() {
        return MainPanel;
    }

    private void createUIComponents() {


        this.Frasi = this.controller.getWikiPage(idPagina);

        ContentContentPane = new JPanel();

        ContentContentPane.setLayout(new GridBagLayout());
        ScrollPanel = new JScrollPane(ContentContentPane);
        ContentContentPane.setBackground(frame.getColorBack());
        ScrollPanel.setBackground(frame.getColorBack());

        if (Frasi != null) {
            ActionListener listener = new ActionListener() {
                boolean clicked = false;

                @Override
                public void actionPerformed(ActionEvent e) {

                    if (controller.isUserLogged()) {
                        if (clicked == false || last_key != Integer.parseInt(e.getActionCommand())) {
                            ModSelectedLabel.setText("Frase selezionata: " + e.getActionCommand());
                            ModPanel.setVisible(true);
                            DivisioreSx.setVisible(true);
                            clicked = true;
                            last_key = Integer.parseInt(e.getActionCommand());
                        } else {
                            ModPanel.setVisible(false);
                            DivisioreSx.setVisible(false);
                            clicked = false;
                        }
                    } else {
                        LoginNedded.setText("Devi effettuare l'accesso per poter modificare");
                        LoginNedded.setVisible(true);
                    }
                }
            };

            MouseListener listnerlik = new MouseListener() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    Component component = e.getComponent();

                    if (component instanceof JLabel) {
                        JLabel clickedLabel = (JLabel) component;

                        int idLink = Integer.parseInt((String) clickedLabel.getClientProperty("pagina"));
                        //int idLink = clickedLabel.getet;
                        System.out.println("Pagina riferimento: " + idLink);
                        frame.SetNewPanel(new WikiPage(frame, MainPanel, controller, idLink).getPanel(), MainPanel);
                    }
                    //int idLink = controller.getLinkIdPagina(idPagina, );

                }

                @Override
                public void mousePressed(MouseEvent e) {
                    //System.out.println("link cliccato");
                }

                @Override
                public void mouseReleased(MouseEvent e) {
                    //System.out.println("link cliccato");
                }

                @Override
                public void mouseEntered(MouseEvent e) {
                    //System.out.println("link cliccato");
                }

                @Override
                public void mouseExited(MouseEvent e) {
                    //System.out.println("link cliccato");
                }

            };

            int i = 0;
            for (Map.Entry<Integer, ArrayList<String>> entry : Frasi.entrySet()) {

                GridBagConstraints gbc;
                gbc = new GridBagConstraints();
                //gbc.fill = GridBagConstraints.WEST;
                //gbc.gridx = 0;
                //gbc.gridy = i+1;
                gbc.anchor = GridBagConstraints.FIRST_LINE_START;
                gbc.gridx = 0;
                gbc.gridy = i;
                gbc.insets.left = 5;
                gbc.insets.top = 5;
                gbc.weightx = 1.0;
                gbc.weighty = 1.0;
                i++;

                // idPag
                // pos reale
                // frase
                // link
                // id link

                JButton button = new JButton(String.valueOf(entry.getKey()));
                button.addActionListener(listener);

                JLabel label;
                if ( entry.getValue().get(3).equals("1") ) {
                    label = new JLabel(entry.getValue().get(2));
                    label.setForeground(Color.blue);
                    //System.out.println("[] il label "+entry.getValue().get(2)+" sta associando la pagina "+ entry.getValue().get(4));
                    label.putClientProperty("pagina",entry.getValue().get(4));
                    label.addMouseListener(listnerlik);
                } else {
                    label = new JLabel(entry.getValue().get(2));
                }

                JPanel Contenuto = new JPanel();
                Contenuto.setLayout(new FlowLayout());
                Contenuto.setBackground(frame.getColorBack());
                button.setBackground(frame.getColorBack());
                button.setFont(frame.getFontToolBar());
                button.setBackground(frame.getColorBack());
                //button.setBorder(new LineBorder(Color.BLACK));
                button.setForeground(Color.BLACK);

                Contenuto.add(button);
                Contenuto.add(label);
                ContentContentPane.add(Contenuto,gbc);

            }
        } else {
            JLabel label = new JLabel("Nessuna pagina trovata");
            label.setForeground(Color.red);

            ContentContentPane.add(label);
        }
    }


}