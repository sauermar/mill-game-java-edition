package com.mill.game.main;

import com.mill.game.main.enums.COLOR;
import com.mill.game.main.enums.PHASE;
import com.mill.game.main.enums.STATE;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;

public class GamePlay extends MouseAdapter {

    private Handler handler;
    private Game game;
    private Board board;
    private InformationBox informationBox;

    private PHASE beforeMillPhase;
    private PHASE phase = PHASE.First;
    private  List<Coordinates> boardRows;
    private  List<Coordinates> boardColumns;
    private final int radius = 15;
    private final int side = 10;
    private GameObject currentStone;
    private int i, click = 0;
    private COLOR playerColor;
    private boolean isChosen = false;

    public GamePlay(Game game, Handler handler, Board board, InformationBox informationBox){
        this.game = game;
        this.handler = handler;
        this.board = board;
        this.informationBox = informationBox;
        boardRows = board.getBoardRows();
        boardColumns = board.getBoardColumns();
    }

    public void mousePressed(MouseEvent e){
        int mx = e.getX();
        int my = e.getY();

        if (game.gameState == STATE.Game){
            if (Helpers.mouseOver(mx, my, 600, 5 , 70, 20)){
                game.gameState = STATE.Menu;
                handler.removeAll();
            }
            //first game phase
            if (phase == PHASE.First){
                for (Coordinates coordinates : boardRows) {
                    if (Helpers.mouseOver(mx, my, coordinates.getX() - side, coordinates.getY() - side, 20, 20)){
                        if (handler.getIndexOnCoordinates(coordinates) == -1) {
                            click++;
                            handler.setObject(i, new Player(coordinates.getX(), coordinates.getY(), currentStone.getId(), currentStone.getColor()));
                            if (isMill(coordinates)) {
                                beforeMillPhase = phase;
                                phase = PHASE.Mill;
                                informationBox.message = String.format("%s player created mill", currentStone.getColor().toString());
                                break;
                            }
                            i++;
                            if (click == 18) {
                                playerColor = GameLogic.changeColor(currentStone.getColor());
                                phase = PHASE.Second;
                                informationBox.message = String.format("Select %s stone for moving", playerColor.toString());
                            }
                        }
                    }
                }
            }else if (phase == PHASE.Second) {
                for (Coordinates coordinates : boardRows) {
                    if (Helpers.mouseOver(mx, my, coordinates.getX() - side, coordinates.getY() - side, 20, 20)){
                        int index = handler.getIndexOnCoordinates(coordinates);
                        if (!isChosen) {
                            if (index != -1 && handler.getColor(index) == playerColor) {
                                i = index;
                                currentStone = handler.getObject(index);
                                isChosen = true;
                            }
                        }else{
                            if (index == -1){
                                if (GameLogic.numberOfStones(handler, playerColor) == 3) {
                                    handler.setObject(i, new Player(coordinates.getX(), coordinates.getY(), currentStone.getId(), currentStone.getColor()));
                                }else{
                                    if (GameLogic.isMoveValid(boardRows, boardColumns,
                                            new Coordinates(currentStone.getX(), currentStone.getY()), coordinates)){
                                        handler.setObject(i, new Player(coordinates.getX(), coordinates.getY(), currentStone.getId(), currentStone.getColor()));
                                    }else{
                                        informationBox.message = "Stone can move in row or column only";
                                        break;
                                    }
                                }
                                playerColor = GameLogic.changeColor(playerColor);
                                isChosen = false;
                                informationBox.message = String.format("Select %s stone for moving", playerColor.toString());
                                if (isMill(coordinates)){
                                    beforeMillPhase = phase;
                                    phase = PHASE.Mill;
                                    informationBox.message = String.format("%s player created mill", currentStone.getColor().toString());
                                    break;
                                }
                            }else if(handler.getColor(index) == playerColor){
                                i = index;
                                currentStone = handler.getObject(index);
                            }else{
                                informationBox.message = "Select different stone or an empty spot";
                            }
                        }
                    }
                }

            }else if (phase == PHASE.Mill){
                for (Coordinates coordinates : boardRows) {
                    if (Helpers.mouseOver(mx, my, coordinates.getX() - side, coordinates.getY() - side, 20, 20)){
                        int index = handler.getIndexOnCoordinates(coordinates);
                        if ( index == -1 || handler.getColor(index) == currentStone.getColor()){
                            informationBox.message = "Please select opponent's stone";
                        }else{
                            handler.removeObject(handler.getObject(index));
                            if (GameLogic.numberOfStones(handler, GameLogic.changeColor(currentStone.getColor())) == 2){
                                informationBox.message = String.format("%s player won the game", currentStone.getColor().toString());
                                phase = PHASE.End;
                            }else {
                                phase = beforeMillPhase;
                                if (phase == PHASE.Second) {
                                    informationBox.message = String.format("Select %s stone for moving", playerColor.toString());
                                }
                            }
                        }
                    }
                }
            }

        }
    }

    public void mouseReleased (MouseEvent e){

    }

    public void render(Graphics g){
        if (phase == PHASE.First){
            if (i < handler.countObjects()) {
                currentStone = handler.getObject(i);
                informationBox.message = String.format("Place the %s stone", currentStone.getColor().toString());
                Helpers.paintOvalFocus((Graphics2D) g, 5, currentStone.getX(), currentStone.getY(), radius);
            }
        }else if (phase == PHASE.Second){
            if (isChosen){
                Helpers.paintOvalFocus((Graphics2D) g, 5, currentStone.getX(), currentStone.getY(), radius);
            }
        }else if (phase == PHASE.Mill){
        }

    }

    private boolean isMill(Coordinates coordinates){
        if (GameLogic.isMill(boardRows, coordinates, currentStone.getColor(), handler)){
            return true;
        }else if (GameLogic.isMill(boardColumns, coordinates, currentStone.getColor(), handler)){
            return true;
        }
        return false;
    }


}
