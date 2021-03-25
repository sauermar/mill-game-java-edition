package com.mill.game.main.helpers;

import com.mill.game.main.Handler;
import com.mill.game.main.models.Coordinates;
import com.mill.game.main.enums.COLOR;
import com.mill.game.main.models.GameObject;

import java.util.List;

public final class GameLogic {
    /**
     * Tests whether a mill was created with stone on given coordinates with given color.
     * @param boardPart board columns or rows to be tested for possible mill
     * @param coordinates coordinates of the tested stone
     * @param color color of the tested stone
     * @param handler current game handler with tested stone registered
     * @return true if stone creates a mill on boardPart, false otherwise
     */
    public static boolean isMill(List<Coordinates> boardPart, Coordinates coordinates, COLOR color, Handler handler){
        int j = boardPart.indexOf(coordinates);

        if (j % 3 == 0){
            if (isMill(boardPart, j, 1,2,color, handler)){
                return true;
            }
        } else if (j % 3 == 1){
            if (isMill(boardPart, j, -1,1,color, handler)){
                return true;
            }
        } else if (j % 3 == 2){
            if (isMill(boardPart, j, -2,-1,color, handler)){
                return true;
            }
        }
        return false;
    }

    /**
     * Sub-testing method for mill detection.
     * @param boardPart  board columns or rows to be tested for possible mill
     * @param index index of coordinates of the tested stone on the specific boardPart
     * @param i number which when added to index creates index of the second stone needed for creation of the mill on the specific boardPart
     * @param j number which when added to index creates index of the third stone needed for creation of the mill on the specific boardPart
     * @param color color of the tested stone
     * @param handler current game handler with tested stone registered
     * @return true if stone is part of a mill on boardPart, false otherwise
     */
    public static boolean isMill(List<Coordinates> boardPart, int index, int i, int j, COLOR color, Handler handler){
        int p,k;
        p = handler.getIndexOnCoordinates(boardPart.get(index + i));
        k = handler.getIndexOnCoordinates(boardPart.get(index + j));
        if (p != -1 && k != -1){
            if (handler.getColor(p) == color && handler.getColor(k) == color){
                return true;
            }else{
                return false;
            }
        }else{
            return false;
        }
    }

    /**
     * Returns opposite color to the color given.
     * @param color given color to be inverted
     * @return inverted color | null if given color is not expected
     */
    public static COLOR changeColor(COLOR color){
        if (color == COLOR.Black){
            return COLOR.White;
        }else if (color == COLOR.White){
            return COLOR.Black;
        }
        return null;
    }

    /**
     * Returns the current number of player's stones with given color on board.
     * @param handler current game handler with registered stones
     * @param playerColor color of the player, we will count stones
     * @param board coordinates of the board circles
     * @return number of stones with given color on board
     */
    public static int numberOfStones(Handler handler, COLOR playerColor, List<Coordinates> board){
        int count = 0;
        for(int i = 0; i < handler.countObjects(); i++) {
            if (handler.getColor(i) == playerColor){
                GameObject tempObject = handler.getObject(i);
                for (Coordinates place : board){
                    if (place.getX() == tempObject.getX() && place.getY() == tempObject.getY()){
                        count++;
                    }
                }
            }
        }
        return count;
    }

    /**
     * Returns the current number of registered player's stones with given color.
     * @param handler current game handler with registered stones
     * @param playerColor color of the player, we will count stones
     * @return number of stones with given color
     */
    public static int numberOfStones(Handler handler, COLOR playerColor){
        int count = 0;
        for(int i = 0; i < handler.countObjects(); i++) {
            if (handler.getColor(i) == playerColor){
                count++;
            }
        }
        return count;
    }

    /**
     * Tests whether a move from coordinates to coordinates is valid according to the second game phase rules.
     * @param boardRows list of coordinates making board rows
     * @param boardColumns list of coordinates making board columns
     * @param oldCoordinates coordinates the stone is moving from
     * @param newCoordinates coordinates the stone is moving to
     * @return true if specified move is valid, false otherwise
     */
    public static boolean isMoveValid(List<Coordinates> boardRows, List<Coordinates> boardColumns,
                                      Coordinates oldCoordinates, Coordinates newCoordinates){
        int r = boardRows.indexOf(oldCoordinates);
        int c = boardColumns.indexOf(oldCoordinates);

        if (validation(boardRows, newCoordinates, r, 0,1, 0)){ return true; }
        else if (validation(boardColumns, newCoordinates, c, 0,1, 0)){return true;}
        else if (validation(boardRows, newCoordinates, r, 1,-1, 1)){return true;}
        else if (validation(boardColumns, newCoordinates, c, 1,-1, 1)){return true;}
        else if (validation(boardRows, newCoordinates, r, 2,-1, 0)){return true;}
        else if (validation(boardColumns, newCoordinates, c, 2,-1, 0)){return true;}

        return false;
    }

    /**
     * Sub-validation method for testing whether a move from coordinates to coordinates
     * is valid according to the second game phase rules.
     * @param boardPart tested list of coordinates making a specific board part
     * @param newCoordinates coordinates the stone is moving to
     * @param index index of coordinates the stone is moving from according to the specified board part
     * @param result number deciding where on the board the stone was placed
     * @param first number which, when added to index, forms an index of first possible move
     * @param second number which, when added to index, forms an index of second possible move
     * @return true if new coordinates are valid as a move for tested stone, false otherwise
     */
    private static boolean validation(List<Coordinates> boardPart, Coordinates newCoordinates,
                                      int index, int result, int first, int second){
        if (index % 3 == result){
            if (boardPart.get(index + first).getX() == newCoordinates.getX() &&
                    boardPart.get(index + first).getY() == newCoordinates.getY()){
                return true;
            }
            if (second != 0) {
                if (boardPart.get(index + second).getX() == newCoordinates.getX() &&
                        boardPart.get(index + second).getY() == newCoordinates.getY()) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Copies all registered objects from one handler to another.
     * @param oldHandler handler from which objects are copied
     * @param newHandler handler in which copied objects are registered
     */
    public static void copyHandledObjects(Handler oldHandler, Handler newHandler){
        for (int i = 0; i < oldHandler.countObjects(); i++){
            newHandler.addObject(oldHandler.getObject(i));
        }
    }
}
