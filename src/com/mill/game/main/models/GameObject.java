package com.mill.game.main.models;

import com.mill.game.main.enums.COLOR;
import com.mill.game.main.enums.ID;

import javax.swing.*;
import java.awt.*;

public abstract class GameObject{

    protected int x, y;
    protected ID id;
    protected COLOR color;

    public GameObject(int x, int y, ID id, COLOR color){
        this.x = x;
        this.y = y;
        this.id = id;
        this.color = color;
    }

    /**
     * Renders game object.
     * @param g application's graphics
     */
    public abstract void render(Graphics g);

    /**
     * Updates the x coordinate.
     * @param x new x coordinate
     */
    public void setX(int x){
        this.x = x;
    }

    /**
     * @return the x coordinate
     */
    public int getX(){
        return x;
    }

    /**
     * Updates the y coordinate.
     * @param y new y coordinate
     */
    public void setY(int y){
        this.y = y;
    }

    /**
     * @return the y coordinate
     */
    public int getY(){
        return y;
    }

    /**
     * Updates id.
     * @param id new id
     */
    public void setId(ID id){
        this.id = id;
    }

    /**
     * @return id
     */
    public ID getId(){
        return id;
    }

    /**
     * @return color
     */
    public COLOR getColor(){
        return color;
    }

}
