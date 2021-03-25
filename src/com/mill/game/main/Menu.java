package com.mill.game.main;

import com.mill.game.main.ai.AlphaBeta;
import com.mill.game.main.ai.Minimax;
import com.mill.game.main.helpers.Helpers;
import com.mill.game.main.enums.COLOR;
import com.mill.game.main.enums.ID;
import com.mill.game.main.enums.STATE;
import com.mill.game.main.models.Stone;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class Menu extends MouseAdapter {

    private Game game;
    private Handler handler;

    private COLOR color_one = COLOR.White;
    private COLOR color_two = COLOR.Black;
    private ID ai = ID.Minimax;
    private final Font fnt = new Font("arial", 1, 50);
    private final Font fnt2 = new Font("arial", 1, 30);
    private final Font fnt3 = new Font("arial", 1, 15);

    public Menu(Game game, Handler handler){
        this.game = game;
        this.handler = handler;
    }

    /**
     * Orchestrates program flow after mouse click happened.
     * @param e Mouse pressed event, contains mouse's x,y
     */
    public void mousePressed(MouseEvent e){
        int mx = e.getX();
        int my = e.getY();

        if (game.gameState == STATE.Play) {
            //change color button
            if (Helpers.mouseOver(mx,my,150, 100, 120, 40)){
                if (color_one == COLOR.White){
                    color_one = COLOR.Black;
                    color_two = COLOR.White;
                }
                else if (color_one == COLOR.Black){
                    color_one = COLOR.White;
                    color_two = COLOR.Black;
                }
            }
            //change ai player
            if (Helpers.mouseOver(mx,my,450, 100, 170, 40)){
                if (ai == ID.Minimax){
                    ai = ID.AlphaBeta;
                }
                else if (ai == ID.AlphaBeta){
                    ai = ID.Minimax;
                }
            }
            //player vs player
            if (Helpers.mouseOver(mx, my, 250, 250, 265, 64)) {
                initiateGameBetweenPlayers();
            }
            //player vs ai
            if (Helpers.mouseOver(mx, my, 250, 350, 265, 64)) {
                initiateGameWithAI();
            }
            //back button for play
            if (Helpers.mouseOver(mx, my, 295, 520, 150, 54)) {
                game.gameState = STATE.Menu;
            }
        }
        //main menu
        else if (game.gameState == STATE.Menu) {
            //play button
            if (Helpers.mouseOver(mx, my, 270, 200, 200, 64)) {
                game.gameState = STATE.Play;
            }

            //instructions button
            if (Helpers.mouseOver(mx, my, 270, 300, 200, 64)) {
                game.gameState = STATE.Instructions;
            }

            //quit button
            if (Helpers.mouseOver(mx, my, 270, 400, 200, 64)) {
                System.exit(1);
            }
        }
        //back button for instructions
        else if (game.gameState == STATE.Instructions) {
            if (Helpers.mouseOver(mx, my, 295, 520, 150, 54)) {
                game.gameState = STATE.Menu;
            }
        }
    }

    public void mouseReleased(MouseEvent e){

    }

    /**
     * Main render method, renders menu UI.
     * @param g application's graphics
     */
    public void render(Graphics g){
        if (game.gameState == STATE.Menu) {
            renderMainMenu(g);
        }
        else if (game.gameState == STATE.Instructions){
            renderInstructions(g);
        }
        else if (game.gameState == STATE.Play){
            renderGameSettings(g);
        }
    }

    /**
     * Renders menu button for game screen.
     * @param g application's graphics
     */
    public void menuButtonRender(Graphics g){
        g.setFont(fnt3);
        g.setColor(Color.white);
        g.fillRect(355, 5 , 70, 20);
        g.setColor(Color.black);
        g.drawRect(355, 5 , 70, 20);
        g.drawString("Menu", 368, 20);
    }

    /**
     * Renders main menu screen.
     * @param g application's graphics
     */
    private void renderMainMenu(Graphics g){
        g.setFont(fnt);
        g.setColor(Color.white);
        g.drawString("The Mill Game", 200, 100);

        g.setFont(fnt2);
        g.drawRect(270, 200, 200, 64);
        g.drawString("Play", 335, 240);
        g.drawRect(270, 300, 200, 64);
        g.drawString("Instructions", 285, 340);
        g.drawRect(270, 400, 200, 64);
        g.drawString("Quit", 335, 440);
    }

    /**
     * Renders instructions screen.
     * @param g application's graphics
     */
    private void renderInstructions(Graphics g){
        g.setFont(fnt);
        g.setColor(Color.white);
        g.drawString("Instructions", 230, 100);

        g.setFont(fnt3);
        g.drawString("Players try to form mills, three of their own men lined horizontally or vertically,", 50, 200);
        g.drawString("allowing a player to remove an opponent's man from the game.",50,225);
        g.drawString("A player wins by reducing the opponent to two pieces.", 50, 250);
        g.drawString("The game proceeds in three phases according to rules:", 50, 300);
        g.drawString("1. Placing men on vacant points", 60, 325);
        g.drawString("2. Moving men to adjacent points", 60, 350);
        g.drawString("3. Moving men to any vacant point when the player has been reduced to three men", 60, 375);

        g.setFont(fnt2);
        g.drawRect(295, 520, 150, 54);
        g.fillRect(295, 520, 150, 54);
        g.setColor(Color.black);
        g.drawString("Back", 335, 555);
    }

    /**
     * Renders game settings screen.
     * @param g application's graphics
     */
    private void renderGameSettings(Graphics g){
        g.setFont(fnt2);
        g.setColor(Color.white);

        //game options
        g.drawString("Color: ", 50, 130);
        g.drawRect(150, 100, 120, 40);
        if (color_one == COLOR.White){
            g.fillRect(150, 100, 120, 40);
            g.setColor(Color.black);
            g.drawString("White", 170, 130);
            g.setColor(Color.white);
        }else if (color_one == COLOR.Black){
            g.setColor(Color.black);
            g.fillRect(151, 101, 119, 39);
            g.setColor(Color.white);
            g.drawString("Black", 170, 130);
        }

        g.drawString("AI: ", 400, 130);
        g.drawRect(450, 100, 170, 40);
        if (ai == ID.Minimax) {
            g.drawString("Minimax", 470, 130);
        }
        else if (ai == ID.AlphaBeta){
            g.drawString("AplhaBeta", 460, 130);
        }

        g.drawRect(250, 250, 265, 64);
        g.drawString("Player vs Player", 265, 290);
        g.drawRect(250, 350, 265, 64);
        g.drawString("Player vs AI", 290, 390);

        //back button
        g.drawRect(295, 520, 150, 54);
        g.fillRect(295, 520, 150, 54);
        g.setColor(Color.black);
        g.drawString("Back", 335, 555);
    }

    /**
     * Creates new game play between two users. Adds stone objects to the game.
     */
    private void initiateGameBetweenPlayers(){
        game.gameState = STATE.Game;
        game.removeMouseListener(game.gamePlay);
        for (int i = 0; i < 9; i++) {
            handler.addObject(new Stone(60, 150 + i * 35, ID.Player1, color_one));
            handler.addObject(new Stone(720, 150 + i * 35, ID.Player2, color_two));
        }
        game.gamePlay = new GamePlay(game, handler);
        game.addMouseListener(game.gamePlay);
    }

    /**
     * Creates new game play against AI opponent. Adds stone objects to the game.
     */
    private void initiateGameWithAI(){
        game.gameState = STATE.Game;
        game.removeMouseListener(game.gamePlay);
        for (int i = 0; i < 9; i++) {
            handler.addObject(new Stone(60, 150 + i * 35, ID.Player1, color_one));
            handler.addObject(new Stone(720, 150 + i * 35, ai, color_two));
        }
        if (ai == ID.Minimax) {
            game.gamePlay = new AiGamePlay(game, handler, new Minimax(new StoneCountEvaluation(), handler, game.board));
        }else if (ai == ID.AlphaBeta){
            game.gamePlay = new AiGamePlay(game, handler, new AlphaBeta(new StoneCountEvaluation(), handler, game.board));
        }
        game.addMouseListener(game.gamePlay);
    }

}
