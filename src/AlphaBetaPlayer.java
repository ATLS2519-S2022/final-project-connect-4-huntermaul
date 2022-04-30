public class AlphaBetaPlayer implements Player {
	int id; 
	int opponent_id;
    int cols; 
    
    @Override
    public String name() {
        return "Minnie";
    }

    @Override
    public void init(int id, int msecPerMove, int rows, int cols) {
    	this.id = id; //id is your player's id, opponent's id is 3-id
    	this.cols = cols;
    	opponent_id = 3-id;
    }

    @Override
    public void calcMove(
        Connect4Board board, int oppMoveCol, Arbitrator arb) 
        throws TimeUpException {
        // Make sure there is room to make a move.
        if (board.isFull()) {
            throw new Error ("Complaint: The board is full!");
        }
        
        int move = 0; 
        int maxDepth = 1;
        int score;
        
        // while there's time left and maxDepth <= number of moves remaining
        while(!arb.isTimeUp() && maxDepth <= board.numEmptyCells()) {
        	// do minimax search
        	// start the first level of minimax, set move as you're finding the bestScore
        	int bestScore = -1000;
        	
        	for (int col = 0; col < 7; col++) {
    			if(board.isValidMove(col)) {
    				board.move(col, id);
    				score = minimax(board, maxDepth-1, false, -1000, 1000, arb);
    				if (score > bestScore) {
    					bestScore = score;
    					move = col;
    				}
    				board.unmove(col,id);
    			}
    		}
            arb.setMove(move);
            maxDepth++;
        }        

    }
    
    public int minimax(Connect4Board board, int depth, boolean isMaximizing, int alpha, int beta, Arbitrator arb) {
    	
//    	if depth = 0 or there's no moves left or time is up
//    			return the heuristic value of node 
    	
    	if (depth == 0 || board.numEmptyCells() == 0 || arb.isTimeUp()) {
    		return score(board);
    	}
    	
//    	if isMaximizing then
//    			bestScore = -1000
//    			for each possible next move do
//					board.move(...)
//    				bestScore = Math.max(bestScore, minimax(child, depth - 1, FALSE)) 
//    				board.unmove(...)
//    			return bestScore
    	if (isMaximizing) {
    		int bestScore = -1000;
    		for (int col = 0; col < 7; col++) {
    			if(board.isValidMove(col)) {
    				board.move(col, id);
    				bestScore = Math.max(bestScore, minimax(board, depth-1, false, alpha, beta, arb));
    				alpha = Math.max(alpha, bestScore);
    				if (beta <= alpha) break;
    				board.unmove(col,id);
    			}
    		}
    		return bestScore;
    	}
    	
//    	else /* minimizing player */ 
//    			bestScore = 1000
//    			for each possible next move do
//					board.move(...)    	
//    				bestScore = Math.min(bestScore, minimax(child, depth - 1, TRUE)) 
//					board.unmove(...)
//    			return bestScore	
    	
    	else {
    		int bestScore = 1000;
    		for (int col = 0; col < 7; col++) {
    			if(board.isValidMove(col)) {
    				board.move(col, opponent_id);
    				bestScore = Math.min(bestScore, minimax(board, depth-1, true, alpha, beta, arb));
    				beta = Math.min(beta, bestScore);
    				if (beta <= alpha) break;
    				board.unmove(col,opponent_id);
    			}
    		}
    		return bestScore;
    	}
    }
    
    // your score - opponent's score
    public int score(Connect4Board board) {
    	return calcScore(board, id) - calcScore(board, opponent_id);
    }
    
    
	// Return the number of connect-4s that player #id has.
	public int calcScore(Connect4Board board, int id)
	{
		final int rows = board.numRows();
		final int cols = board.numCols();
		int score = 0;
		// Look for horizontal connect-4s.
		for (int r = 0; r < rows; r++) {
			for (int c = 0; c <= cols - 4; c++) {
				if (board.get(r, c + 0) != id) continue;
				if (board.get(r, c + 1) != id) continue;
				if (board.get(r, c + 2) != id) continue;
				if (board.get(r, c + 3) != id) continue;
				score++;
			}
		}
		// Look for vertical connect-4s.
		for (int c = 0; c < cols; c++) {
			for (int r = 0; r <= rows - 4; r++) {
				if (board.get(r + 0, c) != id) continue;
				if (board.get(r + 1, c) != id) continue;
				if (board.get(r + 2, c) != id) continue;
				if (board.get(r + 3, c) != id) continue;
				score++;
			}
		}
		// Look for diagonal connect-4s.
		for (int c = 0; c <= cols - 4; c++) {
			for (int r = 0; r <= rows - 4; r++) {
				if (board.get(r + 0, c + 0) != id) continue;
				if (board.get(r + 1, c + 1) != id) continue;
				if (board.get(r + 2, c + 2) != id) continue;
				if (board.get(r + 3, c + 3) != id) continue;
				score++;
			}
		}
		for (int c = 0; c <= cols - 4; c++) {
			for (int r = rows - 1; r >= 4 - 1; r--) {
				if (board.get(r - 0, c + 0) != id) continue;
				if (board.get(r - 1, c + 1) != id) continue;
				if (board.get(r - 2, c + 2) != id) continue;
				if (board.get(r - 3, c + 3) != id) continue;
				score++;
			}
		}
		return score;
	}

}
