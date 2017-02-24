package com.androidgames.tetris;

import java.util.List;

import com.androidgames.framework.Game;
import com.androidgames.framework.Graphics;
import com.androidgames.framework.Input.TouchEvent;
import com.androidgames.framework.Screen;

public class HelpScreen extends Screen {      
    
	public HelpScreen(Game game) {
        super(game);
    }

	@Override
    public void update(float deltaTime) {
        List<TouchEvent> touchEvents = game.getInput().getTouchEvents();
        game.getInput().getKeyEvents();
        
        int len = touchEvents.size();
        for(int i = 0; i < len; i++) {
            TouchEvent event = touchEvents.get(i);
            if(event.type == TouchEvent.TOUCH_UP) {
            	if (event.x < 64 && event.y > 416) {
                	game.setScreen(new MainMenuScreen(game));
                    if(Settings.soundEnabled)
                        Assets.click.play(1);
                    return;
                }
            }
        }
    }

    @Override
    public void present(float deltaTime) {
        Graphics g = game.getGraphics();      
        g.drawPixmap(Assets.background, 0, 0);
        g.drawPixmap(Assets.help, 64, 100);
        g.drawPixmap(Assets.buttons, 0, 416, 64, 64, 64, 64);
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void dispose() {

    }
}