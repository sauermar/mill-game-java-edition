package com.mill.game.main.ai;

import com.mill.game.main.Board;
import com.mill.game.main.Handler;
import com.mill.game.main.helpers.GameLogic;
import com.mill.game.main.enums.COLOR;
import com.mill.game.main.enums.ID;
import com.mill.game.main.enums.PHASE;
import com.mill.game.main.interfaces.*;
import com.mill.game.main.models.Move;
import com.mill.game.main.common.ArtificialPlayerBase;

import java.util.*;

public class Minimax extends ArtificialPlayerBase implements ArtificialPlayer{

    private final int depth = 4;
    private Heuristic heuristic;
    private HashMap<Move, Integer> evalMoves = new HashMap<Move, Integer>();
    private Random random = new Random();
    private int minEval, maxEval, eval;

    public Minimax(Heuristic heuristic, Handler handler, Board board){
        super(handler, board);
        this.heuristic = heuristic;
        aiColor = handler.getColorFromId(ID.Minimax);
    }

    /**
     * Returns the best next move determined by the minimax algorithm with alpha-beta pruning.
     * If there are multiple best moves, it chooses one of them randomly.
     * @param phase current game phase
     * @param i index relevant only for the first game phase,
     *          where it stands for the index of the current stone to be placed on board
     * @return the best next move determined by the minimax algorithm with alpha-beta pruning
     */
    public Move move(PHASE phase, int i){
        minimax(depth, aiColor, handler, i, phase);
        Integer max = -1;
        List<Move> equalMoves = new ArrayList<>();
        Iterator it = evalMoves.entrySet().iterator();
        while (it.hasNext()){
            Map.Entry<Move, Integer> pair = (Map.Entry)it.next();
            if (pair.getValue() > max){
                max = pair.getValue();
                equalMoves.clear();
                equalMoves.add(pair.getKey());
            }else if (pair.getValue() == max){
                equalMoves.add(pair.getKey());
            }
            it.remove();
        }
        if (equalMoves.size() > 1){
            int randomIndex = random.nextInt(equalMoves.size());
            return equalMoves.get(randomIndex);
        }
        return equalMoves.get(0);
    }

    /**
     * The Minimax algorithm helps to find the best move, by working backwards from the end of the game.
     * At each step it assumes that player A is trying to maximize the chances of A winning, while on
     * the next turn player B is trying to minimize the chances of A winning.
     * @param depth number of immersion the algorithm will do
     * @param color color of the current player
     * @param handler current handler with registered game objects
     * @param i index relevant only for the first game phase,
     *          where it stands for the index of the current stone to be placed on board
     * @param phase current game phase
     * @return static evaluation of the played moves
     */
    private int minimax(int depth, COLOR color, Handler handler, int i, PHASE phase) {
        if (depth == 0) {
            int staticEvaluation = heuristic.evaluate(handler, aiColor, boardRows);
            if (mills > 0){
                staticEvaluation += mills;
                mills = 0;
            }else if (takenStones > 0){
                staticEvaluation -= takenStones * 2;
                takenStones = 0;
            }
            return staticEvaluation;
        }

        if (color == aiColor) {
            maxEval = Integer.MIN_VALUE;
            List<Move> possibleMoves = getPossibleMoves(handler, phase, color);
            for (Move move : possibleMoves) {
                Handler newHandler = new Handler();
                GameLogic.copyHandledObjects(handler, newHandler);
                gameSimulation(newHandler, phase, move, color, i);
                eval = minimax(depth - 1, GameLogic.changeColor(color), newHandler, ++i, phase);
                maxEval = Math.max(maxEval, eval);
                if (depth == this.depth) {
                    evalMoves.put(move, maxEval);
                }
                i--;
            }
            return maxEval;
        } else {
            minEval = Integer.MAX_VALUE;
            List<Move> possibleMoves = getPossibleMoves(handler, phase, color);
            for (Move move : possibleMoves) {
                Handler newHandler = new Handler();
                GameLogic.copyHandledObjects(handler, newHandler);
                gameSimulation(newHandler, phase, move, color, i);
                int eval = minimax(depth -1, GameLogic.changeColor(color), newHandler, ++i, phase);
                minEval = Math.min(minEval, eval);
                i--;
            }
            return minEval;
        }
    }
}
