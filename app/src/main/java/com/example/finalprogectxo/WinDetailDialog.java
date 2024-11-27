package com.example.finalprogectxo;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class WinDetailDialog extends Dialog {

    private String message;
    private String description;
    private final MainActivity activity;

    public WinDetailDialog(Context context, String message, String description, MainActivity activity) {
        super(context);
        this.message = message;
        this.description = description;
        this.activity = activity;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_win_detail_dialog);

        TextView winDetailMessage = findViewById(R.id.winDetailMessage);
        TextView winDetailDescription = findViewById(R.id.winDetailDescription);
        Button okButton = findViewById(R.id.okButton);

        winDetailMessage.setText(message);
        winDetailDescription.setText(description);

        okButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activity.restartMatch(); // Restart the game when OK is clicked
                dismiss(); // Close the dialog
            }
        });
    }
}
