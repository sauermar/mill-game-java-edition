package com.mill.game.main;

import com.mill.game.main.enums.ID;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class KeyInput extends KeyAdapter {

    private Handler handler;

    public KeyInput(Handler handler){
        this.handler = handler;
    }

    public void keyPressed(KeyEvent e){
        int key = e.getKeyCode();

        for (int i = 0; i < handler.countObjects(); i++){
            GameObject tempObject = handler.getObject(i);

            if (tempObject.getId() == ID.Player1){
                //key events for player1

                //if (key == KeyEvent.VK_W)
            }
        }
    }

    public void keyReleased(KeyEvent e){
        int key = e.getKeyCode();
    }
}
