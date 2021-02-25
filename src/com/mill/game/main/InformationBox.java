package com.mill.game.main;

import java.awt.*;

public class InformationBox {

    public String message;
    public int x, y;

    public InformationBox(String message, int x, int y){
        this.message = message;
        this.x = x;
        this.y = y;
    }

    public void render(Graphics g){
        g.setColor(Color.white);
        g.fillRect(x - 20, y - 25, 350, 40);
        g.setColor(Color.black);
        g.drawString(message, x, y);
    }
}
