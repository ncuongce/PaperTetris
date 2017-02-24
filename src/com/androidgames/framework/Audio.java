package com.androidgames.framework;

import com.androidgames.framework.Music;
import com.androidgames.framework.Sound;

public interface Audio {
    public Music newMusic(String filename);

    public Sound newSound(String filename);
}
