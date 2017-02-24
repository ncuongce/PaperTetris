package com.androidgames.framework;

import com.androidgames.framework.Audio;
import com.androidgames.framework.FileIO;
import com.androidgames.framework.Graphics;
import com.androidgames.framework.Input;
import com.androidgames.framework.Screen;

public interface Game {
    public Input getInput();

    public FileIO getFileIO();

    public Graphics getGraphics();

    public Audio getAudio();

    public void setScreen(Screen screen);

    public Screen getCurrentScreen();

    public Screen getStartScreen();
}
