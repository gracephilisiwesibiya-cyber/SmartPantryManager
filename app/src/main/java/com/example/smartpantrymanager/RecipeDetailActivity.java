package com.example.smartpantrymanager;

import android.os.Bundle;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.ArrayList;

public class RecipeDetailActivity extends AppCompatActivity {

    private TextView tvRecipeDetailName;
    private TextView tvRecipeIngredients;
    private TextView tvRecipeSteps;

    private DatabaseHelper databaseHelper;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        EdgeToEdge.enable(this);

        setContentView(R.layout.activity_recipe_detail);


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
        tvRecipeDetailName =
                findViewById(
                        R.id.tvRecipeDetailName
                );

        tvRecipeIngredients =
                findViewById(
                        R.id.tvRecipeIngredients
                );

        tvRecipeSteps =
                findViewById(
                        R.id.tvRecipeSteps
                );


        databaseHelper =
                new DatabaseHelper(this);


        // Receive recipe information from Intent
        int recipeId =
                getIntent().getIntExtra(
                        "recipe_id",
                        -1
                );

        String recipeName =
                getIntent().getStringExtra(
                        "recipe_name"
                );

        String recipeSteps =
                getIntent().getStringExtra(
                        "recipe_steps"
                );


        // Display recipe name
        tvRecipeDetailName.setText(
                recipeName
        );


        // Display preparation steps
        tvRecipeSteps.setText(
                recipeSteps
        );


        // Get ingredients from SQLite
        ArrayList<RecipeIngredient> ingredients =
                databaseHelper
                        .getRecipeIngredientList(
                                recipeId
                        );


        StringBuilder ingredientText =
                new StringBuilder();


        for (RecipeIngredient ingredient
                : ingredients) {

            ingredientText
                    .append("• ")
                    .append(
                            formatQuantity(
                                    ingredient.getQuantity()
                            )
                    )
                    .append(" ")
                    .append(
                            ingredient.getUnit()
                    )
                    .append(" ")
                    .append(
                            capitalize(
                                    ingredient.getName()
                            )
                    )
                    .append("\n");
        }


        // Display ingredient list
        tvRecipeIngredients.setText(
                ingredientText.toString().trim()
        );
    }


    // Removes unnecessary .0
    // Example: 2.0 becomes 2
    private String formatQuantity(
            double quantity
    ) {

        if (quantity
                == (long) quantity) {

            return String.valueOf(
                    (long) quantity
            );
        }

        return String.valueOf(
                quantity
        );
    }


    // Makes ingredient names look cleaner
    // Example: egg becomes Egg
    private String capitalize(
            String text
    ) {

        if (text == null
                || text.isEmpty()) {

            return "";
        }

        return text.substring(0, 1)
                .toUpperCase()
                + text.substring(1);
    }
}
