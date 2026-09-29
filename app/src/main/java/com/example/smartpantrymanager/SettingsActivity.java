package com.example.smartpantrymanager;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class SettingsActivity extends AppCompatActivity {

    private EditText etDisplayName;
    private SwitchCompat switchExpiryWarnings;
    private Button btnSaveSettings;

    private SharedPreferences sharedPreferences;

    private static final String PREFS_NAME =
            "SmartPantrySettings";

    private static final String KEY_DISPLAY_NAME =
            "display_name";

    private static final String KEY_EXPIRY_WARNINGS =
            "expiry_warnings";


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        EdgeToEdge.enable(this);

        setContentView(R.layout.activity_settings);


        ViewCompat.setOnApplyWindowInsetsListener(
                findViewById(R.id.main),
                (v, insets) -> {

                    Insets systemBars =
                            insets.getInsets(
                                    WindowInsetsCompat.Type.systemBars()
                            );

                    v.setPadding(
                            systemBars.left,
                            systemBars.top,
                            systemBars.right,
                            systemBars.bottom
                    );

                    return insets;
                }
        );


        // Connect Java to XML
        etDisplayName =
                findViewById(
                        R.id.etDisplayName
                );

        switchExpiryWarnings =
                findViewById(
                        R.id.switchExpiryWarnings
                );

        btnSaveSettings =
                findViewById(
                        R.id.btnSaveSettings
                );


        // Open the app's saved settings
        sharedPreferences =
                getSharedPreferences(
                        PREFS_NAME,
                        MODE_PRIVATE
                );


        // Load previously saved settings
        loadSettings();


        // Save button
        btnSaveSettings.setOnClickListener(v ->
                saveSettings()
        );
    }


    private void loadSettings() {

        String savedName =
                sharedPreferences.getString(
                        KEY_DISPLAY_NAME,
                        ""
                );

        boolean showExpiryWarnings =
                sharedPreferences.getBoolean(
                        KEY_EXPIRY_WARNINGS,
                        true
                );


        etDisplayName.setText(
                savedName
        );

        switchExpiryWarnings.setChecked(
                showExpiryWarnings
        );
    }


    private void saveSettings() {

        String displayName =
                etDisplayName
                        .getText()
                        .toString()
                        .trim();

        boolean showExpiryWarnings =
                switchExpiryWarnings
                        .isChecked();


        SharedPreferences.Editor editor =
                sharedPreferences.edit();


        editor.putString(
                KEY_DISPLAY_NAME,
                displayName
        );

        editor.putBoolean(
                KEY_EXPIRY_WARNINGS,
                showExpiryWarnings
        );


        editor.apply();


        Toast.makeText(
                this,
                "Settings saved successfully",
                Toast.LENGTH_SHORT
        ).show();
    }
}