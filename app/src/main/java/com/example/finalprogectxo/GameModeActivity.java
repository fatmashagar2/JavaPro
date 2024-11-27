package com.example.finalprogectxo;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.ImageView;
import androidx.appcompat.app.AppCompatActivity;
public class GameModeActivity extends AppCompatActivity {

    private static final String PREFS_NAME = "GamePrefs";
    private SharedPreferences sharedPreferences;
    private SoundManager soundManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_mode);

        sharedPreferences = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        soundManager = SoundManager.getInstance(this);

        ImageView humanPlayerContainer = findViewById(R.id.imageHumanPlayer);
        humanPlayerContainer.setOnClickListener(v -> {
            if (soundManager != null) {
                soundManager.playNewClickSound();
            }
            Intent intent = new Intent(GameModeActivity.this, AddPlayers.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
            startActivity(intent);
        });

        ImageView robotPlayerContainer = findViewById(R.id.imageRobotPlayer);
        robotPlayerContainer.setOnClickListener(v -> {
            if (soundManager != null) {
                soundManager.playNewClickSound();
            }
            Intent intent = new Intent(GameModeActivity.this, ChoosePlayerActivity.class);
            startActivity(intent);
        });

        ImageView settingsContainer = findViewById(R.id.imageSettings);
        settingsContainer.setOnClickListener(v -> {
            Intent intent = new Intent(GameModeActivity.this, SettingActivity.class);
            startActivity(intent);
        });

        boolean musicEnabled = sharedPreferences.getBoolean("musicEnabled", true);
        handleMusic(musicEnabled);
    }

    private void handleMusic(boolean isEnabled) {
        if (soundManager != null) {
            if (isEnabled) {
                soundManager.playBackgroundMusic();
            } else {
                soundManager.stopBackgroundMusic();
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (soundManager != null) {
            soundManager.release();
        }
    }
}
