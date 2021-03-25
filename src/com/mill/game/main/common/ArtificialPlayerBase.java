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
import com.mill.game.main.models.Stone;

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

    /**
     * Simulates the game play.
     * @param handler current handler with registered stones
     * @param phase current game phase
     * @param move move to be performed
     * @param color color of the current player
     * @param i index of the stone in handler, relevant only for first game phase
     */
    protected void gameSimulation(Handler handler, PHASE phase, Move move, COLOR color, int i){
        ID id = color == aiColor? ID.Minimax : ID.Player1;
        Coordinates coordinatesTo = move.getTo();
        Coordinates coordinatesFrom = move.getFrom();
        if (phase == PHASE.First) {
            if (i < handler.countObjects()) {
                handler.setObject(i, new Stone(coordinatesTo.getX(), coordinatesTo.getY(), id, color));
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
            handler.setObject(i, new Stone(coordinatesTo.getX(), coordinatesTo.getY(), id, color));
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

    /**
     * Tests whether a mill is created by positioning stone with given color to the given coordinates.
     * @param coordinates coordinates of the tested stone
     * @param color color of the tested stone
     * @param handler current game handler with registered stones
     * @return true if the move of the stone with given color on coordinates creates a mill,false otherwise
     */
    private boolean isMill(Coordinates coordinates, COLOR color, Handler handler){
        return  (GameLogic.isMill(boardRows, coordinates, color, handler) ||
                GameLogic.isMill(boardColumns, coordinates, color, handler));
    }

    /**
     * Checks for opponent's almost mills (two stones in a row or column) and randomly chooses one
     * stone from them to be taken.
     * If there are no almost mills it chooses randomly from the opponent's stones.
     * @param handler current game handler with registered stones
     * @param color color of the stone to be taken
     */
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

    /**
     * Finds almost mills (two stones in a row or column) with given color and adds them to the returning list.
     * @param handler current game handler with registered stones
     * @param boardPart analyzed part of the board (rows/columns)
     * @param color color of the stones
     * @return list of stones, which are two in a row or column (specified by boardPart)
     */
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

    /**
     * Evaluates all moves and returns possible ones according to the current game phase and player color.
     * @param handler current game handler with registered stones
     * @param phase current game phase
     * @param color color of the stones
     * @return list of all possible moves (from, to coordinates)
     */
    protected List<Move> getPossibleMoves(Handler handler, PHASE phase, COLOR color){
        List<Move> possibleMoves = new ArrayList<Move>();
        if (phase == PHASE.First){
            for (Coordinates coordinates : boardRows){
                if (handler.getIndexOnCoordinates(coordinates) == -1){
                    possibleMoves.add(new Move(null, coordinates));
                }
            }
        }else if (phase == PHASE.Second){
            List<GameObject> stones = handler.getObjectsWithColor(color);
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
