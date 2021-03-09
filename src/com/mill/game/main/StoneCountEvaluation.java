package com.mill.game.main;

import com.mill.game.main.helpers.GameLogic;
import com.mill.game.main.enums.COLOR;
import com.mill.game.main.interfaces.Heuristic;
import com.mill.game.main.models.Coordinates;

import java.util.List;

public class StoneCountEvaluation implements Heuristic {
    /**
     * Returns the number of stones currently on board of the player playing with color.
     * @param handler game handler
     * @param color color of the player
     * @return number of stones on board
     */
    public int evaluate(Handler handler, COLOR color, List<Coordinates> board){
        int numberOfPlayerStones = GameLogic.numberOfStones(handler, color, board);
        int numberOfOpponentsStones = GameLogic.numberOfStones(handler, GameLogic.changeColor(color), board);
        if (numberOfOpponentsStones < numberOfPlayerStones){
            return numberOfPlayerStones + 1;
        }
        return numberOfPlayerStones;
    }
}
