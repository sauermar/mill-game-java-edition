package com.mill.game.main;

import com.mill.game.main.enums.COLOR;
import com.mill.game.main.enums.ID;
import com.mill.game.main.models.Coordinates;
import com.mill.game.main.models.GameObject;

import java.awt.*;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

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

    public COLOR getColorFromId(ID id){
        for (int i = 0; i < objects.size(); i++) {
            GameObject tempObject = objects.get(i);
          if (tempObject.getId() == id){
              return tempObject.getColor();
          }
        }
        return null;
    }

    public List<GameObject> getObjectWithColor(COLOR color){
        List<GameObject> colorObjects = new ArrayList<>();
        for (int i = 0; i < objects.size(); i++){
            GameObject tempObject = objects.get(i);
            if (tempObject.getColor() == color){
                colorObjects.add(tempObject);
            }
        }
        return colorObjects;
    }

}
