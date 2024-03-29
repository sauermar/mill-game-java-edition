package com.mill.game.main;

import javax.swing.*;
import java.awt.Canvas;
import java.awt.Dimension;

public class Window extends Canvas{

    /**
     * Creates a JFrame application window with fixed size and title.
     * @param width width of the window
     * @param height height of the window
     * @param title title of the window
     * @param game application to be run in the window
     */
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
