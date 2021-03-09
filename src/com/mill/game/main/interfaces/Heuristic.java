package com.mill.game.main.interfaces;

import com.mill.game.main.Handler;
import com.mill.game.main.enums.COLOR;
import com.mill.game.main.models.Coordinates;

import java.util.List;

public interface Heuristic {
    public int evaluate(Handler handler, COLOR color, List<Coordinates> board);
}
