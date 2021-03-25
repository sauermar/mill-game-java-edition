package com.mill.game.main.common;

import com.mill.game.main.Board;
import com.mill.game.main.Handler;
import com.mill.game.main.InformationBox;
import com.mill.game.main.enums.COLOR;
import com.mill.game.main.helpers.GameLogic;
import com.mill.game.main.models.Coordinates;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.util.List;

public abstract class GamePlayBase extends MouseAdapter {
    protected Handler handler;
    protected InformationBox informationBox;
    protected java.util.List<Coordinates> boardRows;
    protected List<Coordinates> boardColumns;

    protected final int RADIUS = 15;
    protected final int SIDE = 10;

    public GamePlayBase(Handler handler, Board board, InformationBox informationBox){
        this.handler = handler;
        this.informationBox = informationBox;
        boardRows = board.getBoardRows();
        boardColumns = board.getBoardColumns();
    }

    public abstract void render(Graphics g);

    /**
     * Tests whether a mill is created by positioning stone with given color to the given coordinates.
     * @param coordinates coordinates of the tested stone
     * @param color color of the tested stone
     * @return true if the move of the stone with given color on coordinates creates a mill,false otherwise
     */
    protected boolean isMill(Coordinates coordinates, COLOR color){
        return (GameLogic.isMill(boardRows, coordinates, color, handler) ||
                GameLogic.isMill(boardColumns, coordinates, color, handler));
    }
}
