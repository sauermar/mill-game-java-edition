package com.mill.game.main.models;

import com.mill.game.main.helpers.Helpers;
import com.mill.game.main.enums.COLOR;
import com.mill.game.main.enums.ID;

import java.awt.*;

public class Player extends GameObject {

    private static final int radius = 15;

    public Player(int x, int y, ID id, COLOR color) {
        super(x, y, id, color);
    }

    public void tick() {
        x += velX;
        y += velY;
    }

    public void render(Graphics g) {
        if (color == COLOR.White) {
            g.setColor(Color.white);
        }else{
            g.setColor(java.awt.Color.black);
        }
        Helpers.drawCenteredCircle(g, x, y, radius);
    }
}
