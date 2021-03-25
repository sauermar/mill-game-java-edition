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

    /**
     * Renders all registered game objects on the handler.
     * @param g application's graphics
     */
    public void render (Graphics g){
        for (int i = 0; i < objects.size(); i++){
            GameObject tempObject = objects.get(i);

            tempObject.render(g);
        }
    }

    /**
     * Registers game object to the handler.
     * @param object game object to be registered
     */
    public void addObject(GameObject object){
        objects.add(object);
    }

    /**
     * Unregisters game object from the handler.
     * @param object game object to be unregistered
     */
    public void removeObject(GameObject object){
         objects.remove(object);
    }

    /**
     * Unregisters all registered objects from the handler.
     */
    public void removeAll(){
        objects.removeAll(objects);
    }

    /**
     * Counts how many game objects are registered on the handler.
     * @return number of registered game objects
     */
    public int countObjects(){
        return objects.size();
    }

    /**
     * Returns registered game object on index.
     * @param i index of game object
     * @return game object
     */
    public GameObject getObject(int i){
        return objects.get(i);
    }

    /**
     * Updates registered game object on index.
     * @param i index of game object
     * @param object updated game object
     */
    public void setObject(int i, GameObject object){
        objects.set(i, object);
    }

    /**
     * Returns index of registered game object on given coordinates.
     * @param coordinates x,y coordinates on which the object is placed
     * @return index of game object on coordinates or -1
     */
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

    /**
     * Returns color of registered game object on index.
     * @param i index of game object
     * @return white|black
     */
    public COLOR getColor(int i){
        return objects.get(i).getColor();
    }

    /**
     * Returns registered game object color with given id.
     * @param id id of the game object
     * @return white|black
     */
    public COLOR getColorFromId(ID id){
        for (int i = 0; i < objects.size(); i++) {
            GameObject tempObject = objects.get(i);
          if (tempObject.getId() == id){
              return tempObject.getColor();
          }
        }
        return null;
    }

    /**
     * Returns all registered game objects with given color.
     * @param color color of the game objects
     * @return list of game objects with given color
     */
    public List<GameObject> getObjectsWithColor(COLOR color){
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
