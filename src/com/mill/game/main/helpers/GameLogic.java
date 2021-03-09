package com.mill.game.main.helpers;

import com.mill.game.main.Handler;
import com.mill.game.main.models.Coordinates;
import com.mill.game.main.enums.COLOR;
import com.mill.game.main.models.GameObject;

import java.util.List;

public final class GameLogic {
    public static boolean isMill(java.util.List<Coordinates> boardPart, Coordinates coordinates, COLOR color, Handler handler){
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

    public static COLOR changeColor(COLOR color){
        if (color == COLOR.Black){
            return COLOR.White;
        }else if (color == COLOR.White){
            return COLOR.Black;
        }
        return null;
    }

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

    public static int numberOfStones(Handler handler, COLOR playerColor){
        int count = 0;
        for(int i = 0; i < handler.countObjects(); i++) {
            if (handler.getColor(i) == playerColor){
                count++;
            }
        }
        return count;
    }

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

    public static void copyHandledObjects(Handler oldHandler, Handler newHandler){
        for (int i = 0; i < oldHandler.countObjects(); i++){
            newHandler.addObject(oldHandler.getObject(i));
        }
    }
}
