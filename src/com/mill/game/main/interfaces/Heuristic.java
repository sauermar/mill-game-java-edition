package com.mill.game.main.interfaces;

import com.mill.game.main.Handler;
import com.mill.game.main.enums.COLOR;

public interface Heuristic {
    public int evaluate(Handler handler, COLOR color);
}
