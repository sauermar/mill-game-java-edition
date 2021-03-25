package com.mill.game.main;

import javax.swing.*;
import java.awt.Canvas;
import java.awt.Dimension;

public class Window extends Canvas{

    public Window(int width, int height, String title, Game game) {
        JFrame frame = new JFrame(title);
        ImageIcon img = new ImageIcon("./images/icon.png");
        frame.setIconImage(img.getImage());

        frame.setPreferredSize(new Dimension(width, height));
        frame.setMaximumSize(new Dimension(width, height));
        frame.setMinimumSize(new Dimension(width, height));

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setResizable(false);
        frame.setLocationRelativeTo(null);
        frame.add(game);
        frame.setVisible(true);
        game.start();
    }
}
