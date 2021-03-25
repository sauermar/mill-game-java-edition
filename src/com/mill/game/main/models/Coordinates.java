package com.mill.game.main.models;

public class Coordinates {

    private int x, y;

    public Coordinates (int x, int y){
        this.x = x;
        this.y = y;
    }

    /**
     * Returns x coordinate.
     * @return x coordinate
     */
    public int getX(){
        return x;
    }

    /**
     * Returns y coordinate.
     * @return y coordinate
     */
    public int getY(){
        return y;
    }

    /**
     * Tests whether given coordinates are equal to this coordinates.
     * @param obj coordinates for equality test
     * @return true if given coordinates are equal, false otherwise
     */
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
