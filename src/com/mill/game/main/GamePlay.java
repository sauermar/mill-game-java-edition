package com.mill.game.main;

import com.mill.game.main.common.GamePlayBase;
import com.mill.game.main.helpers.GameLogic;
import com.mill.game.main.helpers.Helpers;
import com.mill.game.main.enums.COLOR;
import com.mill.game.main.enums.PHASE;
import com.mill.game.main.enums.STATE;
import com.mill.game.main.models.Coordinates;
import com.mill.game.main.models.GameObject;
import com.mill.game.main.models.Stone;

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

       getAnotherStone();
    }

    /**
     * Orchestrates game flow after mouse click happened.
     * @param e Mouse pressed event, contains mouse's x,y
     */
    public void mousePressed(MouseEvent e){
        int mx = e.getX();
        int my = e.getY();

        if (game.gameState == STATE.Game) {
            //menu button
            if (Helpers.mouseOver(mx, my, 355, 5, 70, 20)) {
                game.gameState = STATE.Menu;
                handler.removeAll();
            }
            //first game phase
            if (phase == PHASE.First) {
                playFirstGamePhase(mx, my);
            // second game phase
            }else if (phase == PHASE.Second) {
                playSecondGamePhase(mx,my);
            //mill was detected
            }else if (phase == PHASE.Mill){
                playMillPhase(mx, my);
            }

        }
    }

    public void mouseReleased (MouseEvent e){

    }

    /**
     * Renders the game play elements.
     * @param g application's graphics
     */
    public void render(Graphics g){
        if (phase == PHASE.First && currentStone != null){
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

    /**
     * Necessary change in information when mill is created.
     */
    private void mill(){
        beforeMillPhase = phase;
        phase = PHASE.Mill;
        informationBox.changeMessage(
                informationBox.millCreated(currentStone.getColor().toString())
        );
    }

    /**
     * Gets the next stone. Relevant only for the first game phase.
     */
    private void getAnotherStone(){
        if (i < handler.countObjects()){
            currentStone = handler.getObject(i);
        }
    }

    /**
     * Orchestrates the flow of the first game phase.
     * @param mx mouse click's x coordinate
     * @param my mouse click's y coordinate
     */
    private void playFirstGamePhase(int mx, int my){
        for (Coordinates coordinates : boardRows) {
            if (Helpers.mouseOver(mx, my, coordinates.getX() - SIDE, coordinates.getY() - SIDE, 20, 20)) {
                if (handler.getIndexOnCoordinates(coordinates) == -1) {
                    click++;
                    handler.setObject(i, new Stone(coordinates.getX(), coordinates.getY(), currentStone.getId(), currentStone.getColor()));
                    if (isMill(coordinates, currentStone.getColor())) {
                        mill();
                        break;
                    }
                    i++;
                    getAnotherStone();
                    if (click == 18) {
                        playerColor = GameLogic.changeColor(currentStone.getColor());
                        phase = PHASE.Second;
                        informationBox.changeMessage(
                                informationBox.secondPhase(playerColor.toString())
                        );
                    }
                }
            }
        }
    }

    /**
     * Orchestrates the flow of the second game phase.
     * @param mx mouse click's x coordinate
     * @param my mouse click's y coordinate
     */
    private void playSecondGamePhase(int mx, int my){
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
                            handler.setObject(i, new Stone(coordinates.getX(), coordinates.getY(), currentStone.getId(), currentStone.getColor()));
                        }else{
                            if (GameLogic.isMoveValid(
                                    boardRows, boardColumns, new Coordinates(currentStone.getX(), currentStone.getY()), coordinates
                            )
                            ){
                                handler.setObject(
                                        i, new Stone(coordinates.getX(), coordinates.getY(), currentStone.getId(), currentStone.getColor())
                                );
                            }else{
                                informationBox.changeMessage(informationBox.INVALID_MOVE);
                                break;
                            }
                        }
                        playerColor = GameLogic.changeColor(playerColor);
                        isChosen = false;
                        informationBox.changeMessage(
                                informationBox.secondPhase(playerColor.toString())
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
    }

    /**
     * Orchestrates the game flow when a mill was detected.
     * @param mx mouse click's x coordinate
     * @param my mouse click's y coordinate
     */
    private void playMillPhase(int mx, int my) {
        for (Coordinates coordinates : boardRows) {
            if (Helpers.mouseOver(mx, my, coordinates.getX() - SIDE, coordinates.getY() - SIDE, 20, 20)){
                int index = handler.getIndexOnCoordinates(coordinates);
                if ( index == -1 || handler.getColor(index) == currentStone.getColor()){
                    informationBox.changeMessage(informationBox.INVALID_CHOICE);
                }else{
                    handler.removeObject(handler.getObject(index));
                    getAnotherStone();
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
