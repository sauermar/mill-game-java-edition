package com.mill.game.main;

import java.awt.*;

public class Game extends Canvas implements Runnable {
    //16x9 ratio for the game window
    public static final int WIDTH = 640, HEIGHT = WIDTH / 12 * 9;

    public Game(){
        new Window(WIDTH, HEIGHT, "Mill game", this);
    }

    public synchronized void start(){

    }

    public void run(){

    }

    public static void main(String args[]){
        new Game();
    }
}
