package com.androidgames.tetris;

import java.util.Random;

public class World {
    static final int WORLD_WIDTH = 10;
    static final int WORLD_HEIGHT = 15;
    static final int SCORE_INCREMENT = 10;
    static final float TICK_INITIAL = 0.5f;
    static final float TICK_DECREMENT = 0.05f;

    public boolean gameOver = false;
    public int score = 0;
    public int level = 0;

    boolean fields[][] = new boolean[WORLD_HEIGHT][WORLD_WIDTH];
    Random random = new Random();
    float tickTime = 0;
    float tick = TICK_INITIAL;
    
    Block block;
    
    public class Block {
	    boolean size[][] = new boolean[4][4];
	    public int row;
	    public int col;
	    
	    public Block() {
	    	row = 0;
	    	col = 0;
	    }
	    
	    public void NewBlock()
	    {
	    	int newblock;
	    	int i,j;
	
	    	for (i = 0; i < 4; i++)
	    		for (j = 0; j < 4; j++)
	    			size[i][j] = false;
	
	    	col = 3;
	    	row = 0;
	
	    	newblock = random.nextInt(7);
	
			switch (newblock)
			{
				case 0:
					size[1][0] = true;
					size[1][1] = true;
					size[1][2] = true;
					size[1][3] = true;
					row = 0;
					break;
				case 1:
					size[1][1] = true;
					size[1][2] = true;
					size[2][1] = true;
					size[2][2] = true;
					row=0;
					break;
				case 2:
					size[1][1] = true;
					size[0][2] = true;
					size[1][2] = true;
					size[2][2] = true;
					row = 0;
					break;
				case 3:
					size[0][1] = true;
					size[1][1] = true;
					size[1][2] = true;
					size[2][2] = true;
					row = 0;
					break;
	
				case 4:
					size[2][1] = true;
					size[1][1] = true;
					size[1][2] = true;
					size[0][2] = true;
					row = 0;
					break;
				case 5:
					size[1][1] = true;
					size[2][1] = true;
					size[2][2] = true;
					size[2][3] = true;
					row = 0;
					break;
				case 6:
					size[2][1] = true;
					size[1][1] = true;
					size[1][2] = true;
					size[1][3] = true;
					row = 0;
					break;
			}
	    }
	    
	    public void RotateBlock()
	    {
	    	int i,j;
	
	    	boolean temp[][] = new boolean[4][4];
	    	
	    	//rotate block
	    	for (i = 0; i < 4; i++)
	    		for (j = 0; j < 4; j++)
	    			temp[j][3-i] = size[i][j];
	    	
	    	//test the two edges left and right
	    	for (i = 0; i < 4; i++)
	    		for (j = 0; j < 4; j++)
	    			if (temp[i][j] == true)
	    				if (col + j < 0||col + j > WORLD_WIDTH-1||row + i < 0||row + i > WORLD_HEIGHT-1)
	    					return;
	
	    	//test the other edges
	    	for (i = 0; i < WORLD_HEIGHT; i++)
	    		for (j = 0; j < WORLD_WIDTH; j++)
	    		if (i >= row && i < row + 4)
	    				if (j >= col && j < col + 4)
	    					if (fields[i][j] == true)
	    						if (temp[i-row][j-col] == true)
	    							return;
	
	    	//successful
	    	for (i = 0; i < 4; i++)
	    		for (j = 0; j < 4; j++)
	    			size[i][j] = temp[i][j];
	    }
	    
	    public void Move(int col, int row)
	    {
	    	if (CollisionTest(col,row))
	    	{
	    		if (row == 1)
	    		{
	    			if (this.row < 1)
	    			{
	    				//gameOver
	    				gameOver = true;
	    			}
	    			else
	    			{
	    				boolean killblock = false;
	    				int i,j;
	    				for (i = 0; i < 4; i++)
	    					for (j = 0; j < 4; j++)
	    						if (this.size[i][j] == true)
	    							fields[this.row+i][this.col+j] = true;
	
	    				//test to clear rows
	    				for (i = 0; i < WORLD_HEIGHT; i++)
	    				{
	    					boolean filled = true;
	    					for (j = 0; j < WORLD_WIDTH;j++)
	    						if (fields[i][j] == false)
	    							filled = false;
	    					if (filled)
	    					{
	    						RemoveRow(i);
	    						score += 1;
	    						level = score/10+1;
	    						
	    			            if (score % 10 == 0 && tick - TICK_DECREMENT > 0) {
	    			            	tick = tick - level*TICK_DECREMENT;
	    			            }
	    						
	    						killblock = true;
	    					}
	    				}
	
	    				if (killblock)
	    				{
	         				for (int m = 0; m < 4; m++)
	    						for (int n = 0; n < 4; n++)
	    							this.size[m][n] = false;
	    				}
	    				NewBlock();
	    			}
	    		}
	    	}
	    	else
	    	{
	    		this.col += col;
	    		this.row +=row;
	    	}
	    }
	
	    public boolean CollisionTest(int col, int row)
	    {
	    	int i,j;
	        int x,y;
	
	    	int newcol = this.col + col;
	    	int newrow = this.row + row;
	    	
	    	//test four edges
	    	for (i = 0; i < 4; i++)
	    		for (j = 0; j < 4; j++)
	    			if (this.size[i][j] == true)
	    				if (newcol+j<0||newcol+j>WORLD_WIDTH-1||newrow+i<0||newrow+i>WORLD_HEIGHT-1)
	    					return true;
	    	
	    	//test the other blocks
	    	for (x = 0; x < WORLD_HEIGHT; x++)
	    		for (y = 0;y < WORLD_WIDTH; y++)
	    			if (x>=newrow && x<newrow+4)
	    				if (y>=newcol && y<newcol+4)
	    					if (fields[x][y] == true)
	    						if (this.size[x-newrow][y-newcol] == true)
	    							return true;
	
	    	return false;
	    }
	
	    public void RemoveRow(int row)
	    {
	    	int i,j;
	    	for (i = row; i > 0; i--)
	    		for (j = 0; j < WORLD_WIDTH; j++)
	    			fields[i][j] = fields[i-1][j];
	    }
    }//end class Block

    public World() {
    	block = new Block();
    	block.NewBlock();
    }

    public void update(float deltaTime) {
        if (gameOver)
            return;

        tickTime += deltaTime;
        
        while (tickTime > tick) {
            tickTime -= tick;
            block.Move(0, 1);
        }
    }
}
