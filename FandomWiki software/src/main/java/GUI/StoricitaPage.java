package main.java.GUI;

import main.java.Controller.Controller;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

public class StoricitaPage {

    private JPanel MainPanel;
    private JScrollPane ScrollPanel;
    private JPanel StoricitaPanel;
    private JPanel ContentScrollPane;
    private JPanel ToolBar;
    private JPanel IconBox;
    private JLabel Icon;
    private JLabel NameApp;
    private JPanel GoBack;
    private JPanel InternalBox;
    private JLabel IconBack;
    private JButton Backbutton;
    private JScrollPane ScrollPagePane;
    private JComboBox DateComboBox;
    private final Controller controller;
    MainJFrame frame;

    //locale
    int last_page;

    public StoricitaPage(MainJFrame frame, JPanel OldPanel, Controller controller){

        this.frame = frame;
        this.controller = controller;

        this.ScrollPagePane.setVisible(false);
        this.StoricitaPanel.setVisible(false);
        this.DateComboBox.setVisible(false);

        GuiPresetComponet t = new GuiPresetComponet(frame);
        NameApp.setFont(frame.getFontToolBar());
        t.SetIcon(Icon, new ImageIcon(t.ResizeIcon(65, 65, frame.getIcon())));

        t.SetIcon(IconBack, new ImageIcon(t.ResizeIcon(20, 20, new ImageIcon("src\\main\\resources\\back.png"))));

        t.ToolBarButton(Backbutton);

        NameApp.setForeground(Color.BLACK);

        ScrollPanel.setBorder(null);

        DateComboBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println("ho selezionato la data: "+DateComboBox.getSelectedItem());

                StoricitaPanel.removeAll();
                ArrayList<ArrayList<String>> frasi = controller.getStoricitaPage(last_page, String.valueOf(DateComboBox.getSelectedItem()) );
                if (frasi != null){
                    for (ArrayList<String> innerArray : frasi){
                        JLabel label = new JLabel(innerArray.get(1)+". "+innerArray.get(0));
                        StoricitaPanel.add(label);
                    }
                }else {
                    JLabel label = new JLabel("Nessuna frase in questa pagina");
                    StoricitaPanel.add(label);
                }

                StoricitaPanel.revalidate();
                StoricitaPanel.repaint();
            }
        });
        Backbutton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                frame.SetNewPanel(OldPanel, MainPanel);
                frame.Resize(1400, 700);
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

    }

    public JPanel getPanel() {
        return MainPanel;
    }

    private void createUIComponents() {
        // TODO: place custom component creation code here

        this.ContentScrollPane = new JPanel();
        ContentScrollPane.setLayout(new BoxLayout(ContentScrollPane, BoxLayout.Y_AXIS));

        ContentScrollPane.setBorder(new EmptyBorder(0,20,0,0));

        ScrollPagePane = new JScrollPane();
        this.StoricitaPanel = new JPanel();
        StoricitaPanel.setLayout(new BoxLayout(StoricitaPanel, BoxLayout.Y_AXIS));
        ScrollPagePane.add(StoricitaPanel);

        StoricitaPanel.setBorder(null);
        ScrollPagePane.setBorder(null);

        ArrayList<ArrayList<String>> pages = controller.getMyPage();

        ActionListener listnerButton = new ActionListener() {

            //id
            //testo

            @Override
            public void actionPerformed(ActionEvent e) {

                StoricitaPanel.removeAll();
                last_page = Integer.parseInt(e.getActionCommand());
                ArrayList<ArrayList<String>> frasi = controller.getStoricitaPage(last_page, "");
                if (frasi != null){
                    for (ArrayList<String> innerArray : frasi){
                        JLabel label = new JLabel(innerArray.get(1)+". "+innerArray.get(0));
                        StoricitaPanel.add(label);
                    }
                }else {
                    JLabel label = new JLabel("Nessuna frase in questa pagina");
                    StoricitaPanel.add(label);
                }

                DateComboBox.removeAllItems();
                ArrayList<String> dateDisponibili = controller.getDateAvailable(Integer.parseInt(e.getActionCommand()));
                for (String Data : dateDisponibili){
                    DateComboBox.addItem(Data);
                }

                ScrollPagePane.setVisible(true);
                StoricitaPanel.setVisible(true);
                DateComboBox.setVisible(true);
                StoricitaPanel.revalidate();
                StoricitaPanel.repaint();

            }
        };

        if(pages!=null){
            for (ArrayList<String> page : pages){

                JButton pageButton = new JButton(page.get(1));
                pageButton.setActionCommand(page.get(0));

                pageButton.setBorder(null);
                pageButton.setFont(frame.getFontToolBarLower());
                pageButton.setBackground(frame.getColorBack());
                pageButton.setForeground(new Color(0, 0, 0));
                pageButton.setBorder(new EmptyBorder(5, 0, 15, 0));

                pageButton.addActionListener(listnerButton);
                pageButton.addMouseMotionListener(new MouseAdapter() {
                    @Override
                    public void mouseEntered(MouseEvent e) {
                        super.mouseEntered(e);
                        pageButton.setBackground(new Color(199, 111, 91));
                    }

                    @Override
                    public void mouseExited(MouseEvent e) {
                        super.mouseExited(e);
                        pageButton.setBackground(frame.getColorToolBar());
                    }

                });

                ContentScrollPane.add(pageButton);

            }
        }

    }

}
