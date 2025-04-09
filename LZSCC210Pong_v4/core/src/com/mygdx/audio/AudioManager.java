package com.mygdx.audio;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;

public class AudioManager {
    private static AudioManager instance;
    private Music backgroundMusic;
    private boolean isMusicOn = true;

    private AudioManager() {
        backgroundMusic = Gdx.audio.newMusic(Gdx.files.internal("music.mp3"));
        backgroundMusic.setLooping(true);
        backgroundMusic.setVolume(0.5f);
    }

    public static AudioManager getInstance() {
        if (instance == null) {
            instance = new AudioManager();
        }
        return instance;
    }

    public void playMusic() {
        if (isMusicOn && !backgroundMusic.isPlaying()) {
            backgroundMusic.play();
        }
    }

    public void stopMusic() {
        backgroundMusic.pause();
    }

    public void toggleMusic() {
        if (isMusicOn) {
            stopMusic();
            isMusicOn = false;
        } else {
            isMusicOn = true;
            playMusic();
        }
    }

    public void dispose() {
        backgroundMusic.dispose();
    }
}
