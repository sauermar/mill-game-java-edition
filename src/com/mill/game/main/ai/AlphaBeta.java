package com.mill.game.main.ai;

import com.mill.game.main.Board;
import com.mill.game.main.Handler;
import com.mill.game.main.common.ArtificialPlayerBase;
import com.mill.game.main.enums.COLOR;
import com.mill.game.main.enums.ID;
import com.mill.game.main.enums.PHASE;
import com.mill.game.main.helpers.GameLogic;
import com.mill.game.main.interfaces.ArtificialPlayer;
import com.mill.game.main.interfaces.Heuristic;
import com.mill.game.main.models.Move;

import java.util.*;

public class AlphaBeta extends ArtificialPlayerBase implements ArtificialPlayer {

    private final int depth = 4;
    private Heuristic heuristic;
    private HashMap<Move, Integer> evalMoves = new HashMap<Move, Integer>();
    private Random random = new Random();
    private int minEval, maxEval, eval;

    public AlphaBeta(Heuristic heuristic, Handler handler, Board board) {
        super(handler, board);
        this.heuristic = heuristic;
        aiColor = handler.getColorFromId(ID.AlphaBeta);
    }

    public Move move(PHASE phase, int i) {
        alphaBeta(Integer.MIN_VALUE, Integer.MAX_VALUE, depth, aiColor, handler, i, phase);
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

    private int alphaBeta(int alpha, int beta, int depth, COLOR color, Handler handler, int i, PHASE phase) {
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
                eval = alphaBeta(alpha, beta, depth - 1, GameLogic.changeColor(color), newHandler, ++i, phase);
                maxEval = Math.max(maxEval, eval);
                if (depth == this.depth) {
                    evalMoves.put(move, maxEval);
                }
                alpha = Math.max(alpha, eval);
                if (beta <= alpha)
                {
                    break;
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
                int eval = alphaBeta(alpha, beta, depth - 1, GameLogic.changeColor(color), newHandler, ++i, phase);
                minEval = Math.min(minEval, eval);
                beta = Math.min(beta, eval);
                if (beta <= alpha)
                {
                    break;
                }
                i--;
            }
            return minEval;
        }
    }
}
