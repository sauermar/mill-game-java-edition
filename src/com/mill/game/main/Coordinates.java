package com.mill.game.main;

public class Coordinates {

    protected int x, y;

    public Coordinates (int x, int y){
        this.x = x;
        this.y = y;
    }

    public int getX(){
        return x;
    }

    public int getY(){
        return y;
    }

    @Override
    public boolean equals(Object obj) {
        Coordinates coordinates = (Coordinates)obj;
        if ((x == coordinates.getX()) && (y == coordinates.getY())){
            return true;
        }
        else{
            return false;
        }
    }

}
