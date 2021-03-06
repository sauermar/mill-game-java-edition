package com.mill.game.main;

import com.mill.game.main.helpers.GameLogic;
import com.mill.game.main.enums.COLOR;
import com.mill.game.main.enums.ID;
import com.mill.game.main.enums.PHASE;
import com.mill.game.main.interfaces.*;
import com.mill.game.main.models.Coordinates;
import com.mill.game.main.models.GameObject;
import com.mill.game.main.models.Move;
import com.mill.game.main.models.Player;

import java.util.ArrayList;
import java.util.List;

import java.util.Random;

public class Minimax implements ArtificialPlayer {
    private final int depth = 5;

    private List<Coordinates> boardRows;
    private List<Coordinates> boardColumns;
    private Heuristic heuristic;
    private Handler handler;
    private COLOR aiColor;

    private Move maxMove, minMove;
    private Random random = new Random();

    public Minimax(Heuristic heuristic, Handler handler, Board board){
        this.heuristic = heuristic;
        this.handler = handler;
        aiColor = handler.getColorFromId(ID.Minimax);
        boardRows = board.getBoardRows();
        boardColumns = board.getBoardColumns();
    }

    public Move move(PHASE phase, int i){
        minimax(depth, aiColor, handler, i, phase);
        Move moveResult;
        if (aiColor == COLOR.White){
            moveResult = maxMove;
        }else{
            moveResult = minMove;
        }
        return moveResult;
    }

    private int minimax(int depth, COLOR color, Handler handler, int i, PHASE phase){
        if (depth == 0){
            return heuristic.evaluate(handler, color);
        }

        List<Move> possibleMoves = getPossibleMoves(phase, color);
        for (Move move : possibleMoves) {
            Handler newHandler = new Handler();
            GameLogic.copyHandledObjects(handler, newHandler);
            gameSimulation(newHandler, phase, move, color, i);
            int eval = minimax(depth - 1, GameLogic.changeColor(color), newHandler, i++, phase);
            if (color == COLOR.White){
                int maxEval = Integer.MIN_VALUE;
                if (depth == this.depth && eval > maxEval){
                    maxMove = move;
                }
                maxEval = Math.max(maxEval, eval);

                return maxEval;
            }else if (color == COLOR.Black){
                int minEval = Integer.MAX_VALUE;

                if (depth == this.depth && eval < minEval){
                    minMove = move;
                }
                minEval = Math.max(minEval, eval);
                return minEval;
            }
        }
        return 0;
    }

    private void gameSimulation(Handler handler, PHASE phase, Move move, COLOR color, int i){
        ID id = color == aiColor? ID.Minimax : ID.Player1;
        Coordinates coordinatesTo = move.getTo();
        Coordinates coordinatesFrom = move.getFrom();
        if (phase == PHASE.First) {
            handler.setObject(i, new Player(coordinatesTo.getX(), coordinatesTo.getY(), id, color));
        }else if (phase == PHASE.Second){
            i = handler.getIndexOnCoordinates(coordinatesFrom);
            handler.setObject(i, new Player(coordinatesTo.getX(), coordinatesTo.getY(), id, color));
        }
        if(isMill(coordinatesTo, color, handler)){
            takeStone(handler, GameLogic.changeColor(color));
        }
    }

    private List<Move> getPossibleMoves(PHASE phase, COLOR color){
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
                for (GameObject stone : stones){
                    for (Coordinates coordinates : boardRows) {
                        if (handler.getIndexOnCoordinates(coordinates) == -1) {
                            if (GameLogic.isMoveValid(boardRows, boardColumns,
                                    new Coordinates(stone.getX(), stone.getY()), coordinates)){
                                possibleMoves.add(new Move(new Coordinates(stone.getX(), stone.getY()), coordinates));
                            }
                        }
                    }
                }
            }
        }
        return possibleMoves;
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
        handler.removeObject(handler.getObject(randomInt));
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
}
