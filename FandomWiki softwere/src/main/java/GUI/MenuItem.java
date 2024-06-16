package main.java.GUI;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;

public class MenuItem extends JMenuItem {

    public MenuItem(MainJFrame frame, String text, ImageIcon icon){
        super(text, icon);
        this.setOpaque(true);
        this.setBackground(frame.getColorToolBar());
        this.setFont(frame.getFontToolBar());
        this.setForeground(Color.BLACK);
        this.setMnemonic(KeyEvent.VK_B);
    }

}
