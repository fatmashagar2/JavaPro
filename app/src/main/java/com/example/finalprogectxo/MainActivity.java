package com.example.finalprogectxo;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private final List<int[]> combinationsList = new ArrayList<>();
    private int[] boxPositions = {0, 0, 0, 0, 0, 0, 0, 0, 0};
    private int playerTurn = 1;
    private int totalSelectedBoxes = 0; // Start at 0 since no boxes are selected initially
    private int playerOneScore = 0;
    private int playerTwoScore = 0;
    private LinearLayout playerOneLayout, playerTwoLayout;
    private TextView playerOneName, playerTwoName;
    private TextView playerOneScoreView, playerTwoScoreView;
    private ImageView image1, image2, image3, image4, image5, image6, image7, image8, image9;
    private SoundManager soundManager;
    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize SharedPreferences and SoundManager
        sharedPreferences = getSharedPreferences("GamePrefs", MODE_PRIVATE);
        soundManager = SoundManager.getInstance(this);

        // Initialize views
        playerOneName = findViewById(R.id.playerOneName);
        playerTwoName = findViewById(R.id.playerTwoName);
        playerOneLayout = findViewById(R.id.playerOneLayout);
        playerTwoLayout = findViewById(R.id.playerTwoLayout);
        playerOneScoreView = findViewById(R.id.playerOneScore); // Assuming you have these TextViews in your layout
        playerTwoScoreView = findViewById(R.id.playerTwoScore); // Assuming you have these TextViews in your layout
        image1 = findViewById(R.id.image1);
        image2 = findViewById(R.id.image2);
        image3 = findViewById(R.id.image3);
        image4 = findViewById(R.id.image4);
        image5 = findViewById(R.id.image5);
        image6 = findViewById(R.id.image6);
        image7 = findViewById(R.id.image7);
        image8 = findViewById(R.id.image8);
        image9 = findViewById(R.id.image9);

        // Initialize combinations list
        combinationsList.add(new int[]{0, 1, 2});
        combinationsList.add(new int[]{3, 4, 5});
        combinationsList.add(new int[]{6, 7, 8});
        combinationsList.add(new int[]{0, 3, 6});
        combinationsList.add(new int[]{1, 4, 7});
        combinationsList.add(new int[]{2, 5, 8});
        combinationsList.add(new int[]{2, 4, 6});
        combinationsList.add(new int[]{0, 4, 8});

        // Get player names from intent
        String getPlayerOneName = getIntent().getStringExtra("PlayerOne");
        String getPlayerTwoName = getIntent().getStringExtra("PlayerTwo");
        playerOneName.setText(getPlayerOneName);
        playerTwoName.setText(getPlayerTwoName);

        // Set up click listeners for all ImageViews
        setupClickListener(image1, 0);
        setupClickListener(image2, 1);
        setupClickListener(image3, 2);
        setupClickListener(image4, 3);
        setupClickListener(image5, 4);
        setupClickListener(image6, 5);
        setupClickListener(image7, 6);
        setupClickListener(image8, 7);
        setupClickListener(image9, 8);
    }

    private void setupClickListener(final ImageView imageView, final int position) {
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isBoxSelectable(position)) {
                    // Check if sound is enabled before playing
                    if (sharedPreferences.getBoolean("soundEnabled", true)) {
                        if (playerTurn == 1) {
                            soundManager.playXSound(); // Play sound for X
                        } else {
                            soundManager.playOSound(); // Play sound for O
                        }
                    }
                    performAction(imageView, position);
                }
            }
        });
    }

    private void performAction(ImageView imageView, int selectedBoxPosition) {
        boxPositions[selectedBoxPosition] = playerTurn;
        if (playerTurn == 1) {
            imageView.setImageResource(R.drawable.close);
        } else {
            imageView.setImageResource(R.drawable.o);
        }

        if (checkPlayerWin()) {
            String winner = playerTurn == 1 ? playerOneName.getText().toString() : playerTwoName.getText().toString();
            showWinDialog(winner + " has won the match");
            updateScores();
        } else if (totalSelectedBoxes == 8) { // Total boxes are 9, but totalSelectedBoxes should be 8 before showing draw
            showWinDialog("It is a draw!");
        } else {
            changePlayerTurn(playerTurn == 1 ? 2 : 1);
            totalSelectedBoxes++;
        }
    }

    private void showWinDialog(String message) {
        WinDialog winDialog = new WinDialog(MainActivity.this, message, MainActivity.this);
        winDialog.setCancelable(false);
        winDialog.show();
    }

    private void changePlayerTurn(int currentPlayerTurn) {
        playerTurn = currentPlayerTurn;
        if (playerTurn == 1) {
            playerOneLayout.setBackgroundResource(R.drawable.round_back_blue_border);
            playerTwoLayout.setBackgroundResource(R.drawable.round_back_dark_blue);
        } else {
            playerTwoLayout.setBackgroundResource(R.drawable.round_back_blue_border);
            playerOneLayout.setBackgroundResource(R.drawable.round_back_dark_blue);
        }
    }

    private boolean checkPlayerWin() {
        for (int[] combination : combinationsList) {
            if (boxPositions[combination[0]] == playerTurn &&
                    boxPositions[combination[1]] == playerTurn &&
                    boxPositions[combination[2]] == playerTurn) {
                return true;
            }
        }
        return false;
    }

    private boolean isBoxSelectable(int boxPosition) {
        return boxPositions[boxPosition] == 0;
    }

    private void updateScores() {
        if (playerTurn == 1) {
            playerOneScore++;
            playerOneScoreView.setText(String.valueOf(playerOneScore));
        } else {
            playerTwoScore++;
            playerTwoScoreView.setText(String.valueOf(playerTwoScore));
        }
    }

    public void restartMatch() {
        boxPositions = new int[]{0, 0, 0, 0, 0, 0, 0, 0, 0};
        playerTurn = 1;
        totalSelectedBoxes = 0; // Reset to 0 since no boxes are selected initially
        ImageView[] images = {image1, image2, image3, image4, image5, image6, image7, image8, image9};
        for (ImageView image : images) {
            image.setImageResource(R.drawable.round_back_dark_blue);
        }
    }
    private void showWinDetailDialog(String winner, String description) {
        WinDetailDialog winDetailDialog = new WinDetailDialog(MainActivity.this,
                winner + " has won the match!",
                description,
                MainActivity.this);
        winDetailDialog.setCancelable(false);
        winDetailDialog.show();
    }

    private void performAction2(ImageView imageView, int selectedBoxPosition) {
        boxPositions[selectedBoxPosition] = playerTurn;
        if (playerTurn == 1) {
            imageView.setImageResource(R.drawable.close);
        } else {
            imageView.setImageResource(R.drawable.o);
        }

        if (checkPlayerWin()) {
            String winner = playerTurn == 1 ? playerOneName.getText().toString() : playerTwoName.getText().toString();
            String description = "Congratulations " + winner + "! You have achieved three in a row.";
            showWinDetailDialog(winner, description);
            updateScores();
        } else if (totalSelectedBoxes == 8) { // Total boxes are 9, but totalSelectedBoxes should be 8 before showing draw
            showWinDetailDialog("It's a draw!", "The game ended in a draw. Try again!");
        } else {
            changePlayerTurn(playerTurn == 1 ? 2 : 1);
            totalSelectedBoxes++;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (soundManager != null) {
            soundManager.release(); // Release sound resources when the activity is destroyed
        }
    }
}
