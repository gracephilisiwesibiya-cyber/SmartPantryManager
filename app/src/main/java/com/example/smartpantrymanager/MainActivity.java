package com.example.smartpantrymanager;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.appbar.MaterialToolbar;

public class MainActivity extends AppCompatActivity {

    private TextView tvWelcome;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        EdgeToEdge.enable(this);

        setContentView(R.layout.activity_main);

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


        // -----------------------------------------
        // FIND VIEWS
        // -----------------------------------------

        MaterialToolbar topAppBar =
                findViewById(R.id.topAppBar);

        tvWelcome =
                findViewById(R.id.tvWelcome);

        Button btnViewPantry =
                findViewById(R.id.btnViewPantry);

        Button btnAddItem =
                findViewById(R.id.btnAddItem);

        Button btnSuggestedRecipes =
                findViewById(R.id.btnSuggestedRecipes);

        Button btnShoppingList =
                findViewById(R.id.btnShoppingList);


        // -----------------------------------------
        // TOOLBAR NAVIGATION MENU
        // -----------------------------------------

        topAppBar.setOnMenuItemClickListener(item -> {

            int itemId =
                    item.getItemId();


            if (itemId == R.id.menuHome) {

                // Already on Home
                return true;


            } else if (itemId == R.id.menuPantry) {

                Intent intent =
                        new Intent(
                                MainActivity.this,
                                ViewPantryActivity.class
                        );

                startActivity(intent);

                return true;


            } else if (itemId == R.id.menuRecipes) {

                Intent intent =
                        new Intent(
                                MainActivity.this,
                                SuggestedRecipesActivity.class
                        );

                startActivity(intent);

                return true;


            } else if (itemId == R.id.menuShopping) {

                Intent intent =
                        new Intent(
                                MainActivity.this,
                                ShoppingListActivity.class
                        );

                startActivity(intent);

                return true;


            } else if (itemId == R.id.menuSettings) {

                Intent intent =
                        new Intent(
                                MainActivity.this,
                                SettingsActivity.class
                        );

                startActivity(intent);

                return true;
            }


            return false;
        });


        // -----------------------------------------
        // HOME SCREEN BUTTONS
        // -----------------------------------------

        btnViewPantry.setOnClickListener(v -> {

            Intent intent =
                    new Intent(
                            MainActivity.this,
                            ViewPantryActivity.class
                    );

            startActivity(intent);
        });


        btnAddItem.setOnClickListener(v -> {

            Intent intent =
                    new Intent(
                            MainActivity.this,
                            AddItemActivity.class
                    );

            startActivity(intent);
        });


        btnSuggestedRecipes.setOnClickListener(v -> {

            Intent intent =
                    new Intent(
                            MainActivity.this,
                            SuggestedRecipesActivity.class
                    );

            startActivity(intent);
        });


        btnShoppingList.setOnClickListener(v -> {

            Intent intent =
                    new Intent(
                            MainActivity.this,
                            ShoppingListActivity.class
                    );

            startActivity(intent);
        });
    }


    // -----------------------------------------
    // LOAD SAVED DISPLAY NAME
    // -----------------------------------------

    @Override
    protected void onResume() {

        super.onResume();


        SharedPreferences sharedPreferences =
                getSharedPreferences(
                        "SmartPantrySettings",
                        MODE_PRIVATE
                );


        String displayName =
                sharedPreferences.getString(
                        "display_name",
                        ""
                );


        if (displayName != null
                && !displayName.isEmpty()) {

            tvWelcome.setText(
                    "Welcome, "
                            + displayName
                            + "!"
            );

        } else {

            tvWelcome.setText(
                    "Manage your pantry smarter"
            );
        }
    }
}