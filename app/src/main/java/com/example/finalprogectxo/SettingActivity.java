package com.example.finalprogectxo;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.widget.CheckBox;
import android.widget.FrameLayout;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Locale;

public class SettingActivity extends AppCompatActivity {

    private CheckBox checkboxMusic, checkboxSound;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    private SoundManager soundManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Initialize SharedPreferences
        sharedPreferences = getSharedPreferences("AppSettings", Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();

        // Load saved language before setting content view
        loadLanguage();

        setContentView(R.layout.activity_setting);

        // Initialize the views
        checkboxMusic = findViewById(R.id.checkboxMusic2);
        checkboxSound = findViewById(R.id.checkboxSound2);

        // Initialize SoundManager
        soundManager = SoundManager.getInstance(this);

        // Load saved settings from SharedPreferences
        boolean isMusicEnabled = sharedPreferences.getBoolean("music_enabled", true);
        boolean isSoundEnabled = sharedPreferences.getBoolean("sound_enabled", true);

        // Set the checkbox states based on saved preferences
        checkboxMusic.setChecked(isMusicEnabled);
        checkboxSound.setChecked(isSoundEnabled);

        // Start or stop music based on the saved state
        handleMusic(isMusicEnabled);

        // Handle Music Checkbox state change
        checkboxMusic.setOnCheckedChangeListener((buttonView, isChecked) -> {
            editor.putBoolean("music_enabled", isChecked);
            editor.apply();
            handleMusic(isChecked);
        });

        // Handle Sound Checkbox state change
        checkboxSound.setOnCheckedChangeListener((buttonView, isChecked) -> {
            editor.putBoolean("sound_enabled", isChecked);
            editor.apply();
            soundManager.setSoundEnabled(isChecked);
        });

        // Handle language change
        setupLanguageButtons();
    }

    private void handleMusic(boolean isEnabled) {
        if (soundManager != null) {
            if (isEnabled) {
                soundManager.playBackgroundMusic(); // Start background music
            } else {
                soundManager.stopBackgroundMusic(); // Stop background music
            }
        }
    }

    private void setupLanguageButtons() {
        // English Button
        FrameLayout buttonEnglish = findViewById(R.id.buttonEnglish);
        buttonEnglish.setOnClickListener(v -> changeLanguage("en"));

        // French Button
        FrameLayout buttonFrench = findViewById(R.id.buttonFrench);
        buttonFrench.setOnClickListener(v -> changeLanguage("fr"));

        // Arabic Button
        FrameLayout buttonArabic = findViewById(R.id.buttonArabic);
        buttonArabic.setOnClickListener(v -> changeLanguage("ar"));

        // Spanish Button
        FrameLayout buttonSpanish = findViewById(R.id.buttonSpanish);
        buttonSpanish.setOnClickListener(v -> changeLanguage("es"));

        // German Button
        FrameLayout buttonGerman = findViewById(R.id.buttonGerman);
        buttonGerman.setOnClickListener(v -> changeLanguage("de"));
    }

    private void changeLanguage(String languageCode) {
        // Save the selected language in SharedPreferences
        editor.putString("language_code", languageCode);
        editor.apply();

        // Reload the language settings
        loadLanguage();

        // Restart the activity to apply the language change
        recreate();
    }

    private void loadLanguage() {
        // Load the saved language from SharedPreferences
        String languageCode = sharedPreferences.getString("language_code", "en");

        Locale locale = new Locale(languageCode);
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        config.setLocale(locale);

        getBaseContext().getResources().updateConfiguration(config, getBaseContext().getResources().getDisplayMetrics());
    }
}
