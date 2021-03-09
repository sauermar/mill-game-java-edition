package com.mill.game.main;

import com.mill.game.main.common.GamePlayBase;
import com.mill.game.main.helpers.GameLogic;
import com.mill.game.main.helpers.Helpers;
import com.mill.game.main.enums.COLOR;
import com.mill.game.main.enums.PHASE;
import com.mill.game.main.enums.STATE;
import com.mill.game.main.interfaces.ArtificialPlayer;
import com.mill.game.main.models.Coordinates;
import com.mill.game.main.models.GameObject;
import com.mill.game.main.models.Move;
import com.mill.game.main.models.Player;

import java.awt.*;
import java.awt.event.MouseEvent;

public class AiGamePlay extends GamePlayBase {

    private Game game;
    private ArtificialPlayer ai;

    public PHASE phase;

    private PHASE beforeMillPhase;
    private GameObject currentStone;
    private int i = -1;
    private int click = 0;
    private COLOR playerColor;
    private boolean isChosen = false;

    public AiGamePlay(Game game, Handler handler, ArtificialPlayer ai){
        super(handler, game.board, game.informationBox);
        this.game = game;
        this.ai = ai;
        phase = PHASE.First;
        getAnotherStone();
    }

    public void mousePressed (MouseEvent e){
        int mx = e.getX();
        int my = e.getY();

        if (game.gameState == STATE.Game) {
            if (Helpers.mouseOver(mx, my, 600, 5, 70, 20)) {
                game.gameState = STATE.Menu;
                handler.removeAll();
            }
            // first game phase
            if (phase == PHASE.First) {
                for (Coordinates coordinates : boardRows) {
                    if (Helpers.mouseOver(mx, my, coordinates.getX() - SIDE, coordinates.getY() - SIDE, 20, 20)) {
                        if (handler.getIndexOnCoordinates(coordinates) == -1) {
                            click += 2;
                            handler.setObject(i, new Player(coordinates.getX(), coordinates.getY(), currentStone.getId(), currentStone.getColor()));
                            if (isMill(coordinates, currentStone.getColor())) {
                                mill(false);
                                break;
                            }
                            getAnotherStone();
                            Coordinates aiCoordinates = aiPlayFirstPhase(i);
                            if (isMill(new Coordinates(aiCoordinates.getX(), aiCoordinates.getY()), currentStone.getColor())) {
                                mill(true);
                                break;
                            }
                            getAnotherStone();
                            if (click == 18) {
                                changeToSecondPhase();
                            }
                        }
                    }
                }
                // second game phase
            } else if (phase == PHASE.Second) {
                for (Coordinates coordinates : boardRows) {
                    if (Helpers.mouseOver(mx, my, coordinates.getX() - SIDE, coordinates.getY() - SIDE, 20, 20)) {
                        int index = handler.getIndexOnCoordinates(coordinates);
                        if (!isChosen) {
                            if (index != -1 && handler.getColor(index) == playerColor) {
                                i = index;
                                currentStone = handler.getObject(index);
                                isChosen = true;
                            }
                        } else {
                            if (index == -1) {
                                if (GameLogic.numberOfStones(handler, playerColor) == 3) {
                                    handler.setObject(i, new Player(coordinates.getX(), coordinates.getY(), currentStone.getId(), currentStone.getColor()));
                                } else {
                                    if (GameLogic.isMoveValid(boardRows, boardColumns, new Coordinates(currentStone.getX(), currentStone.getY()), coordinates)) {
                                        handler.setObject(i, new Player(coordinates.getX(), coordinates.getY(), currentStone.getId(), currentStone.getColor()));
                                    } else {
                                        informationBox.changeMessage(informationBox.INVALID_MOVE);
                                        break;
                                    }
                                }
                                playerColor = GameLogic.changeColor(playerColor);
                                isChosen = false;
                                informationBox.changeMessage(
                                        informationBox.secondPhase(playerColor.toString())
                                );
                                if (isMill(coordinates, currentStone.getColor())) {
                                    mill(false);
                                    break;
                                }
                                //ai player
                                Coordinates aiCoordinates = aiPlaySecondPhase();
                                if (isMill(new Coordinates(aiCoordinates.getX(), aiCoordinates.getY()), currentStone.getColor())) {
                                    mill(true);
                                    break;
                                }
                            } else if (handler.getColor(index) == playerColor) {
                                i = index;
                                currentStone = handler.getObject(index);
                            } else {
                                informationBox.changeMessage(informationBox.INVALID_SELECTION);
                            }
                        }
                    }
                }
                //mill was created
            } else if (phase == PHASE.Mill) {
                for (Coordinates coordinates : boardRows) {
                    if (Helpers.mouseOver(mx, my, coordinates.getX() - SIDE, coordinates.getY() - SIDE, 20, 20)) {
                        int index = handler.getIndexOnCoordinates(coordinates);
                        if (index == -1 || handler.getColor(index) == currentStone.getColor()) {
                            informationBox.changeMessage(informationBox.INVALID_CHOICE);
                        } else {
                            handler.removeObject(handler.getObject(index));
                            currentStone = handler.getObject(i);
                            checkGameEnded();
                            if (phase == PHASE.First) {
                                Coordinates aiCoordinates = aiPlayFirstPhase(i);
                                if (isMill(new Coordinates(aiCoordinates.getX(), aiCoordinates.getY()), currentStone.getColor())) {
                                    mill(true);
                                    break;
                                }
                                getAnotherStone();
                                if (click == 18) {
                                    changeToSecondPhase();
                                }
                            } else if (phase == PHASE.Second) {
                                Coordinates aiCoordinates = aiPlaySecondPhase();
                                if (isMill(new Coordinates(aiCoordinates.getX(), aiCoordinates.getY()), currentStone.getColor())) {
                                    mill(true);
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

    private void mill(boolean ai){
        beforeMillPhase = phase;
        phase = PHASE.Mill;
        informationBox.changeMessage(
                informationBox.millCreated(currentStone.getColor().toString())
        );
        if (ai){
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            this.ai.takeStone(handler, GameLogic.changeColor(currentStone.getColor()));
            checkGameEnded();
            if (phase == PHASE.First){
                i--;
                getAnotherStone();
                if (click == 18) {
                    changeToSecondPhase();
                }
            }
        }
    }

    private Coordinates aiPlayFirstPhase(int i){
        Move move = ai.move(phase, i);
        Coordinates moveTo = move.getTo();
        handler.setObject(i, new Player(moveTo.getX(), moveTo.getY(), currentStone.getId(), currentStone.getColor()));
        return moveTo;
    }

    private Coordinates aiPlaySecondPhase() {
        Move move = ai.move(phase, 0);
        int index = handler.getIndexOnCoordinates(move.getFrom());
        currentStone = handler.getObject(index);
        isChosen = true;
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        Coordinates moveTo = move.getTo();
        handler.setObject(index, new Player(moveTo.getX(), moveTo.getY(), currentStone.getId(), currentStone.getColor()));
        isChosen = false;
        playerColor = GameLogic.changeColor(playerColor);
        informationBox.changeMessage(
                informationBox.secondPhase(playerColor.toString())
        );
        return moveTo;
    }

    private void getAnotherStone(){
        i++;
        if (i < handler.countObjects()) {
            currentStone = handler.getObject(i);
        }
    }

    private void checkGameEnded(){
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

    private void changeToSecondPhase(){
        playerColor = GameLogic.changeColor(currentStone.getColor());
        phase = PHASE.Second;
        informationBox.changeMessage(
                informationBox.secondPhase(playerColor.toString())
        );
    }
}
