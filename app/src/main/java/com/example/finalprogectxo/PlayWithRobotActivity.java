package com.example.finalprogectxo;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import java.util.Random;

public class PlayWithRobotActivity extends AppCompatActivity {

    private ImageView[][] buttons = new ImageView[3][3];
    private boolean playerTurn = true; // true if player's turn, false if robot's turn
    private int[][] board = new int[3][3]; // 0 for empty, 1 for X (player), 2 for O (robot)
    private static final int PLAYER = 1;
    private static final int ROBOT = 2;
    private static final int EMPTY = 0;
    private SoundManager soundManager;
    private SharedPreferences sharedPreferences;

    private int playerSymbolResId;
    private int robotSymbolResId;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play_with_robot);

        // Initialize SharedPreferences and SoundManager
        sharedPreferences = getSharedPreferences("GamePrefs", MODE_PRIVATE);
        soundManager = SoundManager.getInstance(this);

        // Get symbols from intent
        Intent intent = getIntent();
        playerSymbolResId = intent.getIntExtra("PlayerOneSymbol", R.drawable.close); // Default value
        robotSymbolResId = intent.getIntExtra("PlayerTwoSymbol", R.drawable.o); // Default value

        // Update player and robot symbols and names
        updatePlayerAndRobotInfo();

        initializeButtons();
    }

    private void updatePlayerAndRobotInfo() {
        // Update Player One
        TextView playerOneName = findViewById(R.id.playerOneName);
        ImageView playerOneSymbol = findViewById(R.id.playerOneSymbol);

        // Here, replace with actual player name if needed
        playerOneName.setText("You");
        playerOneSymbol.setImageResource(playerSymbolResId);

        // Update Player Two
        TextView playerTwoName = findViewById(R.id.playerTwoName);
        ImageView playerTwoSymbol = findViewById(R.id.playerTwoSymbol);

        // Here, replace with actual robot name if needed
        playerTwoName.setText("Robot");
        playerTwoSymbol.setImageResource(robotSymbolResId);
    }
    private void initializeButtons() {
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                String imageViewID = "image" + (10 + i * 3 + j); // ID of the ImageView in XML
                int resID = getResources().getIdentifier(imageViewID, "id", getPackageName());
                buttons[i][j] = findViewById(resID);
                buttons[i][j].setOnClickListener(new CellClickListener(i, j));
                board[i][j] = EMPTY; // Initialize the board with EMPTY
            }
        }
    }

    private class CellClickListener implements View.OnClickListener {
        private int row, col;

        public CellClickListener(int row, int col) {
            this.row = row;
            this.col = col;
        }

        @Override
        public void onClick(View v) {
            if (playerTurn && board[row][col] == EMPTY) {
                buttons[row][col].setImageResource(playerSymbolResId); // Player's move
                board[row][col] = PLAYER;
                playSoundIfEnabled(true); // Play sound for player's symbol if sound is enabled
                if (checkWin(PLAYER)) {
                    showWinDialog("Player wins!");
                    return;
                }
                if (isBoardFull()) {
                    showWinDialog("It's a draw!");
                    return;
                }
                playerTurn = false;
                makeRobotMove();
            }
        }
    }

    private void makeRobotMove() {
        Random rand = new Random();
        int row, col;
        do {
            row = rand.nextInt(3);
            col = rand.nextInt(3);
        } while (board[row][col] != EMPTY);
        buttons[row][col].setImageResource(robotSymbolResId); // Robot's move
        board[row][col] = ROBOT;
        playSoundIfEnabled(false); // Play sound for robot's symbol if sound is enabled
        if (checkWin(ROBOT)) {
            showWinDialog("Robot wins!");
            return;
        }
        if (isBoardFull()) {
            showWinDialog("It's a draw!");
            return;
        }
        playerTurn = true;
    }

    private void playSoundIfEnabled(boolean isPlayer) {
        if (sharedPreferences.getBoolean("soundEnabled", true)) {
            if (isPlayer) {
                soundManager.playXSound(); // Play X sound if sound is enabled
            } else {
                soundManager.playOSound(); // Play O sound if sound is enabled
            }
        }
    }

    private boolean checkWin(int player) {
        // Check rows, columns, and diagonals
        for (int i = 0; i < 3; i++) {
            if (board[i][0] == player && board[i][1] == player && board[i][2] == player) return true;
            if (board[0][i] == player && board[1][i] == player && board[2][i] == player) return true;
        }
        if (board[0][0] == player && board[1][1] == player && board[2][2] == player) return true;
        if (board[0][2] == player && board[1][1] == player && board[2][0] == player) return true;

        return false;
    }

    private boolean isBoardFull() {
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (board[i][j] == EMPTY) {
                    return false;
                }
            }
        }
        return true;
    }

    private void showWinDialog(String message) {
        WinDialog winDialog = new WinDialog(PlayWithRobotActivity.this, message, PlayWithRobotActivity.this);
        winDialog.setCancelable(false);
        winDialog.setOnDismissListener(dialog -> restartMatch()); // Restart match when dialog is dismissed
        winDialog.show();
    }

    public void restartMatch() {
        // Reset the board
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                board[i][j] = EMPTY;
                buttons[i][j].setImageResource(android.R.color.transparent); // Clear images
            }
        }
        playerTurn = true; // Set player to start
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (soundManager != null) {
            soundManager.release(); // Release sound resources when the activity is destroyed
        }
    }
}
