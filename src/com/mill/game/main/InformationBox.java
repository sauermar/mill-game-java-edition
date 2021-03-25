package com.mill.game.main;

import java.awt.*;

public class InformationBox {

    private String message;
    private int x, y;

    public final String INVALID_MOVE = "Stone can move in row or column only";
    public final String INVALID_SELECTION = "Select different stone or an empty spot";
    public final String INVALID_CHOICE = "Please select opponent's stone";

    public InformationBox(String message, int x, int y){
        this.message = message;
        this.x = x;
        this.y = y;
    }

    public void render(Graphics g){
        g.setColor(Color.white);
        g.fillRect(x - 20, y - 25, 380, 40);
        g.setColor(Color.black);
        g.drawString(message, x, y);
    }

    public void changeMessage(String message){
        this.message = message;
    }

    public String millCreated(String color){
        return String.format("%s player created mill", color);
    }

    public String secondPhase(String color){
        return String.format("Select %s stone for moving", color);
    }

    public String gameWon(String color){
        return String.format("%s player won the game", color);
    }

    public String firstPhase(String color){
        return String.format("Place the %s stone", color);
    }
}
