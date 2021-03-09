package com.mill.game.main;

import com.mill.game.main.ai.AlphaBeta;
import com.mill.game.main.ai.Minimax;
import com.mill.game.main.helpers.Helpers;
import com.mill.game.main.enums.COLOR;
import com.mill.game.main.enums.ID;
import com.mill.game.main.enums.STATE;
import com.mill.game.main.models.Player;

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
                game.gameState = STATE.Game;
                game.removeMouseListener(game.gamePlay);
                for (int i = 0; i < 9; i++) {
                    handler.addObject(new Player(60, 150 + i * 35, ID.Player1, color_one));
                    handler.addObject(new Player(720, 150 + i * 35, ID.Player2, color_two));
                }
                game.gamePlay = new GamePlay(game, handler);
                game.addMouseListener(game.gamePlay);
            }
            //player vs ai
            if (Helpers.mouseOver(mx, my, 250, 350, 265, 64)) {
                game.gameState = STATE.Game;
                game.removeMouseListener(game.gamePlay);
                for (int i = 0; i < 9; i++) {
                    handler.addObject(new Player(60, 150 + i * 35, ID.Player1, color_one));
                    handler.addObject(new Player(720, 150 + i * 35, ai, color_two));
                }
                if (ai == ID.Minimax) {
                    game.gamePlay = new AiGamePlay(game, handler, new Minimax(new StoneCountEvaluation(), handler, game.board));
                }else if (ai == ID.AlphaBeta){
                    game.gamePlay = new AiGamePlay(game, handler, new AlphaBeta(new StoneCountEvaluation(), handler, game.board));
                }
                game.addMouseListener(game.gamePlay);
            }
            //back button for play
            if (Helpers.mouseOver(mx, my, 295, 520, 150, 54)) {
                game.gameState = STATE.Menu;
            }
        }

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

    public void tick(){

    }

    public void render(Graphics g){
        if (game.gameState == STATE.Menu) {
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
        else if (game.gameState == STATE.Instructions){
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
        else if (game.gameState == STATE.Play){
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

            g.drawString("Ai: ", 400, 130);
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
            g.drawString("Player vs Ai", 290, 390);

            //back button
            g.drawRect(295, 520, 150, 54);
            g.fillRect(295, 520, 150, 54);
            g.setColor(Color.black);
            g.drawString("Back", 335, 555);
        }
    }

    public void menuButtonRender(Graphics g){
        g.setFont(fnt3);
        g.setColor(Color.white);
        g.fillRect(600, 5 , 70, 20);
        g.setColor(Color.black);
        g.drawRect(600, 5 , 70, 20);
        g.drawString("Menu", 610, 20);
    }

}
