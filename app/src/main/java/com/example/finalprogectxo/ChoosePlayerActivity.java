package com.example.finalprogectxo;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class ChoosePlayerActivity extends AppCompatActivity {

    private ImageView playerOneSymbol, playerTwoSymbol, startGameImageView;
    private TextView playerOneLabel, playerTwoLabel;

    private boolean isPlayerOne = true; // Default: player one is X and player two is O

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_player);

        // Initialize views
        playerOneSymbol = findViewById(R.id.playerOneSymbol);
        playerTwoSymbol = findViewById(R.id.playerTwoSymbol);
        playerOneLabel = findViewById(R.id.playerOneLabel);
        playerTwoLabel = findViewById(R.id.playerTwoLabel);
        startGameImageView = findViewById(R.id.startGameImageView);

        // Set initial symbols and labels
        updateSymbolsAndLabels();

        // Set up listeners for changing labels
        playerOneSymbol.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ChoosePlayerActivity.this, PlayWithRobotActivity.class);
                intent.putExtra("PlayerOneSymbol", isPlayerOne ? R.drawable.close : R.drawable.o);
                intent.putExtra("PlayerTwoSymbol", isPlayerOne ? R.drawable.o : R.drawable.close);
                startActivity(intent);
            }
        });

        playerTwoSymbol.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isPlayerOne = false;
                updateSymbolsAndLabels();
            }
        });

        // Set up listener for starting the game
        startGameImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ChoosePlayerActivity.this, PlayWithRobotActivity.class);
                intent.putExtra("PlayerOneSymbol", isPlayerOne ? R.drawable.close : R.drawable.o);
                intent.putExtra("PlayerTwoSymbol", isPlayerOne ? R.drawable.o : R.drawable.close);
                startActivity(intent);
            }
        });
    }

    private void updateSymbolsAndLabels() {
        // Set fixed symbols
        playerOneSymbol.setImageResource(R.drawable.close); // X
        playerTwoSymbol.setImageResource(R.drawable.o);    // O

        // Update labels based on selection
        if (isPlayerOne) {
            playerOneLabel.setText("You");
            playerTwoLabel.setText("Robot");
        } else {
            playerOneLabel.setText("Robot");
            playerTwoLabel.setText("You");
        }
    }
}
