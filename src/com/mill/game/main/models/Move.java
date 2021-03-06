package com.mill.game.main.models;

public class Move {
    private Coordinates from, to;

    public Move(Coordinates from, Coordinates to){
        this.from = from;
        this.to = to;
    }

    public Coordinates getFrom(){
        return from;
    }

    public Coordinates getTo(){
        return to;
    }
}
