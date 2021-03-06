package com.mill.game.main;

import com.mill.game.main.helpers.GameLogic;
import com.mill.game.main.enums.COLOR;
import com.mill.game.main.interfaces.Heuristic;

public class StoneCountEvaluation implements Heuristic {
    /**
     * Returns the number of stones currently on board of the player playing with color.
     * @param handler game handler
     * @param color color of the player
     * @return number of stones on board
     */
    public int evaluate(Handler handler, COLOR color){
        return GameLogic.numberOfStones(handler, color);
    }
}
