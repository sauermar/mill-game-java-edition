package com.mill.game.main.common;

import com.mill.game.main.Board;
import com.mill.game.main.Handler;
import com.mill.game.main.enums.COLOR;
import com.mill.game.main.enums.ID;
import com.mill.game.main.enums.PHASE;
import com.mill.game.main.helpers.GameLogic;
import com.mill.game.main.models.Coordinates;
import com.mill.game.main.models.GameObject;
import com.mill.game.main.models.Move;
import com.mill.game.main.models.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class ArtificialPlayerBase {
    protected COLOR aiColor;
    protected Handler handler;
    protected List<Coordinates> boardRows;
    protected List<Coordinates> boardColumns;

    protected int mills = 0;
    protected int takenStones = 0;

    private Random random = new Random();

    public ArtificialPlayerBase(Handler handler, Board board){
        this.handler = handler;
        boardRows = board.getBoardRows();
        boardColumns = board.getBoardColumns();
    }

    protected void gameSimulation(Handler handler, PHASE phase, Move move, COLOR color, int i){
        ID id = color == aiColor? ID.Minimax : ID.Player1;
        Coordinates coordinatesTo = move.getTo();
        Coordinates coordinatesFrom = move.getFrom();
        if (phase == PHASE.First) {
            if (i < handler.countObjects()) {
                handler.setObject(i, new Player(coordinatesTo.getX(), coordinatesTo.getY(), id, color));
                if(isMill(coordinatesTo, color, handler)){
                    if (color == aiColor) {
                        mills++;
                    }else{
                        takenStones++;
                    }
                    takeStone(handler, GameLogic.changeColor(color));
                }
            }
        }else if (phase == PHASE.Second) {
            i = handler.getIndexOnCoordinates(coordinatesFrom);
            handler.setObject(i, new Player(coordinatesTo.getX(), coordinatesTo.getY(), id, color));
            if (isMill(coordinatesTo, color, handler)) {
                if (color == aiColor) {
                    mills++;
                }else{
                    takenStones++;
                }
                takeStone(handler, GameLogic.changeColor(color));
            }
        }
    }

    private boolean isMill(Coordinates coordinates, COLOR color, Handler handler){
        if (GameLogic.isMill(boardRows, coordinates, color, handler)){
            return true;
        }else if (GameLogic.isMill(boardColumns, coordinates, color, handler)){
            return true;
        }
        return false;
    }

    public void takeStone(Handler handler, COLOR color){
        List<GameObject> candidates = new ArrayList<GameObject>();
        candidates.addAll(getAlmostMillStones(handler, boardRows, color));
        candidates.addAll(getAlmostMillStones(handler, boardColumns, color));
        if (candidates.size() == 0){
            GameObject tempObject;
            for (int i = 0; i < handler.countObjects(); i++){
                tempObject = handler.getObject(i);
                if (tempObject.getColor() == color){
                    candidates.add(tempObject);
                }
            }
        }

        int randomInt = random.nextInt(candidates.size());
        handler.removeObject(candidates.get(randomInt));
    }

    private List<GameObject> getAlmostMillStones(Handler handler, List<Coordinates> boardPart, COLOR color){
        List<GameObject> almostMill = new ArrayList<GameObject>();
        int count, index;
        for ( int i = 0; i < boardPart.size(); i = i + 3){
            count = 0;
            for(int j = 0; j < 3; j++) {
                index = handler.getIndexOnCoordinates(boardPart.get(i + j));
                if (index != -1 && handler.getObject(index).getColor() == color) {
                    count++;
                }
            }
            //almost mill spotted
            if (count == 2){
                for (int j = 0; j < 3; j++){
                    index = handler.getIndexOnCoordinates(boardPart.get(i + j));
                    if (index != -1 && handler.getObject(index).getColor() == color) {
                        almostMill.add(handler.getObject(index));
                    }
                }
            }
        }
        return almostMill;
    }

    protected List<Move> getPossibleMoves(Handler handler, PHASE phase, COLOR color){
        List<Move> possibleMoves = new ArrayList<Move>();
        if (phase == PHASE.First){
            for (Coordinates coordinates : boardRows){
                if (handler.getIndexOnCoordinates(coordinates) == -1){
                    possibleMoves.add(new Move(null, coordinates));
                }
            }
        }else if (phase == PHASE.Second){
            List<GameObject> stones = handler.getObjectWithColor(color);
            if (GameLogic.numberOfStones(handler, color) == 3){
                for (GameObject stone : stones) {
                    for (Coordinates coordinates : boardRows) {
                        if (handler.getIndexOnCoordinates(coordinates) == -1) {
                            possibleMoves.add(new Move(new Coordinates(stone.getX(), stone.getY()), coordinates));
                        }
                    }
                }
            }else{
                List<Coordinates> emptyCoordinates = new ArrayList<>();
                for (Coordinates coordinates : boardRows) {
                    if (handler.getIndexOnCoordinates(coordinates) == -1){
                        emptyCoordinates.add(coordinates);
                    }
                }
                for (GameObject stone : stones){
                    for (Coordinates coordinates : emptyCoordinates) {
                        if (GameLogic.isMoveValid(boardRows, boardColumns,
                                new Coordinates(stone.getX(), stone.getY()), coordinates)){
                            possibleMoves.add(new Move(new Coordinates(stone.getX(), stone.getY()), coordinates));
                        }
                    }
                }
            }
        }
        return possibleMoves;
    }
}
