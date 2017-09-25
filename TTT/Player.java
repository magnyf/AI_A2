import java.util.*;


public class Player {
    
        int firstPlayer;
        
    /**
     * Performs a move
     *
     * @param gameState
     *            the current state of the board
     * @param deadline
     *            time before which we must have returned
     * @return the next state the board is in after our move
     */
    public GameState play(final GameState gameState, final Deadline deadline) {
        Vector<GameState> nextStates = new Vector<GameState>();
        gameState.findPossibleMoves(nextStates);

        if (nextStates.size() == 0) {
            // Must play "pass" move if there are no other moves possible.
            return new GameState(gameState, new Move());
        }
             
        /**
         * Here you should write your algorithms to get the best next move, i.e.
         * the best next state. This skeleton returns a random move instead.
         */
        
        int scoreMax  = Integer.MIN_VALUE ; 
        
        //store the best move
        GameState bestState = nextStates.firstElement();
        firstPlayer = gameState.getNextPlayer();
        //depth of the algo
        int depth = 2;
        
        // Identification of the player,true if it is our turn false if it is the opponent turn.
        int alpha = Integer.MIN_VALUE;
        int beta = Integer.MAX_VALUE;
        
        
        long previousDeadLine = deadline.getCpuTime();
        while (deadline.timeUntil()>(previousDeadLine-deadline.getCpuTime())*(previousDeadLine-deadline.getCpuTime())){
            depth++;
            //System.err.println("La profondeur est de : " +depth);
            previousDeadLine = deadline.getCpuTime();
            
            for (int i = 0; i < nextStates.size(); i++){
                int score = alphaBeta(nextStates.get(i), depth-1,alpha, beta, nextStates.get(i).getNextPlayer());
                if ( score > scoreMax){
                    scoreMax = score;
                    bestState= nextStates.elementAt(i);
                }
            }  
      }
        System.err.println("Le score pour ce tour est de : " + scoreMax + " SCORE X : " + estimateCurrentScore(bestState, Constants.CELL_X) + " SCORE O : " + estimateCurrentScore(bestState, Constants.CELL_O));
        return bestState;
    }    
    
    public int alphaBeta(GameState gameState, int depth, int alpha, int beta, int player){
        Vector<GameState> nextStates = new Vector<GameState>();
        gameState.findPossibleMoves(nextStates);
        int score = 0;
      
        if (depth==0 || nextStates.size()==0){
            score = estimateCurrentScore (gameState,player);
            //System.err.println("Le score après estimation est de :" +score);
        } else if (player==firstPlayer){
            score = Integer.MIN_VALUE;
            for (int i = 0; i  < nextStates.size(); i++){
                score = max (score, alphaBeta(nextStates.get(i), depth-1, alpha, beta, nextStates.get(i).getNextPlayer()));
                alpha = max (alpha, score);
                if (beta <= alpha){
                    return beta;
                }
            }
        } else {
            score = Integer.MAX_VALUE;
            for(int i = 0; i < nextStates.size(); i++){
                score = min(score, alphaBeta(nextStates.get(i), depth-1, alpha, beta, nextStates.get(i).getNextPlayer()));
                beta = min (beta, score);
                if (beta <= alpha){
                    return alpha;
                }
            }
        }
        return score;
    }

    public int estimateCurrentScore(GameState gameState, int player){
        int score = 0;
        if (player == Constants.CELL_X){
            //System.err.println("on compte les points pour X?");
            if(gameState.isXWin()){
                return Integer.MAX_VALUE;
            }
            if(gameState.isOWin()){
                return Integer.MIN_VALUE;
            } 
        } else if (player==Constants.CELL_O){
            //System.err.println("on compte les points pour O?");
            if(gameState.isOWin()){
                return Integer.MAX_VALUE;
            }
            if(gameState.isXWin()){
                return Integer.MIN_VALUE;
            }
        }
        // count the point on the lines
            for(int i = 0; i < gameState.BOARD_SIZE; i++){
                int scoreLine = 0;
                for(int j = 0; j < gameState.BOARD_SIZE; j++){
                    int valueCase = gameState.at(i,j);
                    if (valueCase!= Constants.CELL_EMPTY){
                    if ( player == valueCase ){
                        if (scoreLine<0){
                            scoreLine = 0; break;
                        } else {
                            scoreLine ++;
                        }
                    } else {
                        if (scoreLine > 0){
                            scoreLine = 0; break;
                        } else {
                            scoreLine --;
                        }
                    }
                    }
                }
                score += scoreLine;
            }
            // count the point on the columns
                    
            for(int i = 0; i < gameState.BOARD_SIZE; i++){
                int scoreColumn = 0;
                for(int j = 0; j < gameState.BOARD_SIZE; j++){
                    int valueCase = gameState.at(i,j);
                    if(valueCase != Constants.CELL_EMPTY){                    
                    if ( player == valueCase){
                        if (scoreColumn<0){
                            scoreColumn = 0; break;
                        } else {
                            scoreColumn ++;
                        }
                    } else {
                        if (scoreColumn > 0){
                            scoreColumn = 0; break;
                        } else {
                            scoreColumn --;
                        }
                    }
                    }
                }
                score += scoreColumn;
            }
            // count the point on the diagonale
            int scoreDiag = 0;
            for(int i =0 ; i < gameState.BOARD_SIZE; i++){
                int valueCase = gameState.at(i,i);
                if (valueCase != Constants.CELL_EMPTY){
                if ( player == valueCase){
                        if (scoreDiag<0){
                            scoreDiag = 0; break;
                        } else {
                            scoreDiag ++;
                        }
                    } else {
                        if (scoreDiag > 0){
                            scoreDiag = 0; break;
                        } else {
                            scoreDiag --;
                        }
                    }
                }
            }
            score += scoreDiag;
            scoreDiag = 0; 
            for(int i =0 ; i < gameState.BOARD_SIZE; i++){
                int valueCase = gameState.at(gameState.BOARD_SIZE-i,gameState.BOARD_SIZE-i);
                if (valueCase != Constants.CELL_EMPTY){
                if ( player == valueCase){
                        if (scoreDiag<0){
                            scoreDiag = 0; break;
                        } else {
                            scoreDiag ++;
                        }
                    } else {
                        if (scoreDiag > 0){
                            scoreDiag = 0; break;
                        } else {
                            scoreDiag --;
                        }
                    }
                }
            }
            score += scoreDiag;
            return score;
        
    }
    
    public int max(int a , int b){
        if (a>b){
            return a ;
        }
        return b;
    }
    
    public int min(int a , int b){
        if (a > b ){
            return b;
        }
        return a ;
    }
}


