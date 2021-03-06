package com.mill.game.main;

import com.mill.game.main.enums.COLOR;
import com.mill.game.main.helpers.GameLogic;
import com.mill.game.main.models.Coordinates;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.util.List;

public class GamePlayBase extends MouseAdapter {
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

    public void render (Graphics g){

    }

    protected boolean isMill(Coordinates coordinates, COLOR color){
        if (GameLogic.isMill(boardRows, coordinates, color, handler)){
            return true;
        }else if (GameLogic.isMill(boardColumns, coordinates, color, handler)){
            return true;
        }
        return false;
    }
}
