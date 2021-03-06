package com.mill.game.main;

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
    private int i = 0;
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
                                mill();
                                break;
                            }
                            i++;
                            getAnotherStone();
                            aiPlay(i);
                            if (isMill(
                                    new Coordinates(currentStone.getX(), currentStone.getY()), currentStone.getColor())
                            ){
                                mill();
                                break;
                            }
                            i++;
                            getAnotherStone();
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

    private void aiPlay(int i){
        Move move = ai.move(phase, i);
        Coordinates moveTo = move.getTo();
        handler.setObject(i, new Player(moveTo.getX(), moveTo.getY(), currentStone.getId(), currentStone.getColor()));
    }

    private void getAnotherStone(){
        if (i < handler.countObjects()) {
            currentStone = handler.getObject(i);
        }
    }
}
