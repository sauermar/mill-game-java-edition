package com.mill.game.main;

import com.mill.game.main.common.GamePlayBase;
import com.mill.game.main.enums.STATE;

import java.awt.*;
import java.awt.image.BufferStrategy;

public class Game extends Canvas implements Runnable {
    //16x9 ratio for the game window
    public static final int WIDTH = 780, HEIGHT = 680;
    //main game thread
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

    public synchronized void start(){
        thread = new Thread(this);
        thread.start();
        running = true;
    }

    public synchronized void stop(){
        try{
            thread.join();
            running = false;
        }catch (Exception e){
            e.printStackTrace();
        }

    }

    /**
     * Game loop
     */
    public void run(){
        long lastTime = System.nanoTime();
        double amountOfTicks = 60.0;
        double ns = 1000000000 / amountOfTicks;
        double delta = 0;
        long timer = System.currentTimeMillis();
        int frames = 0;
        long now;
        while (running){
            now = System.nanoTime();
            delta += (now - lastTime) / ns;
            lastTime = now;
            while (delta >= 1){
                tick();
                delta--;
            }
            if (running){
                render();
            }
            frames++;

            if (System.currentTimeMillis() - timer > 1000){
                timer += 1000;
               // System.out.println("FPS: " + frames);
                frames = 0;
            }
        }
        stop();
    }

    private void tick(){
        handler.tick();

        if (gameState == STATE.Menu){
            menu.tick();
        }
    }

    ///buffer strategy
    private void render(){
        BufferStrategy bs = this.getBufferStrategy();
        if (bs == null){
            this.createBufferStrategy(3);
            return;
        }

        Graphics g = bs.getDrawGraphics();

        if (gameState == STATE.Menu || gameState == STATE.Instructions || gameState == STATE.Play){
            g.setColor(Color.black);
            g.fillRect(0,0, WIDTH, HEIGHT);
            menu.render(g);
        }else if (gameState == STATE.Game){
            g.setColor(new java.awt.Color(115, 204, 255, 57));
            g.fillRect(0,0, WIDTH, HEIGHT);
            menu.menuButtonRender(g);
            informationBox.render(g);
            board.renderBoard(g);
            gamePlay.render(g);
        }

        handler.render(g);


        g.dispose();
        bs.show();
    }

    public static void main(String args[]){
        new Game();
    }
}
