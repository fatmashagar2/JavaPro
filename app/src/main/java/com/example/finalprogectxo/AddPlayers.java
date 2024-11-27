package com.example.finalprogectxo;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class AddPlayers extends AppCompatActivity {

    private SoundManager soundManager;
    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_players);

        final EditText playerOne = findViewById(R.id.playerOneName);
        final EditText playerTwo = findViewById(R.id.playerTwoName);
        final Button startGameBtn = findViewById(R.id.startGameBtn);

        soundManager = SoundManager.getInstance(this);
        sharedPreferences = getSharedPreferences("AppSettings", MODE_PRIVATE);

        startGameBtn.setOnClickListener(v -> {
            // Check if sound is enabled before playing the click sound
            if (sharedPreferences.getBoolean("sound_enabled", true)) {
                soundManager.playNewClickSound();
            }

            final String getPlayerOneName = playerOne.getText().toString();
            final String getPlayerTwoName = playerTwo.getText().toString();
            if (getPlayerOneName.isEmpty() || getPlayerTwoName.isEmpty()) {
                Toast.makeText(AddPlayers.this, "Please enter both player names", Toast.LENGTH_SHORT).show();
            } else {
                Intent intent = new Intent(AddPlayers.this, MainActivity.class);
                intent.putExtra("PlayerOne", getPlayerOneName);
                intent.putExtra("PlayerTwo", getPlayerTwoName);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (soundManager != null) {
            soundManager.release(); // Release sound resources when the activity is destroyed
        }
    }
}
