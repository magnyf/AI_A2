import java.util.*;

public class Player {
    
    int firstPlayer;
    long timeLimit;
    long deadlineDeb;
    Vector<Vector<Integer>> linesPossible;
    
    
    public Player(){
           computePossibleLines();
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
/* 		for (int i=0; i < nextStates.size(); i++){
			System.err.print(estimateCurrentScore(nextStates.get(i), 0) + "  ");
		}
		System.err.println();
		Vector<GameState> v = triScore(nextStates);
		for (int i=0; i < nextStates.size(); i++){
			System.err.print(estimateCurrentScore(v.get(i), 0) + "  ");
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
        int depth = 0;

        // Identification of the player,true if it is our turn false if it is the opponent turn.
        timeLimit = deadline.timeUntil() - 7000000000L/200;   
        //System.err.println("timelimit : " + timeLimit);
        //System.err.println("deadline.timeUntil() : " + deadline.timeUntil());
	while (deadline.timeUntil()>timeLimit){
	    int alpha = Integer.MIN_VALUE;
            int beta = Integer.MAX_VALUE;

            depth++;
            
	    //  System.err.println("La profondeur est de : " +depth);
	    if (depth > 16){
		//break;
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
	/* System.err.println(" SCORE X : " + estimateCurrentScore(bestState, Constants.CELL_X) + " SCORE O : " + estimateCurrentScore(bestState, Constants.CELL_O) + " depth : " +depth);
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
        int score = 0;
	//        System.err.println("pour cette etape, il y a  : "+nextStates.size() + " d'etats possibles");
        if (depth==0 || nextStates.size()==0){
            score = estimateCurrentScore (gameState,player);
            //System.err.println("Le score après estimation est de :" +score);
        
        } else if (player==firstPlayer){
            score = Integer.MIN_VALUE;
            for (int i = 0; i  < nextStates.size(); i++){
                if (deadline.timeUntil() < timeLimit){
                    return 0;
                }
                score = max (score, alphaBeta(nextStates.get(i), depth-1, alpha, beta, nextStates.get(i).getNextPlayer(), deadline));
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
                score = min(score, alphaBeta(nextStates.get(i), depth-1, alpha, beta, nextStates.get(i).getNextPlayer(), deadline));
                beta = min (beta, score);
                if (beta <= alpha){
                    return beta;
                }
            }
        }
        return score;
    }

    public int estimateCurrentScore(GameState gameState, int player){
        if (firstPlayer == Constants.CELL_X){
            //System.err.println("on compte les points pour X?");
            if(gameState.isXWin()){
                return Integer.MAX_VALUE -1;
            }
            if(gameState.isOWin()){
                return Integer.MIN_VALUE +1;
            }
        } else if (firstPlayer==Constants.CELL_O){
            if(gameState.isOWin()){
                return Integer.MAX_VALUE -1;
            }
            if(gameState.isXWin()){
		return Integer.MIN_VALUE +1;
            }
        }
        return computeScore(gameState);
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

    public int calcValue(int score){
        if (score == 0){
            return 0;
        }
        if (score > 0){
            return (int) Math.pow(10,score);
        } else {
            return (int)-Math.pow(10, -score);
        }
    }


 
  // c'est une fonction qui prend en argument un Vector< GameState> et retourne un Vector<GameState> trié
  // les gameStates dans le retour sont triés par odre décoissant selon leur score avec l'appel à estimateCurrentScore
    public Vector<GameState> triScore(Vector<GameState> tab){
        Vector<GameState> v = new Vector<GameState>(tab);
        int i,j;
	for (i = 1; i < v.size(); ++i) {
            GameState elem = v.get(i);
            for (j = i; j > 0 && estimateCurrentScore(v.get(j-1), 0) < estimateCurrentScore(elem, 0); j--)
                v.set(j,  v.get(j-1));
            v.set(j, elem);
        }
        return v;
    }
    
    // compute the diff lines possible
    public void computePossibleLines(){
        linesPossible = new Vector<Vector<Integer>>();
        Vector<Integer> lineCour;
        // count the point on the lines on the same layer
        for(int i = 0; i < GameState.BOARD_SIZE; i++){
            for(int j = 0; j < GameState.BOARD_SIZE; j++){
                lineCour = new Vector<Integer> ();;
                for(int k = 0; k < GameState.BOARD_SIZE;k++){
                    lineCour.add(coordonnees(j,k,i));
                }
                linesPossible.add(lineCour);
            }
        }
        
	// count the point on the columns on the same layer
        for(int i = 0; i < GameState.BOARD_SIZE; i++){
            for(int j = 0; j < GameState.BOARD_SIZE; j++){
                lineCour = new Vector<Integer> ();;
                for(int k =0; k < GameState.BOARD_SIZE; k++){
                    lineCour.add(coordonnees(k,j,i));
                }
                linesPossible.add(lineCour);
            }
        }
        //cont the line on the same column
        for(int i = 0; i < GameState.BOARD_SIZE; i++){
            for(int j = 0; j < GameState.BOARD_SIZE; j++){
                lineCour = new Vector<Integer> ();
                for(int k =0; k < GameState.BOARD_SIZE; k++){
                    lineCour.add(coordonnees(i,j,k));
                }
                linesPossible.add(lineCour);
            }
        }
        // count the diag on the same layer
        for(int k = 0; k < GameState.BOARD_SIZE; k++){
            lineCour = new Vector<Integer> ();;
            for(int i =0 ; i < GameState.BOARD_SIZE; i++){
                lineCour.add(coordonnees(i,i, k));
            }
            linesPossible.add(lineCour);
            lineCour = new Vector<Integer> ();;
            for(int i =0 ; i < GameState.BOARD_SIZE; i++){
                lineCour.add(coordonnees(i,GameState.BOARD_SIZE-i-1, k));
            }
            linesPossible.add(lineCour);
        }
        //count the diag on the same rows
        for(int k = 0; k < GameState.BOARD_SIZE; k++){
            lineCour = new Vector<Integer> ();;
            for(int i =0 ; i < GameState.BOARD_SIZE; i++){
                lineCour.add(coordonnees(k,i, i));
            }
            linesPossible.add(lineCour);
            lineCour = new Vector<Integer> ();;
            for(int i =0 ; i < GameState.BOARD_SIZE; i++){
                lineCour.add(coordonnees(k, i,GameState.BOARD_SIZE-i-1));
            }
            linesPossible.add(lineCour);
        }
        //count the diag on the same columns
        for(int k = 0; k < GameState.BOARD_SIZE; k++){
            lineCour = new Vector<Integer> ();;
            for(int i =0 ; i < GameState.BOARD_SIZE; i++){
                lineCour.add(coordonnees(i,k,i));
            }
            linesPossible.add(lineCour);
            lineCour = new Vector<Integer> ();;
            for(int i =0 ; i < GameState.BOARD_SIZE; i++){
                lineCour.add(coordonnees(i,k,GameState.BOARD_SIZE-i-1));
            }
            linesPossible.add(lineCour);
        }
        // Compute the four major diagonale
        lineCour = new Vector<Integer> ();;
        for (int i = 0; i < GameState.BOARD_SIZE; i++){
            lineCour.add(coordonnees(i,i, i));
        }
                  linesPossible.add(lineCour);
        lineCour = new Vector<Integer> ();;
        for (int i = 0; i < GameState.BOARD_SIZE; i++){
            lineCour.add(coordonnees(i,GameState.BOARD_SIZE-1-i, GameState.BOARD_SIZE-i-1));
        }
        linesPossible.add(lineCour);
        lineCour = new Vector<Integer> ();;
        for (int i = 0; i < GameState.BOARD_SIZE; i++){
            lineCour.add(coordonnees(GameState.BOARD_SIZE-1-i,i, GameState.BOARD_SIZE-i-1));
        }
        linesPossible.add(lineCour);
        lineCour = new Vector<Integer> ();;
        for (int i = 0; i < GameState.BOARD_SIZE; i++){
            lineCour.add(coordonnees(GameState.BOARD_SIZE-1-i, GameState.BOARD_SIZE-i-1, i));
        }
        linesPossible.add(lineCour);
       /* 
        System.err.println("Il y a "+ linesPossible.size() + "lignes possibles." );
        for (int i = 0; i < linesPossible.size();i++){
            lineCour = linesPossible.get(i);
            System.err.print("la ligne est : ");
            for (int j = 0; j < lineCour.size(); j++){
                System.err.print("\t" + lineCour.get(j));
            }
            System.err.println();
            System.err.flush();
        }*/
    }

     public int coordonnees(int row, int column, int layer)
    {
        return column + row * GameState.BOARD_SIZE + layer * GameState.BOARD_SIZE * GameState.BOARD_SIZE;
    }
     
    public int computeScore(GameState gameState){
        Vector<Integer> lineCour;
        int score = 0;
        int scoreLine = 0;
        for (int i = 0; i < linesPossible.size(); i++){
            lineCour = linesPossible.elementAt(i);
            scoreLine = 0;
            
            for (int j = 0; j < GameState.BOARD_SIZE; j++){
                int valueCase = gameState.at(lineCour.elementAt(j));
                if (valueCase != Constants.CELL_EMPTY){
                    if ( firstPlayer == valueCase){
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
            score += calcValue(scoreLine);

        }
    /*    System.err.println("On étudie ce plateau :");
        System.err.println(gameState.toString(Constants.CELL_O));
        System.err.println("le score est de : " + score);
        System.err.println();
        System.err.println();
      */  
        return score;
    }
}