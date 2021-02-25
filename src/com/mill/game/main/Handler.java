package com.mill.game.main;

import com.mill.game.main.enums.COLOR;

import java.awt.*;
import java.util.Collection;
import java.util.LinkedList;

public class Handler {

    private LinkedList<GameObject> objects = new LinkedList<GameObject>();

    public void tick(){
        for (int i = 0; i < objects.size(); i++){
            GameObject tempObject = objects.get(i);

            tempObject.tick();
        }
    }

    public void render (Graphics g){
        for (int i = 0; i < objects.size(); i++){
            GameObject tempObject = objects.get(i);

            tempObject.render(g);
        }
    }

    public void addObject(GameObject object){
        objects.add(object);
    }

    public void removeObject(GameObject object){
         objects.remove(object);
    }

    public void removeAll(){
        objects.removeAll(objects);
    }

    public int countObjects(){
        return objects.size();
    }

    public GameObject getObject(int i){
        return objects.get(i);
    }

    public void setObject(int i, GameObject object){
        objects.set(i, object);
    }

    public int getIndexOnCoordinates (Coordinates coordinates){
        for (int i = 0; i < objects.size(); i++){
            GameObject tempObject = objects.get(i);
            if (tempObject.getX() == coordinates.getX()){
                if (tempObject.getY() == coordinates.getY()){
                    return i;
                }
            }
        }
        return -1;
    }

    public COLOR getColor(int i){
        return objects.get(i).getColor();
    }

}
