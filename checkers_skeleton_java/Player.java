import java.util.*;

public class Player {
    
    int firstPlayer;
    long timeLimit;
    
    
    public Player(){
    }
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
/*      for (int i=0; i < nextStates.size(); i++){
            System.err.print(estimateCurrentScore(nextStates.get(i)) + "  ");
        }
        System.err.println();
        Vector<GameState> v = triScore(nextStates);
        for (int i=0; i < nextStates.size(); i++){
            System.err.print(estimateCurrentScore(v.get(i)) + "  ");
        }
        System.err.println(); */
        
        
        if (nextStates.size() == 0) {
            // Must play "pass" move if there are no other moves possible.
            return new GameState(gameState, new Move());
        }

        int scoreMax  = Integer.MIN_VALUE ;

        //store the best move
        GameState bestState = nextStates.firstElement();
        firstPlayer = gameState.getNextPlayer();
        
        //depth of the algo
        int depth = 5;

        // Identification of the player,true if it is our turn false if it is the opponent turn.
        timeLimit = 20000000;//deadline.timeUntil() - 7000000000L/150;   
        //System.err.println("timelimit : " + timeLimit);
        //System.err.println("deadline.timeUntil() : " + deadline.timeUntil());
        triScore(nextStates, deadline);
        while (deadline.timeUntil()>timeLimit){
        int alpha = Integer.MIN_VALUE;
            int beta = Integer.MAX_VALUE;

            depth++;
            
        //  System.err.println("La profondeur est de : " +depth);
        if (depth > gameState.getMovesUntilDraw()){
        break;
        }

            GameState bestCurrentState = new GameState();
            int currentScoreMax = Integer.MIN_VALUE;
        for (int i = 0; i < nextStates.size(); i++){
                if (deadline.timeUntil() < timeLimit){
                    break;
                }
        int score = alphaBeta(nextStates.get(i), depth-1,alpha, beta, nextStates.get(i).getNextPlayer(), deadline);
        if ( score > currentScoreMax){
            currentScoreMax = score;
                    bestCurrentState= nextStates.elementAt(i);
        }
        }
            if (deadline.timeUntil() > timeLimit){
                bestState = bestCurrentState;
                scoreMax = currentScoreMax;
            }            
        }
    /* System.err.println(" SCORE X : " + estimateCurrentScore(bestState) + " SCORE O : " + estimateCurrentScore(bestState) + " depth : " +depth);
        */
        System.err.println("la profondeur finale est de : " + depth);
        System.err.println("Le score max est de : " + scoreMax);
        return bestState;    
    }

    public int alphaBeta(GameState gameState, int depth, int alpha, int beta, int player, Deadline deadline){

        if (deadline.timeUntil() < timeLimit){
            return 0;
        }
        Vector<GameState> nextStates = new Vector<GameState>();
        gameState.findPossibleMoves(nextStates);
        triScore(nextStates, deadline);
        //if(depth == 1){
         //   return estimateCurrentScore(nextStates.get(0));
       // }
        
        int score = 0;
    //        System.err.println("pour cette etape, il y a  : "+nextStates.size() + " d'etats possibles");
        if (depth==0 || nextStates.size()==0){
            score = estimateCurrentScore (gameState);
            //System.err.println("Le score après estimation est de :" +score);
        
        } else if (player==firstPlayer){
            score = Integer.MIN_VALUE;
            for (int i = 0; i  < nextStates.size(); i++){
                if (deadline.timeUntil() < timeLimit){
                    return 0;
                }
                int result = 0;
                if (nextStates.get(i).getMove().isJump()){
                    result = alphaBeta(nextStates.get(i), depth, alpha, beta, nextStates.get(i).getNextPlayer(), deadline);
                } else{
                    result = alphaBeta(nextStates.get(i), depth-1, alpha, beta, nextStates.get(i).getNextPlayer(), deadline);

                } 
                score = max (score,result);
                alpha = max (alpha, score);
                if (beta <= alpha){
                    return alpha;
                }
            }
        } else {
            score = Integer.MAX_VALUE;
            for(int i = 0; i < nextStates.size(); i++){
                if ( deadline.timeUntil() < timeLimit){
                    return 0;
                }
                int result = 0;
                if (nextStates.get(i).getMove().isJump()){
                    result = alphaBeta(nextStates.get(i), depth, alpha, beta, nextStates.get(i).getNextPlayer(), deadline);
                } else{
                    result = alphaBeta(nextStates.get(i), depth-1, alpha, beta, nextStates.get(i).getNextPlayer(), deadline);

                } 
                score = min(score,result);
                beta = min (beta, score);
                if (beta <= alpha){
                    return beta;
                }
            }
        }
        return score;
    }

    public int estimateCurrentScore(GameState gameState){
        if (firstPlayer == Constants.CELL_RED){
            //System.err.println("on compte les points pour X?");
            if(gameState.isRedWin()){
                return Integer.MAX_VALUE -1;
            }
            if(gameState.isWhiteWin()){
                return Integer.MIN_VALUE +1;
            }
        } else if (firstPlayer==Constants.CELL_WHITE){
            if(gameState.isWhiteWin()){
                return Integer.MAX_VALUE -1;
            }
            if(gameState.isRedWin()){
        return Integer.MIN_VALUE +1;
            }
        }
        int score = 0;
        int nbPionsPlayer = 0;
        int nbOtherPions = 0;
        for (int i = 0 ; i < gameState.NUMBER_OF_SQUARES; i++){
            int valueCase = gameState.get(i);
            if(valueCase>0){
                if ((valueCase & firstPlayer) > 0){
                    if ((valueCase & Constants.CELL_KING) > 0){
                        nbPionsPlayer += 2;
                    } else {
                        nbPionsPlayer++;
                    }
                } else {
                    if ((valueCase & Constants.CELL_KING) > 0){
                        nbOtherPions -= 2;
                    } else {
                        nbOtherPions--;
                    }
                }
            }    
        }
        score =  nbPionsPlayer + nbOtherPions;
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

  // c'est une fonction qui prend en argument un Vector< GameState> et retourne un Vector<GameState> trié
  // les gameStates dans le retour sont triés par odre décoissant selon leur score avec l'appel à estimateCurrentScore
    public void triScore(Vector<GameState> tab, Deadline deadline){
        if (tab.size() < 2) {
            return;
        }
        int[] score = new int[tab.size()];
        for (int i = 0 ; i < tab.size(); i++){
            score[i] = (estimateCurrentScore(tab.get(i)));
        }
        int player = tab.get(0).getNextPlayer();
        int j;
        if(player == firstPlayer){
            for (int i = 1; i < tab.size(); ++i) {
                if (deadline.timeUntil() < timeLimit){
                    return;
                }
                GameState elem = tab.get(i);
                int s = score[i];
                for (j = i; j > 0 && score[j-1] < s; j--){
                    tab.set(j,  tab.get(j-1));
                    tab.set(j-1, elem);
                    score[j] = score[j-1];
                    score[j-1] = s;
                }
            }
        } else {
            for (int i = 1; i < tab.size(); ++i) {
                if (deadline.timeUntil() < timeLimit){
                    return;
                }
                GameState elem = tab.get(i);
                int s = score[i];
                for (j = i; j > 0 && score[j-1] > s; j--){
                    tab.set(j,  tab.get(j-1));
                    tab.set(j-1, elem);
                    score[j] = score[j-1];
                    score[j-1] = s;
                }
            }
        }
    }  
}

