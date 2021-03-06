package com.mill.game.main;

import com.mill.game.main.helpers.GameLogic;
import com.mill.game.main.helpers.Helpers;
import com.mill.game.main.enums.COLOR;
import com.mill.game.main.enums.PHASE;
import com.mill.game.main.enums.STATE;
import com.mill.game.main.models.Coordinates;
import com.mill.game.main.models.GameObject;
import com.mill.game.main.models.Player;

import java.awt.*;
import java.awt.event.MouseEvent;

public class GamePlay extends GamePlayBase {

    private Game game;

    public PHASE phase;

    private PHASE beforeMillPhase;
    private GameObject currentStone;
    private int i = 0;
    private int click = 0;
    private COLOR playerColor;
    private boolean isChosen = false;

    public GamePlay(Game game, Handler handler){
        super(handler, game.board, game.informationBox);
        this.game = game;
        phase = PHASE.First;

        if (i < handler.countObjects()) {
            currentStone = handler.getObject(i);
        }
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
                    if (Helpers.mouseOver(mx, my, coordinates.getX() - SIDE, coordinates.getY() - SIDE, 20, 20)){
                        if (handler.getIndexOnCoordinates(coordinates) == -1) {
                            click++;
                            handler.setObject(i, new Player(coordinates.getX(), coordinates.getY(), currentStone.getId(), currentStone.getColor()));
                            if (isMill(coordinates, currentStone.getColor())) {
                                mill();
                                break;
                            }
                            i++;
                            if (i < handler.countObjects()) {
                                currentStone = handler.getObject(i);
                            }
                            if (click == 18) {
                                playerColor = GameLogic.changeColor(currentStone.getColor());
                                phase = PHASE.Second;
                                informationBox.changeMessage(
                                        informationBox.secondPhase(currentStone.getColor().toString())
                                );
                            }
                        }
                    }
                }
                // second game phase
            }else if (phase == PHASE.Second) {
                for (Coordinates coordinates : boardRows) {
                    if (Helpers.mouseOver(mx, my, coordinates.getX() - SIDE, coordinates.getY() - SIDE, 20, 20)){
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
                                    if (GameLogic.isMoveValid(
                                            boardRows, boardColumns, new Coordinates(currentStone.getX(), currentStone.getY()), coordinates
                                        )
                                    ){
                                        handler.setObject(
                                                i, new Player(coordinates.getX(), coordinates.getY(), currentStone.getId(), currentStone.getColor())
                                        );
                                    }else{
                                        informationBox.changeMessage(informationBox.INVALID_MOVE);
                                        break;
                                    }
                                }
                                playerColor = GameLogic.changeColor(playerColor);
                                isChosen = false;
                                informationBox.changeMessage(
                                        informationBox.secondPhase(currentStone.getColor().toString())
                                );
                                if (isMill(coordinates, currentStone.getColor())){
                                    mill();
                                    break;
                                }
                            }else if(handler.getColor(index) == playerColor){
                                i = index;
                                currentStone = handler.getObject(index);
                            }else{
                                informationBox.changeMessage(informationBox.INVALID_SELECTION);
                            }
                        }
                    }
                }
                //mill was detected
            }else if (phase == PHASE.Mill){
                for (Coordinates coordinates : boardRows) {
                    if (Helpers.mouseOver(mx, my, coordinates.getX() - SIDE, coordinates.getY() - SIDE, 20, 20)){
                        int index = handler.getIndexOnCoordinates(coordinates);
                        if ( index == -1 || handler.getColor(index) == currentStone.getColor()){
                            informationBox.changeMessage(informationBox.INVALID_CHOICE);
                        }else{
                            handler.removeObject(handler.getObject(index));
                            currentStone = handler.getObject(i);
                            if (GameLogic.numberOfStones(handler, GameLogic.changeColor(currentStone.getColor())) == 2){
                                informationBox.changeMessage(
                                        informationBox.gameWon(currentStone.getColor().toString())
                                );
                                phase = PHASE.End;
                            }else {
                                phase = beforeMillPhase;
                                if (phase == PHASE.Second) {
                                    informationBox.changeMessage(
                                            informationBox.secondPhase(playerColor.toString())
                                    );
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
                informationBox.changeMessage(
                        informationBox.firstPhase(currentStone.getColor().toString())
                );
                Helpers.paintOvalFocus((Graphics2D) g, 5, currentStone.getX(), currentStone.getY(), RADIUS);

        }else if (phase == PHASE.Second) {
            if (isChosen) {
                Helpers.paintOvalFocus((Graphics2D) g, 5, currentStone.getX(), currentStone.getY(), RADIUS);
            }
        }
    }
    
    private void mill(){
        beforeMillPhase = phase;
        phase = PHASE.Mill;
        informationBox.changeMessage(
                informationBox.millCreated(currentStone.getColor().toString())
        );
    }


}
