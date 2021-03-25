package com.mill.game.main;

import com.mill.game.main.common.GamePlayBase;
import com.mill.game.main.enums.STATE;

import java.awt.*;
import java.awt.image.BufferStrategy;

public class Game extends Canvas implements Runnable {
    //16x9 ratio for the game window
    public static final int WIDTH = 780, HEIGHT = 680;
    //main application thread
    private Thread thread;
    private boolean running = false;

    public Board board;
    public InformationBox informationBox;
    public GamePlayBase gamePlay;

    private Handler handler;
    private Menu menu;

    public STATE gameState = STATE.Menu;

    public Game(){
        handler = new Handler();
        board = new Board();
        informationBox = new InformationBox("", 220, 580);
        gamePlay = new GamePlay(this, handler);
        menu = new Menu(this, handler);

        this.addMouseListener(menu);

        new Window(WIDTH, HEIGHT, "Mill game", this);
    }

    /**
     * Starts the main application thread.
     */
    public synchronized void start(){
        thread = new Thread(this);
        thread.start();
        running = true;
    }

    /**
     * Stops the main application thread.
     */
    public synchronized void stop(){
        try{
            thread.join();
            running = false;
        }catch (Exception e){
            e.printStackTrace();
        }

    }

    /**
     * Main application loop. Takes care of rendering and stopping of the application.
     */
    public void run(){
//        long timer = System.currentTimeMillis();
//        int frames = 0;
        while (running){
            if (running){
                render();
            }
//            frames++;

//            if (System.currentTimeMillis() - timer > 1000){
//                timer += 1000;
//                System.out.println("FPS: " + frames);
//                frames = 0;
//            }
        }
        stop();
    }

    /**
     * Application's main method.
     * @param args command line arguments
     */
    public static void main(String args[]){
        new Game();
    }

    /**
     * Application main render method, using buffer strategy with 3 buffers for rendering graphics.
     */
    private void render(){
        BufferStrategy bs = this.getBufferStrategy();
        if (bs == null){
            this.createBufferStrategy(3);
            return;
        }

        Graphics g = bs.getDrawGraphics();

        if (gameState == STATE.Menu || gameState == STATE.Instructions || gameState == STATE.Play){
            renderMenuScreen(g);
        }else if (gameState == STATE.Game){
            renderGameScreen(g);
        }

        handler.render(g);

        g.dispose();
        bs.show();
    }

    /**
     * Renders menu screen.
     * @param g application's graphics
     */
    private void renderMenuScreen(Graphics g){
        g.setColor(Color.black);
        g.fillRect(0,0, WIDTH, HEIGHT);
        menu.render(g);
    }

    /**
     * Renders game screen.
     * @param g application's graphics
     */
    private void renderGameScreen(Graphics g){
        g.setColor(new java.awt.Color(115, 204, 255, 57));
        g.fillRect(0,0, WIDTH, HEIGHT);
        menu.menuButtonRender(g);
        informationBox.render(g);
        board.renderBoard(g);
        gamePlay.render(g);
    }
}
