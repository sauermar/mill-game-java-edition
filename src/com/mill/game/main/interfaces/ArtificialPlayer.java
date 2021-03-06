package com.mill.game.main.interfaces;

import com.mill.game.main.enums.PHASE;
import com.mill.game.main.models.Move;

public interface ArtificialPlayer {
    public Move move(PHASE phase, int i);
}
