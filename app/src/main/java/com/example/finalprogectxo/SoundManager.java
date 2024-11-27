package com.example.finalprogectxo;

import android.content.Context;
import android.content.SharedPreferences;
import android.media.MediaPlayer;

import java.io.IOException;

public class SoundManager {
    private static SoundManager instance;
    private MediaPlayer musicPlayer;
    private MediaPlayer newClickSoundPlayer;
    private MediaPlayer xSoundPlayer;
    private MediaPlayer oSoundPlayer;
    private MediaPlayer winSoundPlayer;
    private SharedPreferences sharedPreferences;

    private SoundManager(Context context) {
        sharedPreferences = context.getSharedPreferences("AppSettings", Context.MODE_PRIVATE);
        // Initialize MediaPlayers with sound resources
        musicPlayer = MediaPlayer.create(context, R.raw.music);
        musicPlayer.setLooping(true);
        newClickSoundPlayer = MediaPlayer.create(context, R.raw.main);
        xSoundPlayer = MediaPlayer.create(context, R.raw.sound);
        oSoundPlayer = MediaPlayer.create(context, R.raw.sound2);
        winSoundPlayer = MediaPlayer.create(context, R.raw.main);
    }

    public static synchronized SoundManager getInstance(Context context) {
        if (instance == null) {
            instance = new SoundManager(context.getApplicationContext());
        }
        return instance;
    }

    public void playBackgroundMusic() {
        if (sharedPreferences.getBoolean("music_enabled", true)) {
            if (musicPlayer != null) {
                if (!musicPlayer.isPlaying()) {
                    musicPlayer.start();
                }
            }
        }
    }

    public void stopBackgroundMusic() {
        if (musicPlayer != null && musicPlayer.isPlaying()) {
            musicPlayer.pause();
        }
    }

    public void setSoundEnabled(boolean isEnabled) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean("sound_enabled", isEnabled);
        editor.apply();
    }

    public void playNewClickSound() {
        if (sharedPreferences.getBoolean("sound_enabled", true)) {
            playSound(newClickSoundPlayer);
        }
    }

    public void playXSound() {
        if (sharedPreferences.getBoolean("sound_enabled", true)) {
            playSound(xSoundPlayer);
        }
    }

    public void playOSound() {
        if (sharedPreferences.getBoolean("sound_enabled", true)) {
            playSound(oSoundPlayer);
        }
    }

    public void playWinSound() {
        if (sharedPreferences.getBoolean("sound_enabled", true)) {
            playSound(winSoundPlayer);
        }
    }

    private void playSound(MediaPlayer player) {
        if (player != null) {
            try {
                if (player.isPlaying()) {
                    player.seekTo(0); // Restart sound if it's already playing
                } else {
                    player.start();
                }
            } catch (IllegalStateException e) {
                // Handle exception or reinitialize the MediaPlayer if necessary
                player.reset();
                try {
                    player.prepare();
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
                player.start();
            } catch (Exception e) {
                e.printStackTrace(); // Log any other exceptions
            }
        }
    }

    public void release() {
        releaseMediaPlayer(musicPlayer);
        releaseMediaPlayer(newClickSoundPlayer);
        releaseMediaPlayer(xSoundPlayer);
        releaseMediaPlayer(oSoundPlayer);
        releaseMediaPlayer(winSoundPlayer);
    }

    private void releaseMediaPlayer(MediaPlayer player) {
        if (player != null) {
            if (player.isPlaying()) {
                player.stop();
            }
            player.release();
        }
    }
}
