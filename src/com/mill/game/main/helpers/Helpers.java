package com.mill.game.main.helpers;

import java.awt.*;

public final class Helpers {
    /**
     * Draws a centered circle around the middle point.
     * @param g application's graphics
     * @param x middle point's x coordinate
     * @param y middle point's y coordinate
     * @param radius radius of the circle
     */
    public static void drawCenteredCircle(Graphics g, int x, int y, int radius){
        int diameter = radius * 2;
        g.fillOval(x - radius, y - radius, diameter, diameter);
    }

    /**
     * Paints 3 different strokes around a circle to indicate focus.
     * The widest stroke is the most transparent, so this achieves a "glow" effect.
     * @param g application's graphics
     * @param biggestStroke the widest stroke to use.
     * @param x middle point's x coordinate
     * @param y middle point's y coordinate
     * @param radius radius of the circle
     */
    public static void paintOvalFocus(Graphics2D g, int biggestStroke, int x, int y, int radius) {
        int diameter = radius * 2;
        Color focusColor = new Color(255, 255, 0);
        Color[] focusArray = new Color[] {
                new Color(focusColor.getRed(), focusColor.getGreen(), focusColor.getBlue(),255),
                new Color(focusColor.getRed(), focusColor.getGreen(), focusColor.getBlue(),170),
                new Color(focusColor.getRed(), focusColor.getGreen(), focusColor.getBlue(),110)
        };
        g.setStroke(new BasicStroke(biggestStroke));
        g.setColor(focusArray[2]);
        g.drawOval(x - radius, y - radius, diameter, diameter);
        g.setStroke(new BasicStroke(biggestStroke-1));
        g.setColor(focusArray[1]);
        g.drawOval(x - radius, y - radius, diameter, diameter);
        g.setStroke(new BasicStroke(biggestStroke-2));
        g.setColor(focusArray[0]);
        g.drawOval(x - radius, y - radius, diameter, diameter);
        g.setStroke(new BasicStroke(1));
    }

    /**
     * Tests whether a mouse click happened inside a shape determined by x,y coordinates, width and height.
     * @param mx x coordinate of the mouse click
     * @param my y coordinate of the mouse click
     * @param x x coordinate of the shape
     * @param y y coordinate of the shape
     * @param width width of the shape
     * @param height height of the shape
     * @return true if the mouse clicked inside, false otherwise
     */
    public static boolean mouseOver(int mx, int my, int x, int y, int width, int height){
        if (mx > x && mx < x + width){
            if (my > y && my < y + height){
                return true;
            }
            else {
                return false;
            }
        }else {
            return false;
        }
    }

}
