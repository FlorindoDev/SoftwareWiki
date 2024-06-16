package main.java.GUI;

import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;

public class GuiPresetComponet {

    private MainJFrame frame;
    public GuiPresetComponet(MainJFrame frame){
        this.frame = frame;
    }

    public void ToolBarButton(JButton Button){
        Button.setFont(frame.getFontToolBar());

        Button.setBackground(frame.getColorToolBar());

        Button.setBorderPainted(false);

        Button.setForeground(Color.BLACK);

    }

    public Image ResizeIcon(int W, int H, ImageIcon i){
        Image ResizeImage = i.getImage().getScaledInstance(W,H, java.awt.Image.SCALE_SMOOTH);
        return ResizeImage;
    }

    public void SetIcon(JLabel j, ImageIcon i){
        j.setIcon(i);
    }


    public void GenericButton(JButton Button){
        Button.setFont(frame.getFontToolBar());

        Button.setBackground(frame.getColorToolBar());

        Button.setForeground(Color.BLACK);

        Button.setBorder(new LineBorder(Color.BLACK, 2));

    }

    public void LabelSetTextBlack(JLabel j) {
        j.setForeground(Color.BLACK);

    }

    public void LabelSetFont(JLabel j) {
        j.setFont(frame.getFontToolBar());

    }

    public void LabelSetFontAndColorLower(JLabel j){
        j.setForeground(Color.BLACK);
        j.setFont(frame.getFontToolBarLower());
    }

    public void LabelSetFontAndColorUpper(JLabel j){
        j.setForeground(Color.BLACK);
        j.setFont(frame.getFontToolBar());
    }

}
