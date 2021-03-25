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

    /**
     * Renders information box and it's message.
     * @param g application's graphics
     */
    public void render(Graphics g){
        g.setColor(Color.white);
        g.fillRect(x - 20, y - 25, 380, 40);
        g.setColor(Color.black);
        g.drawString(message, x, y);
    }

    /**
     * Changes displayed message in information box.
     * @param message message to be displayed
     */
    public void changeMessage(String message){
        this.message = message;
    }

    /**
     * Returns message about mill creation.
     * @param color color of the player, who created mill
     * @return message about mill creation
     */
    public String millCreated(String color){
        return String.format("%s player created mill", color);
    }

    /**
     * Returns message about which player's turn it is suitable for second game phase.
     * @param color color of the active player
     * @return message about which player's turn it is
     */
    public String secondPhase(String color){
        return String.format("Select %s stone for moving", color);
    }

    /**
     * Returns message, that one player won the game.
     * @param color color of the winning player
     * @return message, that one player won the game
     */
    public String gameWon(String color){
        return String.format("%s player won the game", color);
    }

    /**
     * Returns message about which player's turn it is suitable for first game phase.
     * @param color color of the active player
     * @return message about which player's turn it is
     */
    public String firstPhase(String color){
        return String.format("Place the %s stone", color);
    }
}
