package TestRun;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;  
import java.awt.image.BufferedImage;
import javax.swing.JOptionPane;


public class SpaceInvaders extends JFrame  {

    public SpaceInvaders() {

        initUI();
    }

    private void initUI() {

        
    }

    public static void main(String[] args) {

        EventQueue.invokeLater(() -> {

            var ex = new SpaceInvaders();
            ex.setVisible(true);
        });
    }
}
