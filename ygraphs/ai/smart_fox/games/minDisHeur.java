package ygraphs.ai.smart_fox.games;

import java.util.LinkedList;
import java.util.Queue;

public class minDisHeur
{
	Tile[][] board = null;
	  GameRules bl = null;
	  Queen[] queens = new Queen[8];
	  public int ownedByThem = 0;
	  public int ownedByUs = 0;
	  
	public minDisHeur() 
	{
		board = null;
		bl = null;
		queens = new Queen[8];
		ownedByThem = 0;
		ownedByUs = 0;
	}
	  
	  public void calculate(GameRules b) {
	      bl = b;
	      board = bl.board;
	      // queens <- all queens (friendly and enemy)
	      for(int i=0; i<8; i++) {
	          if(i<4) {
	              queens[i]=b.getFriend()[i];
	          }
	          else {
	              queens[i]=b.getEnemy()[i-4];
	          }
	      }
	      ownedByUs=0;
	      ownedByThem=0;

	      // For every tile in the board
	      for(int i = 0; i <= 9; i++){
	          for(int j = 0; j <= 9; j++){
	              // If tile is empty, check who 'owns it'
	              if(bl.board[i][j] == null){
	                  findNearestQueen(i, j);
	              }
	          }
	      }
	  }
	  


	  public void findNearestQueen(int row, int col)
	  {
		boolean[][] checked = new boolean[10][10];
	    checked[row][col] = true;
	    boolean isFound = false;
	    
	    Queue<Tile> q = new LinkedList<Tile>();
	    q = addQueenMoves(q, row, col, checked);//all moves a queen could make to reach this tile
	    while (!isFound) {
	      Queue<Tile> tempQ = new LinkedList<Tile>();
	      int index = q.size();
	      if (index == 0) {//no valid moves, tile blocked in
	        isFound = true;
	        break;
	      }
	      for (int i = 0; i < index; i++) {//check every move possible from current tile
	        Tile currentTile = (Tile)q.poll();
	        
	        if(currentTile.row == 0 || currentTile.col == 0)
	        	continue;
	        
	        //current tile has queen here
	        if ((board[currentTile.row][currentTile.col] != null) && (board[currentTile.row][currentTile.col] instanceof Queen)) 
	        {
	          isFound = true;
	          boolean enemyQueen = ((Queen)board[currentTile.row][currentTile.col]).isOpponent;
	          boolean contested = false;
	          
	          for (Tile shell : q)//if queen found in q, queen is 1 move away, checks if opposing queens 1 move away 
	          {
	        	//opposing queen found 1 move away, tile contested
	            if ((board[shell.row][shell.col] != null) && (board[shell.row][shell.col] instanceof Queen)&& (((Queen)board[shell.row][shell.col]).isOpponent!=enemyQueen))
	              contested = true;
	          }
	          if (contested) {//owned by no one
	            break;
	          }
	          if (((Queen)board[currentTile.row][currentTile.col]).isOpponent) {
	            ownedByThem++;
	            break;
	          }
	          ownedByUs++;
	          break;
	        }
	        
	        checked[currentTile.row][currentTile.col] = true;
	        
	        if (board[currentTile.row][currentTile.col] == null) {//tile has no arrow or queen
	          tempQ = addQueenMoves(tempQ, row, col, checked);
	        }
	      }
	      q = tempQ;
	    }
	  }
	  

	  public Queue<Tile> addQueenMoves(Queue<Tile> q, int curRow, int curCol, boolean[][] checked)
	  {
	    for (int i = 0; curCol - i >= 0; i++) {
	      Tile lData = new Tile(curRow, curCol - i);
	      if (checked[curRow][(curCol - i)] == false) {
	        q.add(lData);
	      }
	      if (board[curRow][(curCol - i)] != null) {
	        break;
	      }
	    }
	    
	    for (int i = 0; (curRow - i >= 0) && (curCol - i >= 0); i++) {
	      Tile lData = new Tile(curRow - i, curCol - i);
	      if (checked[(curRow - i)][(curCol - i)] == false) {
	        q.add(lData);
	      }
	      if (board[(curRow - i)][(curCol - i)] != null) {
	        break;
	      }
	    }
	    
	    for (int i = 0; curRow - i >= 0; i++) {
	      Tile lData = new Tile(curRow - i, curCol);
	      if (checked[(curRow - i)][curCol] == false) {
	        q.add(lData);
	      }
	      if (board[(curRow - i)][curCol] != null) {
	        break;
	      }
	    }
	    
	    for (int i = 0; (curRow - i >= 0) && (curCol + i <= 9); i++) {
	      Tile lData = new Tile(curRow - i, curCol + i);
	      if (checked[(curRow - i)][(curCol + i)] == false) {
	        q.add(lData);
	      }
	      if (board[(curRow - i)][(curCol + i)] != null) {
	        break;
	      }
	    }
	    
	    for (int i = 0; curCol + i <= 9; i++) {
	      Tile lData = new Tile(curRow, curCol + i);
	      if (checked[curRow][(curCol + i)] == false) {
	        q.add(lData);
	      }
	      //System.out.println("Cur Col = " + curCol+" Cur Row = "+curRow+" I: "+i+" Board:"+board);
	      if (board[curRow][(curCol + i)] != null) {
	        break;
	      }
	    }
	    
	    for (int i = 0; (curRow + i <= 9) && (curCol + i <= 9); i++) {
	      Tile lData = new Tile(curRow + i, curCol + i);
	      if (checked[(curRow + i)][(curCol + i)] == false) {
	        q.add(lData);
	      }
	      if (board[(curRow + i)][(curCol + i)] != null) {
	        break;
	      }
	    }
	    
	    for (int i = 0; curRow + i <= 9; i++) {
	      Tile lData = new Tile(curRow + i, curCol);
	      if (checked[(curRow + i)][curCol] == false) {
	        q.add(lData);
	      }
	      if (board[(curRow + i)][curCol] != null) {
	        break;
	      }
	    }
	    
	    for (int i = 0; (curRow + i <= 9) && (curCol - i >= 0); i++) {
	      Tile lData = new Tile(curRow + i, curCol - i);
	      if (checked[(curRow + i)][(curCol - i)] == false) {
	        q.add(lData);
	      }
	      if (board[(curRow + i)][(curCol - i)] != null) {
	        break;
	      }
	    }
	    return q;
	  }
}
