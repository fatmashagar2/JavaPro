package com.example.finalprogectxo;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;

public class WinDialog extends Dialog {

    private final String message;
    private final Activity activity;

    public WinDialog(@NonNull Context context, String message, Activity activity) {
        super(context);
        this.message = message;
        this.activity = activity;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.win_dialog_layout);
        final TextView messageTxt = findViewById(R.id.messageText);
        final Button startAgainBtn = findViewById(R.id.startAgainBtn);

        // Set the message text from the provided message
        messageTxt.setText(message);

        startAgainBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (activity instanceof MainActivity) {
                    ((MainActivity) activity).restartMatch();
                } else if (activity instanceof PlayWithRobotActivity) {
                    ((PlayWithRobotActivity) activity).restartMatch();
                }
                dismiss();
            }
        });
    }
}
