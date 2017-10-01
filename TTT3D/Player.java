import java.util.*;

public class Player {
    
    int firstPlayer;
    long timeLimit;
    long deadlineDeb;
    
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
        int depth = 1;

        // Identification of the player,true if it is our turn false if it is the opponent turn.
        int alpha = Integer.MIN_VALUE;
        int beta = Integer.MAX_VALUE;
        timeLimit = deadline.timeUntil() - 7000000000L/225;   
        //System.err.println("timelimit : " + timeLimit);
        //System.err.println("deadline.timeUntil() : " + deadline.timeUntil());
	while (deadline.timeUntil()>timeLimit){
	    depth++;
            
	    //  System.err.println("La profondeur est de : " +depth);
	    if (depth > 16){
		//break;
	    }

	    for (int i = 0; i < nextStates.size(); i++){
		int score = alphaBeta(nextStates.get(i), depth-1,alpha, beta, nextStates.get(i).getNextPlayer(), deadline);
		if ( score > scoreMax){
		    scoreMax = score;
		    if( deadline.timeUntil()>timeLimit){
			bestState= nextStates.elementAt(i);
		    }
		}
	    }
        }
	/* System.err.println(" SCORE X : " + estimateCurrentScore(bestState, Constants.CELL_X) + " SCORE O : " + estimateCurrentScore(bestState, Constants.CELL_O) + " depth : " +depth);
	   System.err.println();
        
        
        
        
        
	   System.err.println("MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM");
	   System.err.println("MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM");
	   System.err.println("MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM");
	   System.err.println("MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM");
	   System.err.println("MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM");
	   System.err.println("MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM");
	   System.err.println("MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM");
	   System.err.println("MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM");
	   System.err.println("MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM");
	   System.err.println("MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM");
	   System.err.println("MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM");
	   System.err.println("MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM");
	   System.err.println("MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM");
	   System.err.println("MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM");
	   System.err.println("MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM");
	   System.err.println("MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM");
	   System.err.println("MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM");
	   System.err.println("MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM");
	   System.err.println("MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM");
	   System.err.println("MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM");
	   System.err.println("MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM");
	   System.err.println("MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM");
	   System.err.println("MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM");
	   System.err.println("MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM");
	   System.err.println("MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM");
	   System.err.println("MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM");
	   System.err.println("MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM");
	   System.err.println("MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM");
	   System.err.println("MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM");
	   System.err.println("MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM");
	   System.err.println("MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM");
	   System.err.println("MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM");
	   System.err.println("MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM");
	   System.err.println("MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM");
	   System.err.println("MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM");
	   System.err.println("MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM");
        */return bestState;    
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
                    return beta;
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
                    return alpha;
                }
            }
        }
        return score;
    }

    public int estimateCurrentScore(GameState gameState, int player){
        int score = 0;

        if (firstPlayer == Constants.CELL_X){
            //System.err.println("on compte les points pour X?");
            if(gameState.isXWin()){
		/*               System.err.println("POUR LE GAMESTATE SUIVANT : ");
				 System.err.println(gameState.toString(Constants.CELL_X));
				 System.err.println( " le score de x est de :" + Integer.MAX_VALUE);
				 System.err.println();
				 System.err.println("GGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGG");
				 System.err.println("GGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGG");
				 System.err.println("GGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGG");
				 System.err.println("GGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGG");
				 System.err.println("GGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGG");
				 System.err.println("GGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGG");
				 System.err.println("GGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGG");
				 System.err.println("GGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGG");
				 System.err.println("GGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGG");
				 System.err.println("GGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGG");
		*/
                return Integer.MAX_VALUE;
            }
            if(gameState.isOWin()){
		/*      System.err.println("POUR LE GAMESTATE SUIVANT : ");
			System.err.println(gameState.toString(Constants.CELL_X));
			System.err.println( " le score de x est de :" + Integer.MIN_VALUE);
			System.err.println();
                        System.err.println("GGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGG");
			System.err.println("GGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGG");
			System.err.println("GGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGG");
			System.err.println("GGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGG");
			System.err.println("GGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGG");
			System.err.println("GGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGG");
			System.err.println("GGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGG");
			System.err.println("GGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGG");
			System.err.println("GGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGG");
			System.err.println("GGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGG");
			System.err.println("GGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGG");
			System.err.println("GGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGG");
			System.err.println("GGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGG");
		*/
                return Integer.MIN_VALUE;
            }
        } else if (firstPlayer==Constants.CELL_O){
            //System.err.println("on compte les points pour O?");
            if(gameState.isOWin()){
		/*              System.err.println("POUR LE GAMESTATE SUIVANT : ");
				System.err.println(gameState.toString(Constants.CELL_X));
				System.err.println( " le score de x est de :" + Integer.MAX_VALUE);
				System.err.println();
				System.err.println("GGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGG");
				System.err.println("GGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGG");
				System.err.println("GGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGG");
				System.err.println("GGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGG");
				System.err.println("GGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGG");
				System.err.println("GGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGG");
				System.err.println("GGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGG");
				System.err.println("GGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGG");
				System.err.println("GGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGG");
				System.err.println("GGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGG");
                */return Integer.MAX_VALUE;
            }
            if(gameState.isXWin()){
		/*     System.err.println("POUR LE GAMESTATE SUIVANT : ");
		       System.err.println(gameState.toString(Constants.CELL_X));
		       System.err.println( " le score de x est de :" + Integer.MIN_VALUE);
		       System.err.println();
		       System.err.println("GGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGG");
		       System.err.println("GGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGG");
		       System.err.println("GGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGG");
		       System.err.println("GGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGG");
		       System.err.println("GGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGG");
		       System.err.println("GGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGG");
		       System.err.println("GGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGG");
		       System.err.println("GGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGG");
		       System.err.println("GGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGG");
		       System.err.println("GGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGG");
		       System.err.println("GGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGG");
		       System.err.println("GGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGG");
		       System.err.println("GGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGG");
		       System.err.println("GGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGG");
		       System.err.println("GGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGG");
		       System.err.println("GGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGG");
		       System.err.println("GGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGG");
		       System.err.println("GGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGG");
		*/ return Integer.MIN_VALUE;
            }
        }

	// count the point on the lines on the same layer
        for(int i = 0; i < gameState.BOARD_SIZE; i++){
            for(int j = 0; j < gameState.BOARD_SIZE; j++){
                int scoreLine = 0;
                //System.err.print("on check les case : ");
                for(int k = 0; k < gameState.BOARD_SIZE;k++){
                    int valueCase = gameState.at(j,k,i);
                    //System.err.print("\t" + gameState.rowColumnLayerToCell(j,k,i));
                    if (valueCase!= Constants.CELL_EMPTY){
                        if ( firstPlayer == valueCase ){
                            if (scoreLine < 0){
                                scoreLine = 0;
                                break;
                            } else {
                                scoreLine++ ;
                            }
                        } else {
                            if (scoreLine > 0){
                                scoreLine = 0;
                                break;
                            } else {
                                scoreLine --;
                            }
                        }
                    }
                }
                //System.err.println();
                score += calcValue(scoreLine);
            }
        }
	// count the point on the columns on the same layer
        //System.err.println();
        //System.err.println();

        for(int i = 0; i < gameState.BOARD_SIZE; i++){
            for(int j = 0; j < gameState.BOARD_SIZE; j++){
                int scoreColumn = 0;
		//    System.err.print("on check les case : ");   
                for(int k =0; k < gameState.BOARD_SIZE; k++){
                    int valueCase = gameState.at(k,j,i);
		    //                    System.err.print("\t" + gameState.rowColumnLayerToCell(k,j,i));
                    if(valueCase != Constants.CELL_EMPTY){
                        if ( firstPlayer == valueCase){
                            if (scoreColumn < 0){
                                scoreColumn = 0;
                                break;
                            } else {
                                scoreColumn ++;
                            }
                        } else {
                            if (scoreColumn > 0){
                                scoreColumn = 0;
                                break;
                            } else {
                                scoreColumn --;
                            }
                        }
                    }
                }
		//          System.err.println();
		score += calcValue(scoreColumn);
            }
        }
	//    System.err.println();
	//  System.err.println();

        //cont the line on the same column
        for(int i = 0; i < gameState.BOARD_SIZE; i++){
            for(int j = 0; j < gameState.BOARD_SIZE; j++){
		//               System.err.print("on check les case : ");   
                int scoreColumn = 0;
                for(int k =0; k < gameState.BOARD_SIZE; k++){
                    int valueCase = gameState.at(i,j,k);
		    //                  System.err.print("\t" + gameState.rowColumnLayerToCell(i,j,k));
                    if(valueCase != Constants.CELL_EMPTY){
                        if ( firstPlayer == valueCase){
                            if (scoreColumn < 0){
                                scoreColumn = 0;
                                break;
                            } else {
                                scoreColumn ++;
                            }
                        } else {
                            if (scoreColumn > 0){
                                scoreColumn = 0;
                                break;
                            } else {
                                scoreColumn --;
                            }
                        }
                    }
                }
		//             System.err.println();
                score += calcValue(scoreColumn);
            }
        }
	//     System.err.println();
	//      System.err.println();

        
        int scoreDiag = 0;
        // count the diag on the same layer
        for(int k = 0; k < gameState.BOARD_SIZE; k++){
            scoreDiag = 0;
	    //        System.err.print("on check les case : ");   
            for(int i =0 ; i < gameState.BOARD_SIZE; i++){
                int valueCase = gameState.at(i,i, k);
		//            System.err.print("\t" + gameState.rowColumnLayerToCell(i,i,k));
                if (valueCase != Constants.CELL_EMPTY){
                    if ( firstPlayer == valueCase){
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
            score += calcValue(scoreDiag);
	    //         System.err.println();

            scoreDiag = 0;
	    //       System.err.print("on check les case : ");   
            for(int i =0 ; i < gameState.BOARD_SIZE; i++){
                int valueCase = gameState.at(i,gameState.BOARD_SIZE-i-1, k);
		//         System.err.print("\t" + gameState.rowColumnLayerToCell(i, gameState.BOARD_SIZE-i-1, k));
                if (valueCase != Constants.CELL_EMPTY){
                    if ( firstPlayer == valueCase){
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
	    //   System.err.println();
            score += calcValue(scoreDiag);
        }
        //System.err.println();
	//        System.err.println();

        //count the diag on the same rows
        for(int k = 0; k < gameState.BOARD_SIZE; k++){
            scoreDiag = 0;
	    //          System.err.print("on check les case : ");   
            for(int i =0 ; i < gameState.BOARD_SIZE; i++){
		//            System.err.print("\t" + gameState.rowColumnLayerToCell(k,i,i));
                int valueCase = gameState.at(k,i, i);
                if (valueCase != Constants.CELL_EMPTY){
                    if ( firstPlayer == valueCase){
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
	    //      System.err.println();
            score += calcValue(scoreDiag);
            scoreDiag = 0;
	    //    System.err.print("on check les case : ");   
            for(int i =0 ; i < gameState.BOARD_SIZE; i++){
		//      System.err.print("\t" + gameState.rowColumnLayerToCell(k, i,gameState.BOARD_SIZE-i-1));
                int valueCase = gameState.at(k, i,gameState.BOARD_SIZE-i-1);
                if (valueCase != Constants.CELL_EMPTY){
                    if ( firstPlayer == valueCase){
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
	    //            System.err.println();
            score += calcValue(scoreDiag);
        }
	//      System.err.println();
	//    System.err.println();

        //count the diag on the same columns
        for(int k = 0; k < gameState.BOARD_SIZE; k++){
	    //      System.err.print("on check les case : ");   
            scoreDiag = 0;
            for(int i =0 ; i < gameState.BOARD_SIZE; i++){
                int valueCase = gameState.at(i,k,i);
		//        System.err.print("\t" + gameState.rowColumnLayerToCell(i,k,i));
                if (valueCase != Constants.CELL_EMPTY){
                    if ( firstPlayer == valueCase){
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
            score += calcValue(scoreDiag);
	    //  System.err.println();
            scoreDiag = 0;
            //System.err.print("on check les case : ");   
            for(int i =0 ; i < gameState.BOARD_SIZE; i++){
                int valueCase = gameState.at(i,k,gameState.BOARD_SIZE-i-1);
		//  System.err.print("\t" + gameState.rowColumnLayerToCell(i,k,gameState.BOARD_SIZE-i-1));
                if (valueCase != Constants.CELL_EMPTY){
                    if ( firstPlayer == valueCase){
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
	    //            System.err.println();
            score += calcValue(scoreDiag);
        }
        scoreDiag = 0;
	//      System.err.println();
	//        System.err.println();

        // Compute the four major diagonale
	//      System.err.print("on check les case : ");   
        for (int i = 0; i < gameState.BOARD_SIZE; i++){
            int valueCase = gameState.at(i,i, i);
	    //        System.err.print("\t" + gameState.rowColumnLayerToCell(i,i,i));
            if (valueCase != Constants.CELL_EMPTY){
                if ( firstPlayer == valueCase){
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
	//  System.err.println();
        score += calcValue(scoreDiag);
        
        //System.err.print("on check les case : ");   
        scoreDiag = 0;
        for (int i = 0; i < gameState.BOARD_SIZE; i++){
            int valueCase = gameState.at(i,gameState.BOARD_SIZE-1-i, gameState.BOARD_SIZE-i-1);
	    //  System.err.print("\t" + gameState.rowColumnLayerToCell(i,gameState.BOARD_SIZE-1-i, gameState.BOARD_SIZE-i-1));
            if (valueCase != Constants.CELL_EMPTY){
                if ( firstPlayer == valueCase){
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
	//        System.err.println();
        score += calcValue(scoreDiag);
        
	//      System.err.print("on check les case : ");   
        scoreDiag = 0;
        for (int i = 0; i < gameState.BOARD_SIZE; i++){
            int valueCase = gameState.at(gameState.BOARD_SIZE-1-i,i, gameState.BOARD_SIZE-i-1);
	    //        System.err.print("\t" + gameState.rowColumnLayerToCell(gameState.BOARD_SIZE-1-i,i, gameState.BOARD_SIZE-i-1));
            if (valueCase != Constants.CELL_EMPTY){
                if ( firstPlayer == valueCase){
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
        
	//  System.err.println();
        score += calcValue(scoreDiag);
        
        //System.err.print("on check les case : ");   
        scoreDiag = 0;
        for (int i = 0; i < gameState.BOARD_SIZE; i++){
            int valueCase = gameState.at(gameState.BOARD_SIZE-1-i, gameState.BOARD_SIZE-i-1, i);
	    //  System.err.print("\t" + gameState.rowColumnLayerToCell(gameState.BOARD_SIZE-1-i, gameState.BOARD_SIZE-i-1, i));
            if (valueCase != Constants.CELL_EMPTY){
                if ( firstPlayer == valueCase){
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
	//        System.err.println();       
	//      System.err.println();
	//    System.err.println();
	//  System.err.println();
        //System.err.println();
        //System.err.println();
	/*  score += calcValue(scoreDiag);
	    System.err.println("POUR LE GAMESTATE SUIVANT : ");
	    System.err.println(gameState.toString(Constants.CELL_X));
	    System.err.println( " le score de x est de :" + score);
	    System.err.println();
        */
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
    
}