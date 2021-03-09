package com.mill.game.main.interfaces;

import com.mill.game.main.Handler;
import com.mill.game.main.enums.COLOR;
import com.mill.game.main.enums.PHASE;
import com.mill.game.main.models.Move;

public interface ArtificialPlayer {
    public Move move(PHASE phase, int i);
    public void takeStone(Handler handler, COLOR color);
}
