package com.example.smartpantrymanager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class RecipeAdapter
        extends RecyclerView.Adapter<RecipeAdapter.RecipeViewHolder> {

    private final List<Recipe> recipeList;
    private final OnRecipeClickListener listener;


    // Sends the View Recipe button click
    // back to the Activity
    public interface OnRecipeClickListener {

        void onViewRecipeClick(Recipe recipe);
    }


    // Constructor
    public RecipeAdapter(
            List<Recipe> recipeList,
            OnRecipeClickListener listener
    ) {

        this.recipeList = recipeList;
        this.listener = listener;
    }


    // Creates one recipe card
    @NonNull
    @Override
    public RecipeViewHolder onCreateViewHolder(
            @NonNull ViewGroup parent,
            int viewType
    ) {

        View view =
                LayoutInflater
                        .from(parent.getContext())
                        .inflate(
                                R.layout.item_recipe,
                                parent,
                                false
                        );

        return new RecipeViewHolder(view);
    }


    // Places Recipe data into the card
    @Override
    public void onBindViewHolder(
            @NonNull RecipeViewHolder holder,
            int position
    ) {

        Recipe recipe =
                recipeList.get(position);


        holder.tvRecipeName.setText(
                recipe.getName()
        );


        holder.btnViewRecipe
                .setOnClickListener(v ->

                        listener
                                .onViewRecipeClick(
                                        recipe
                                )
                );
    }


    // Number of recipes being displayed
    @Override
    public int getItemCount() {

        return recipeList.size();
    }


    // Holds the views from item_recipe.xml
    public static class RecipeViewHolder
            extends RecyclerView.ViewHolder {

        TextView tvRecipeName;
        Button btnViewRecipe;


        public RecipeViewHolder(
                @NonNull View itemView
        ) {

            super(itemView);


            tvRecipeName =
                    itemView.findViewById(
                            R.id.tvRecipeName
                    );


            btnViewRecipe =
                    itemView.findViewById(
                            R.id.btnViewRecipe
                    );
        }
    }
}