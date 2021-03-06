package com.mill.game.main.helpers;

import java.awt.*;

public final class Helpers {

    public static void drawCenteredCircle(Graphics g, int x, int y, int radius){
        int diameter = radius * 2;
        g.fillOval(x - radius, y - radius, diameter, diameter);
    }

    /** Paints 3 different strokes around a shape to indicate focus.
     * The widest stroke is the most transparent, so this achieves a nice
     * "glow" effect.
     * <P>The catch is that you have to render this underneath the shape,
     * and the shape should be filled completely.
     *
     * @param g the graphics to paint to
     * @param biggestStroke the widest stroke to use.
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
