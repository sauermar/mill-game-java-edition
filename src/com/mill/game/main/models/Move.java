package com.mill.game.main.models;

public class Move {
    private final Coordinates from, to;

    public Move(Coordinates from, Coordinates to){
        this.from = from;
        this.to = to;
    }

    /**
     * @return move from coordinates
     */
    public Coordinates getFrom(){
        return from;
    }

    /**
     * @return move to coordinates
     */
    public Coordinates getTo(){
        return to;
    }
}
