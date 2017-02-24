package com.androidgames.tetris;

import com.androidgames.framework.Screen;
import com.androidgames.framework.impl.AndroidGame;

public class TetrisGame extends AndroidGame {
    public Screen getStartScreen() {
    	return new LoadingScreen(this);
    }
}
