package main.java.GUI;

import main.java.Controller.Controller;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

public class SearchPage {
    private JPanel MainPanel;
    private final MainJFrame frame;
    private final JPanel OldPanel;
    private JScrollPane ScrollPanel;
    private JPanel ContentContentPane;
    private JPanel ToolBar;
    private JPanel IconBox;
    private JLabel Icon;
    private JLabel NameApp;
    private JPanel GoBack;
    private JPanel InternalBox;
    private JLabel IconBack;
    private JButton Backbutton;
    private final Controller controller;
    private final String ricerca;
    private ArrayList<ArrayList<String>> DataPages;
    private ActionListener listener;

    public SearchPage(MainJFrame frame, JPanel OldPanel, Controller controller, String ricerca) {

        this.controller = controller;
        this.ricerca = ricerca;
        this.frame = frame;
        this.OldPanel = OldPanel;
        ScrollPanel.setBorder(null);

        GuiPresetComponet t = new GuiPresetComponet(frame);

        t.ToolBarButton(Backbutton);

        ToolBar.setBackground(frame.getColorToolBar());

        MainPanel.setBackground(frame.getColorBack());


        t.SetIcon(Icon, new ImageIcon(t.ResizeIcon(65, 65, frame.getIcon())));

        t.SetIcon(IconBack, new ImageIcon(t.ResizeIcon(20, 20, new ImageIcon("src\\main\\resources\\back.png"))));

        NameApp.setFont(frame.getFontToolBar());

        NameApp.setForeground(Color.BLACK);

        this.DataPages = controller.searchPages(ricerca);


        if (DataPages != null) {


            int i = 0;
            for (ArrayList<String> innerList : DataPages) {
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

                ContentContentPane.add(new SearchPanelPage(frame, controller, MainPanel, innerList.get(0), innerList.get(3), innerList.get(1), innerList.get(2)), gbc);
                i++;
            }
        } else {
            JLabel label = new JLabel("Nessuna pagina trovata");

            ContentContentPane.add(label);
        }

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

        Backbutton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                frame.SetNewPanel(OldPanel, MainPanel);
                frame.Resize(1400, 700);
            }
        });


    }

    public JPanel getPanel() {
        return MainPanel;
    }


}
