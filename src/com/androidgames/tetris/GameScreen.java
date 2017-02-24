package com.androidgames.tetris;

import java.util.List;
import java.util.concurrent.TimeUnit;

import android.graphics.Color;

import com.androidgames.framework.Game;
import com.androidgames.framework.Graphics;
import com.androidgames.framework.Input.TouchEvent;
import com.androidgames.framework.Pixmap;
import com.androidgames.framework.Screen;


public class GameScreen extends Screen {
    enum GameState {
        Ready,
        Running,
        Paused,
        GameOver
    }
    
    static final float MOVE_DELAY = 0.125f;
    
    float moveDelay = 0;
    
    GameState state = GameState.Ready;
    World world;
    int score = 0;
    
    public GameScreen(Game game) {
        super(game);
        world = new World();
    }

    @Override
    public void update(float deltaTime) {
        List<TouchEvent> touchEvents = game.getInput().getTouchEvents();
        game.getInput().getKeyEvents();
        
        if(state == GameState.Ready)
            updateReady(touchEvents);
        if(state == GameState.Running)
            updateRunning(touchEvents, deltaTime);
        if(state == GameState.Paused)
            updatePaused(touchEvents);
        if(state == GameState.GameOver)
            updateGameOver(touchEvents);        
    }

    private void updateReady(List<TouchEvent> touchEvents) {
        if(touchEvents.size() > 0)
            state = GameState.Running;
        //start playing music
        Assets.music.setLooping(true);
        if(Settings.soundEnabled)
        	Assets.music.play();
    }

    private void updateRunning(List<TouchEvent> touchEvents, float deltaTime) {  
        int len = touchEvents.size();
        for(int i = 0; i < len; i++) {
            TouchEvent event = touchEvents.get(i);
            if(event.type == TouchEvent.TOUCH_UP) {
                if(event.x < 64 && event.y < 64) {
                    if(Settings.soundEnabled)
                        Assets.click.play(1);
                    state = GameState.Paused;
                    return;
                }
            }
            if(event.type == TouchEvent.TOUCH_UP) {
            	if(event.x > 64 || event.y > 64) {
            		world.block.RotateBlock();
            	}
            }
        }
        
        float accelX = game.getInput().getAccelX();
    	float accelY = game.getInput().getAccelY();
    	
    	moveDelay += deltaTime;

        if (moveDelay > MOVE_DELAY) {
            moveDelay = 0;

        	//move left
        	if (accelX > 4)
        	{
        		world.block.Move(-1, 0);
        	}
        
        	//move right
        	if (accelX < -4)
        	{
        		world.block.Move(1, 0);
        	}
        	
        	//move down
        	if (accelY > 4)
        	{
        		world.block.Move(0, 1);
        	}
        }
    	
        world.update(deltaTime);
        if(world.gameOver) {
            if(Settings.soundEnabled)
                Assets.lose.play(1);
            state = GameState.GameOver;
        }
        if(score != world.score) {
            score = world.score;
            if(Settings.soundEnabled)
                Assets.win.play(1);
        }
    }

    private void updatePaused(List<TouchEvent> touchEvents) {
    	//stop playing music
    	Assets.music.pause();
    	
        int len = touchEvents.size();
        for(int i = 0; i < len; i++) {
            TouchEvent event = touchEvents.get(i);
            if(event.type == TouchEvent.TOUCH_UP) {
                if(event.x > 80 && event.x <= 240) {
                    if(event.y > 100 && event.y <= 148) {
                        if(Settings.soundEnabled)
                        {
                            Assets.click.play(1);
                            Assets.music.play();
                        }
                        state = GameState.Running;
                        return;
                    }                    
                    if(event.y > 148 && event.y < 196) {
                        if(Settings.soundEnabled)
                            Assets.click.play(1);
                        game.setScreen(new MainMenuScreen(game)); 
                        return;
                    }
                }
            }
        }
    }

    private void updateGameOver(List<TouchEvent> touchEvents) {
    	//stop playing music
    	Assets.music.pause();
    	
        int len = touchEvents.size();
        for(int i = 0; i < len; i++) {
            TouchEvent event = touchEvents.get(i);
            if(event.type == TouchEvent.TOUCH_UP) {
                if(event.x >= 128 && event.x <= 192 &&
                   event.y >= 200 && event.y <= 264) {
                    if(Settings.soundEnabled)
                        Assets.click.play(1);
                    game.setScreen(new MainMenuScreen(game));
                    return;
                }
            }
        }
    }

    @Override
    public void present(float deltaTime) {
        Graphics g = game.getGraphics();
        
        g.drawPixmap(Assets.background, 0, 0);
        drawWorld();
        if(state == GameState.Ready) 
            drawReadyUI();
        if(state == GameState.Running)
            drawRunningUI();
        if(state == GameState.Paused)
            drawPausedUI();
        if(state == GameState.GameOver)
            drawGameOverUI();
    }

    private void drawWorld() {
        Graphics g = game.getGraphics();           
        World.Block block = world.block;
        
        //draw block
        for (int i = 0; i < 4; i++)
    		for (int j = 0; j < 4; j++)
    			if (block.size[i][j] == true)
    				g.drawPixmap(Assets.block, (block.col+j)*32, (block.row+i)*32);
        
    	//draw fields
    	for (int x = 0; x < World.WORLD_HEIGHT; x++)
    		for (int y = 0; y < World.WORLD_WIDTH; y++)
    			if (world.fields[x][y] == true)
    				g.drawPixmap(Assets.block, y*32, x*32);
    }

    private void drawReadyUI() {
        Graphics g = game.getGraphics();
        
        g.drawPixmap(Assets.ready, 47, 100);
    }
    
    private void drawRunningUI() {
        Graphics g = game.getGraphics();
        //pause button
        g.drawPixmap(Assets.buttons, 0, 0, 64, 128, 64, 64);
    }
    
    private void drawPausedUI() {
        Graphics g = game.getGraphics();
        
        g.drawPixmap(Assets.pause, 80, 100);
    }

    private void drawGameOverUI() {
        Graphics g = game.getGraphics();
        
        g.drawPixmap(Assets.gameOver, 62, 100);
    }

    @Override
    public void pause() {
    	//stop playing music
    	Assets.music.pause();
    	
        if(state == GameState.Running)
            state = GameState.Paused;
        
        if(world.gameOver) {
            Settings.addScore(world.score);
            Settings.save(game.getFileIO());
        }
    }

    @Override
    public void resume() {
        
    }

    @Override
    public void dispose() {
        
    }
}

