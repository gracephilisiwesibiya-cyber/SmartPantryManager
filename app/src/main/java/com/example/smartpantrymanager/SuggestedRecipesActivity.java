package com.example.smartpantrymanager;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class SuggestedRecipesActivity extends AppCompatActivity {

    private RecyclerView recyclerViewRecipes;
    private TextView tvNoRecipes;

    private DatabaseHelper databaseHelper;

    private ArrayList<Recipe> suggestedRecipes;
    private RecipeAdapter recipeAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        EdgeToEdge.enable(this);

        setContentView(
                R.layout.activity_suggested_recipes
        );


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
        recyclerViewRecipes =
                findViewById(
                        R.id.recyclerViewRecipes
                );

        tvNoRecipes =
                findViewById(
                        R.id.tvNoRecipes
                );


        // Connect to SQLite database
        databaseHelper =
                new DatabaseHelper(this);


        // List containing only recipes
        // that match the pantry
        suggestedRecipes =
                new ArrayList<>();


        // Display recipe cards vertically
        recyclerViewRecipes.setLayoutManager(
                new LinearLayoutManager(this)
        );


        // Create the Recipe Adapter
        recipeAdapter =
                new RecipeAdapter(
                        suggestedRecipes,
                        recipe -> {

                            // Create an Intent to open
                            // the Recipe Detail screen
                            Intent intent =
                                    new Intent(
                                            SuggestedRecipesActivity.this,
                                            RecipeDetailActivity.class
                                    );


                            // Send selected recipe information
                            // to RecipeDetailActivity
                            intent.putExtra(
                                    "recipe_id",
                                    recipe.getId()
                            );

                            intent.putExtra(
                                    "recipe_name",
                                    recipe.getName()
                            );

                            intent.putExtra(
                                    "recipe_steps",
                                    recipe.getSteps()
                            );


                            // Open Recipe Detail screen
                            startActivity(intent);
                        }
                );


        // Connect Adapter to RecyclerView
        recyclerViewRecipes.setAdapter(
                recipeAdapter
        );


        // Load matching recipes
        loadSuggestedRecipes();
    }


    private void loadSuggestedRecipes() {

        suggestedRecipes.clear();


        // Get only recipes that pass
        // the strict matching rules
        ArrayList<Recipe> matches =
                databaseHelper
                        .getSuggestedRecipes();


        suggestedRecipes.addAll(
                matches
        );


        // Refresh RecyclerView
        recipeAdapter
                .notifyDataSetChanged();


        // Zero-match feedback
        if (suggestedRecipes.isEmpty()) {

            tvNoRecipes.setVisibility(
                    View.VISIBLE
            );

            recyclerViewRecipes.setVisibility(
                    View.GONE
            );

        } else {

            tvNoRecipes.setVisibility(
                    View.GONE
            );

            recyclerViewRecipes.setVisibility(
                    View.VISIBLE
            );
        }
    }


    @Override
    protected void onResume() {

        super.onResume();


        // Recheck the pantry whenever
        // the user returns to this screen
        if (databaseHelper != null
                && recipeAdapter != null) {

            loadSuggestedRecipes();
        }
    }
}