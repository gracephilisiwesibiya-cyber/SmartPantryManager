package com.example.smartpantrymanager;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {

    // Database information
    private static final String DATABASE_NAME = "SmartPantry.db";
    private static final int DATABASE_VERSION = 4;


    // =========================
    // PANTRY TABLE
    // =========================

    public static final String TABLE_PANTRY = "pantry_items";

    public static final String COLUMN_ID = "id";
    public static final String COLUMN_NAME = "item_name";
    public static final String COLUMN_QUANTITY = "quantity";
    public static final String COLUMN_UNIT = "unit";
    public static final String COLUMN_CATEGORY = "category";
    public static final String COLUMN_EXPIRY_DATE = "expiry_date";


    // =========================
    // SHOPPING LIST TABLE
    // =========================

    public static final String TABLE_SHOPPING = "shopping_items";

    public static final String SHOPPING_ID = "id";
    public static final String SHOPPING_NAME = "item_name";
    public static final String SHOPPING_QUANTITY = "quantity";


    // =========================
    // RECIPE TABLE
    // =========================

    public static final String TABLE_RECIPES = "recipes";

    public static final String RECIPE_ID = "id";
    public static final String RECIPE_NAME = "recipe_name";
    public static final String RECIPE_STEPS = "steps";


    // =========================
    // RECIPE INGREDIENTS TABLE
    // =========================

    public static final String TABLE_RECIPE_INGREDIENTS =
            "recipe_ingredients";

    public static final String INGREDIENT_ID = "id";
    public static final String INGREDIENT_RECIPE_ID = "recipe_id";
    public static final String INGREDIENT_NAME = "ingredient_name";
    public static final String INGREDIENT_QUANTITY = "quantity";
    public static final String INGREDIENT_UNIT = "unit";


    // Constructor
    public DatabaseHelper(Context context) {

        super(
                context,
                DATABASE_NAME,
                null,
                DATABASE_VERSION
        );
    }


    // Runs when database is created for the first time
    @Override
    public void onCreate(SQLiteDatabase db) {

        createPantryTable(db);

        createShoppingTable(db);

        createRecipeTables(db);

        seedRecipes(db);
    }


    // Runs when database version increases
    @Override
    public void onUpgrade(
            SQLiteDatabase db,
            int oldVersion,
            int newVersion
    ) {

        // Version 2 added Shopping List
        if (oldVersion < 2) {

            createShoppingTable(db);
        }


        // Version 3 added unit to pantry
        if (oldVersion < 3) {

            db.execSQL(
                    "ALTER TABLE " +
                            TABLE_PANTRY +
                            " ADD COLUMN " +
                            COLUMN_UNIT +
                            " TEXT NOT NULL DEFAULT 'item'"
            );
        }


        // Version 4 adds recipes
        if (oldVersion < 4) {

            createRecipeTables(db);

            seedRecipes(db);
        }
    }


    // =========================
    // CREATE TABLE METHODS
    // =========================

    private void createPantryTable(
            SQLiteDatabase db
    ) {

        String createPantryTable =
                "CREATE TABLE IF NOT EXISTS " +
                        TABLE_PANTRY + " (" +

                        COLUMN_ID +
                        " INTEGER PRIMARY KEY AUTOINCREMENT, " +

                        COLUMN_NAME +
                        " TEXT NOT NULL, " +

                        COLUMN_QUANTITY +
                        " INTEGER NOT NULL, " +

                        COLUMN_UNIT +
                        " TEXT NOT NULL, " +

                        COLUMN_CATEGORY +
                        " TEXT NOT NULL, " +

                        COLUMN_EXPIRY_DATE +
                        " TEXT NOT NULL)";

        db.execSQL(createPantryTable);
    }


    private void createShoppingTable(
            SQLiteDatabase db
    ) {

        String createShoppingTable =
                "CREATE TABLE IF NOT EXISTS " +
                        TABLE_SHOPPING + " (" +

                        SHOPPING_ID +
                        " INTEGER PRIMARY KEY AUTOINCREMENT, " +

                        SHOPPING_NAME +
                        " TEXT NOT NULL, " +

                        SHOPPING_QUANTITY +
                        " INTEGER NOT NULL)";

        db.execSQL(createShoppingTable);
    }


    private void createRecipeTables(
            SQLiteDatabase db
    ) {

        // Main recipe table
        String createRecipeTable =
                "CREATE TABLE IF NOT EXISTS " +
                        TABLE_RECIPES + " (" +

                        RECIPE_ID +
                        " INTEGER PRIMARY KEY AUTOINCREMENT, " +

                        RECIPE_NAME +
                        " TEXT NOT NULL, " +

                        RECIPE_STEPS +
                        " TEXT NOT NULL)";

        db.execSQL(createRecipeTable);


        // Ingredients required by each recipe
        String createIngredientTable =
                "CREATE TABLE IF NOT EXISTS " +
                        TABLE_RECIPE_INGREDIENTS + " (" +

                        INGREDIENT_ID +
                        " INTEGER PRIMARY KEY AUTOINCREMENT, " +

                        INGREDIENT_RECIPE_ID +
                        " INTEGER NOT NULL, " +

                        INGREDIENT_NAME +
                        " TEXT NOT NULL, " +

                        INGREDIENT_QUANTITY +
                        " REAL NOT NULL, " +

                        INGREDIENT_UNIT +
                        " TEXT NOT NULL, " +

                        "FOREIGN KEY(" +
                        INGREDIENT_RECIPE_ID +
                        ") REFERENCES " +
                        TABLE_RECIPES +
                        "(" +
                        RECIPE_ID +
                        "))";

        db.execSQL(createIngredientTable);
    }


    // =========================
    // PANTRY CRUD
    // =========================

    public boolean addPantryItem(
            String name,
            int quantity,
            String unit,
            String category,
            String expiryDate
    ) {

        SQLiteDatabase db =
                this.getWritableDatabase();

        ContentValues values =
                new ContentValues();

        values.put(COLUMN_NAME, name);
        values.put(COLUMN_QUANTITY, quantity);
        values.put(COLUMN_UNIT, unit);
        values.put(COLUMN_CATEGORY, category);
        values.put(COLUMN_EXPIRY_DATE, expiryDate);

        long result =
                db.insert(
                        TABLE_PANTRY,
                        null,
                        values
                );

        db.close();

        return result != -1;
    }


    public Cursor getAllPantryItems() {

        SQLiteDatabase db =
                this.getReadableDatabase();

        return db.rawQuery(
                "SELECT * FROM " +
                        TABLE_PANTRY +
                        " ORDER BY " +
                        COLUMN_ID +
                        " DESC",
                null
        );
    }


    public boolean deletePantryItem(
            int id
    ) {

        SQLiteDatabase db =
                this.getWritableDatabase();

        int result =
                db.delete(
                        TABLE_PANTRY,
                        COLUMN_ID + " = ?",
                        new String[]{
                                String.valueOf(id)
                        }
                );

        db.close();

        return result > 0;
    }


    public boolean updatePantryItem(
            int id,
            String name,
            int quantity,
            String unit,
            String category,
            String expiryDate
    ) {

        SQLiteDatabase db =
                this.getWritableDatabase();

        ContentValues values =
                new ContentValues();

        values.put(COLUMN_NAME, name);
        values.put(COLUMN_QUANTITY, quantity);
        values.put(COLUMN_UNIT, unit);
        values.put(COLUMN_CATEGORY, category);
        values.put(COLUMN_EXPIRY_DATE, expiryDate);

        int result =
                db.update(
                        TABLE_PANTRY,
                        values,
                        COLUMN_ID + " = ?",
                        new String[]{
                                String.valueOf(id)
                        }
                );

        db.close();

        return result > 0;
    }


    // =========================
    // SHOPPING LIST METHODS
    // =========================

    public boolean addShoppingItem(
            String name,
            int quantity
    ) {

        SQLiteDatabase db =
                this.getWritableDatabase();

        ContentValues values =
                new ContentValues();

        values.put(
                SHOPPING_NAME,
                name
        );

        values.put(
                SHOPPING_QUANTITY,
                quantity
        );

        long result =
                db.insert(
                        TABLE_SHOPPING,
                        null,
                        values
                );

        db.close();

        return result != -1;
    }


    public Cursor getAllShoppingItems() {

        SQLiteDatabase db =
                this.getReadableDatabase();

        return db.rawQuery(
                "SELECT * FROM " +
                        TABLE_SHOPPING +
                        " ORDER BY " +
                        SHOPPING_ID +
                        " DESC",
                null
        );
    }


    public boolean deleteShoppingItem(
            int id
    ) {

        SQLiteDatabase db =
                this.getWritableDatabase();

        int result =
                db.delete(
                        TABLE_SHOPPING,
                        SHOPPING_ID + " = ?",
                        new String[]{
                                String.valueOf(id)
                        }
                );

        db.close();

        return result > 0;
    }


    public boolean isShoppingItemExists(
            String name
    ) {

        SQLiteDatabase db =
                this.getReadableDatabase();

        Cursor cursor =
                db.query(
                        TABLE_SHOPPING,
                        new String[]{
                                SHOPPING_ID
                        },
                        SHOPPING_NAME +
                                " = ? COLLATE NOCASE",
                        new String[]{
                                name
                        },
                        null,
                        null,
                        null
                );

        boolean exists =
                cursor.getCount() > 0;

        cursor.close();
        db.close();

        return exists;
    }


    // =========================
    // RECIPE METHODS
    // =========================

    public Cursor getAllRecipes() {

        SQLiteDatabase db =
                this.getReadableDatabase();

        return db.rawQuery(
                "SELECT * FROM " +
                        TABLE_RECIPES +
                        " ORDER BY " +
                        RECIPE_NAME +
                        " ASC",
                null
        );
    }


    public Cursor getRecipeIngredients(
            int recipeId
    ) {

        SQLiteDatabase db =
                this.getReadableDatabase();

        return db.query(
                TABLE_RECIPE_INGREDIENTS,
                null,
                INGREDIENT_RECIPE_ID + " = ?",
                new String[]{
                        String.valueOf(recipeId)
                },
                null,
                null,
                null
        );
    }


    // =========================
    // SEED RECIPES
    // =========================

    private void seedRecipes(
            SQLiteDatabase db
    ) {

        // Prevent duplicate recipes
        Cursor cursor =
                db.rawQuery(
                        "SELECT COUNT(*) FROM " +
                                TABLE_RECIPES,
                        null
                );

        int recipeCount = 0;

        if (cursor.moveToFirst()) {

            recipeCount =
                    cursor.getInt(0);
        }

        cursor.close();


        if (recipeCount > 0) {

            return;
        }


        // 1. Scrambled Eggs
        long scrambledEggs =
                addRecipe(
                        db,
                        "Scrambled Eggs",
                        "Beat the eggs with milk. Cook in a pan over medium heat while stirring until the eggs are cooked."
                );

        addRecipeIngredient(
                db,
                scrambledEggs,
                "egg",
                2,
                "pieces"
        );

        addRecipeIngredient(
                db,
                scrambledEggs,
                "milk",
                100,
                "ml"
        );


        // 2. Cheese Omelette
        long cheeseOmelette =
                addRecipe(
                        db,
                        "Cheese Omelette",
                        "Beat the eggs. Pour into a heated pan, add cheese and cook until the eggs are set. Fold and serve."
                );

        addRecipeIngredient(
                db,
                cheeseOmelette,
                "egg",
                2,
                "pieces"
        );

        addRecipeIngredient(
                db,
                cheeseOmelette,
                "cheese",
                50,
                "g"
        );


        // 3. Tomato Omelette
        long tomatoOmelette =
                addRecipe(
                        db,
                        "Tomato Omelette",
                        "Chop the tomato. Beat the eggs and combine with tomato. Cook in a pan until the eggs are set."
                );

        addRecipeIngredient(
                db,
                tomatoOmelette,
                "egg",
                2,
                "pieces"
        );

        addRecipeIngredient(
                db,
                tomatoOmelette,
                "tomato",
                1,
                "piece"
        );


        // 4. Cheese Sandwich
        long cheeseSandwich =
                addRecipe(
                        db,
                        "Cheese Sandwich",
                        "Place the cheese between the slices of bread and serve, or toast until the cheese melts."
                );

        addRecipeIngredient(
                db,
                cheeseSandwich,
                "bread",
                2,
                "slices"
        );

        addRecipeIngredient(
                db,
                cheeseSandwich,
                "cheese",
                50,
                "g"
        );


        // 5. Tomato Sandwich
        long tomatoSandwich =
                addRecipe(
                        db,
                        "Tomato Sandwich",
                        "Slice the tomato and place it between the slices of bread. Serve immediately."
                );

        addRecipeIngredient(
                db,
                tomatoSandwich,
                "bread",
                2,
                "slices"
        );

        addRecipeIngredient(
                db,
                tomatoSandwich,
                "tomato",
                1,
                "piece"
        );


        // 6. Banana Toast
        long bananaToast =
                addRecipe(
                        db,
                        "Banana Toast",
                        "Toast the bread. Slice the banana and place it on top of the toast."
                );

        addRecipeIngredient(
                db,
                bananaToast,
                "bread",
                2,
                "slices"
        );

        addRecipeIngredient(
                db,
                bananaToast,
                "banana",
                1,
                "piece"
        );


        // 7. Peanut Butter Toast
        long peanutButterToast =
                addRecipe(
                        db,
                        "Peanut Butter Toast",
                        "Toast the bread and spread peanut butter evenly over each slice."
                );

        addRecipeIngredient(
                db,
                peanutButterToast,
                "bread",
                2,
                "slices"
        );

        addRecipeIngredient(
                db,
                peanutButterToast,
                "peanut butter",
                30,
                "g"
        );


        // 8. Rice and Egg Bowl
        long riceEgg =
                addRecipe(
                        db,
                        "Rice and Egg Bowl",
                        "Cook the rice until soft. Fry or scramble the eggs and serve them over the cooked rice."
                );

        addRecipeIngredient(
                db,
                riceEgg,
                "rice",
                200,
                "g"
        );

        addRecipeIngredient(
                db,
                riceEgg,
                "egg",
                2,
                "pieces"
        );


        // 9. Tomato Rice
        long tomatoRice =
                addRecipe(
                        db,
                        "Tomato Rice",
                        "Cook the rice. Chop the tomato and cook until soft, then mix it into the rice."
                );

        addRecipeIngredient(
                db,
                tomatoRice,
                "rice",
                200,
                "g"
        );

        addRecipeIngredient(
                db,
                tomatoRice,
                "tomato",
                2,
                "pieces"
        );


        // 10. Tuna Sandwich
        long tunaSandwich =
                addRecipe(
                        db,
                        "Tuna Sandwich",
                        "Drain the tuna and place it between the slices of bread. Serve immediately."
                );

        addRecipeIngredient(
                db,
                tunaSandwich,
                "bread",
                2,
                "slices"
        );

        addRecipeIngredient(
                db,
                tunaSandwich,
                "tuna",
                100,
                "g"
        );


        // 11. Chicken Rice
        long chickenRice =
                addRecipe(
                        db,
                        "Chicken Rice",
                        "Cook the rice. Cook the chicken thoroughly, slice it and serve it with the rice."
                );

        addRecipeIngredient(
                db,
                chickenRice,
                "rice",
                200,
                "g"
        );

        addRecipeIngredient(
                db,
                chickenRice,
                "chicken",
                200,
                "g"
        );


        // 12. Chicken Pasta
        long chickenPasta =
                addRecipe(
                        db,
                        "Chicken Pasta",
                        "Cook the pasta. Cook the chicken thoroughly, cut it into pieces and mix it with the pasta."
                );

        addRecipeIngredient(
                db,
                chickenPasta,
                "pasta",
                200,
                "g"
        );

        addRecipeIngredient(
                db,
                chickenPasta,
                "chicken",
                200,
                "g"
        );


        // 13. Tomato Pasta
        long tomatoPasta =
                addRecipe(
                        db,
                        "Tomato Pasta",
                        "Cook the pasta. Chop and cook the tomatoes until soft, then mix them into the pasta."
                );

        addRecipeIngredient(
                db,
                tomatoPasta,
                "pasta",
                200,
                "g"
        );

        addRecipeIngredient(
                db,
                tomatoPasta,
                "tomato",
                2,
                "pieces"
        );


        // 14. Cheese Pasta
        long cheesePasta =
                addRecipe(
                        db,
                        "Cheese Pasta",
                        "Cook the pasta until soft. Add the cheese while the pasta is hot and stir until melted."
                );

        addRecipeIngredient(
                db,
                cheesePasta,
                "pasta",
                200,
                "g"
        );

        addRecipeIngredient(
                db,
                cheesePasta,
                "cheese",
                80,
                "g"
        );


        // 15. Banana Milk Smoothie
        long bananaSmoothie =
                addRecipe(
                        db,
                        "Banana Milk Smoothie",
                        "Peel the banana. Blend it with the milk until smooth and serve immediately."
                );

        addRecipeIngredient(
                db,
                bananaSmoothie,
                "banana",
                1,
                "piece"
        );

        addRecipeIngredient(
                db,
                bananaSmoothie,
                "milk",
                250,
                "ml"
        );


        // 16. Apple Yogurt Bowl
        long appleYogurt =
                addRecipe(
                        db,
                        "Apple Yogurt Bowl",
                        "Chop the apple into small pieces and mix it with the yogurt."
                );

        addRecipeIngredient(
                db,
                appleYogurt,
                "apple",
                1,
                "piece"
        );

        addRecipeIngredient(
                db,
                appleYogurt,
                "yogurt",
                150,
                "g"
        );


        // 17. Banana Yogurt Bowl
        long bananaYogurt =
                addRecipe(
                        db,
                        "Banana Yogurt Bowl",
                        "Slice the banana and mix it with the yogurt. Serve chilled."
                );

        addRecipeIngredient(
                db,
                bananaYogurt,
                "banana",
                1,
                "piece"
        );

        addRecipeIngredient(
                db,
                bananaYogurt,
                "yogurt",
                150,
                "g"
        );


        // 18. Potato and Egg Bowl
        long potatoEgg =
                addRecipe(
                        db,
                        "Potato and Egg Bowl",
                        "Boil or fry the potatoes until cooked. Cook the eggs and serve them together."
                );

        addRecipeIngredient(
                db,
                potatoEgg,
                "potato",
                2,
                "pieces"
        );

        addRecipeIngredient(
                db,
                potatoEgg,
                "egg",
                2,
                "pieces"
        );


        // 19. Mashed Potatoes
        long mashedPotatoes =
                addRecipe(
                        db,
                        "Mashed Potatoes",
                        "Boil the potatoes until soft. Drain them, add milk and mash until smooth."
                );

        addRecipeIngredient(
                db,
                mashedPotatoes,
                "potato",
                3,
                "pieces"
        );

        addRecipeIngredient(
                db,
                mashedPotatoes,
                "milk",
                100,
                "ml"
        );


        // 20. Egg Sandwich
        long eggSandwich =
                addRecipe(
                        db,
                        "Egg Sandwich",
                        "Cook the eggs, place them between the slices of bread and serve."
                );

        addRecipeIngredient(
                db,
                eggSandwich,
                "bread",
                2,
                "slices"
        );

        addRecipeIngredient(
                db,
                eggSandwich,
                "egg",
                2,
                "pieces"
        );
    }


    // Insert one recipe and return its ID
    private long addRecipe(
            SQLiteDatabase db,
            String name,
            String steps
    ) {

        ContentValues values =
                new ContentValues();

        values.put(
                RECIPE_NAME,
                name
        );

        values.put(
                RECIPE_STEPS,
                steps
        );

        return db.insert(
                TABLE_RECIPES,
                null,
                values
        );
    }


    // Insert one ingredient belonging to a recipe
    private void addRecipeIngredient(
            SQLiteDatabase db,
            long recipeId,
            String ingredientName,
            double quantity,
            String unit
    ) {

        ContentValues values =
                new ContentValues();

        values.put(
                INGREDIENT_RECIPE_ID,
                recipeId
        );

        values.put(
                INGREDIENT_NAME,
                ingredientName
        );

        values.put(
                INGREDIENT_QUANTITY,
                quantity
        );

        values.put(
                INGREDIENT_UNIT,
                unit
        );

        db.insert(
                TABLE_RECIPE_INGREDIENTS,
                null,
                values
        );
    }
    // =========================
// STRICT RECIPE MATCHING
// =========================

    // Returns only recipes that can be made
// using the ingredients currently in the pantry
    public java.util.ArrayList<Recipe> getSuggestedRecipes() {

        java.util.ArrayList<Recipe> suggestedRecipes =
                new java.util.ArrayList<>();

        SQLiteDatabase db =
                this.getReadableDatabase();


        // Get all recipes
        Cursor recipeCursor =
                db.rawQuery(
                        "SELECT * FROM " +
                                TABLE_RECIPES,
                        null
                );


        while (recipeCursor.moveToNext()) {

            int recipeId =
                    recipeCursor.getInt(
                            recipeCursor.getColumnIndexOrThrow(
                                    RECIPE_ID
                            )
                    );


            String recipeName =
                    recipeCursor.getString(
                            recipeCursor.getColumnIndexOrThrow(
                                    RECIPE_NAME
                            )
                    );


            String recipeSteps =
                    recipeCursor.getString(
                            recipeCursor.getColumnIndexOrThrow(
                                    RECIPE_STEPS
                            )
                    );


            boolean canMakeRecipe = true;


            // Get every ingredient required
            // by this particular recipe
            Cursor ingredientCursor =
                    db.query(
                            TABLE_RECIPE_INGREDIENTS,
                            null,
                            INGREDIENT_RECIPE_ID + " = ?",
                            new String[]{
                                    String.valueOf(recipeId)
                            },
                            null,
                            null,
                            null
                    );


            while (ingredientCursor.moveToNext()) {

                String requiredName =
                        ingredientCursor.getString(
                                ingredientCursor
                                        .getColumnIndexOrThrow(
                                                INGREDIENT_NAME
                                        )
                        );


                double requiredQuantity =
                        ingredientCursor.getDouble(
                                ingredientCursor
                                        .getColumnIndexOrThrow(
                                                INGREDIENT_QUANTITY
                                        )
                        );


                String requiredUnit =
                        ingredientCursor.getString(
                                ingredientCursor
                                        .getColumnIndexOrThrow(
                                                INGREDIENT_UNIT
                                        )
                        );


                boolean ingredientFound = false;


                // Check pantry for this ingredient
                Cursor pantryCursor =
                        db.rawQuery(
                                "SELECT * FROM " +
                                        TABLE_PANTRY,
                                null
                        );


                while (pantryCursor.moveToNext()) {

                    String pantryName =
                            pantryCursor.getString(
                                    pantryCursor
                                            .getColumnIndexOrThrow(
                                                    COLUMN_NAME
                                            )
                            );


                    int pantryQuantity =
                            pantryCursor.getInt(
                                    pantryCursor
                                            .getColumnIndexOrThrow(
                                                    COLUMN_QUANTITY
                                            )
                            );


                    String pantryUnit =
                            pantryCursor.getString(
                                    pantryCursor
                                            .getColumnIndexOrThrow(
                                                    COLUMN_UNIT
                                            )
                            );


                    // Normalize names before comparing
                    String normalizedPantryName =
                            RecipeMatcher
                                    .normalizeIngredientName(
                                            pantryName
                                    );


                    String normalizedRequiredName =
                            RecipeMatcher
                                    .normalizeIngredientName(
                                            requiredName
                                    );


                    // Same ingredient?
                    if (normalizedPantryName.equals(
                            normalizedRequiredName
                    )) {

                        // Enough quantity?
                        if (RecipeMatcher.hasEnoughQuantity(
                                pantryQuantity,
                                pantryUnit,
                                requiredQuantity,
                                requiredUnit
                        )) {

                            ingredientFound = true;
                            break;
                        }
                    }
                }


                pantryCursor.close();


                // If even ONE ingredient is missing
                // or insufficient, reject the recipe
                if (!ingredientFound) {

                    canMakeRecipe = false;
                    break;
                }
            }


            ingredientCursor.close();


            // Add only recipes that passed
            // every ingredient check
            if (canMakeRecipe) {

                Recipe recipe =
                        new Recipe(
                                recipeId,
                                recipeName,
                                recipeSteps
                        );

                suggestedRecipes.add(recipe);
            }
        }


        recipeCursor.close();

        db.close();


        return suggestedRecipes;
    }
    public java.util.ArrayList<RecipeIngredient> getRecipeIngredientList(
            int recipeId
    ) {

        java.util.ArrayList<RecipeIngredient> ingredientList =
                new java.util.ArrayList<>();

        SQLiteDatabase db =
                this.getReadableDatabase();

        Cursor cursor =
                db.query(
                        TABLE_RECIPE_INGREDIENTS,
                        null,
                        INGREDIENT_RECIPE_ID + " = ?",
                        new String[]{
                                String.valueOf(recipeId)
                        },
                        null,
                        null,
                        null
                );

        while (cursor.moveToNext()) {

            int ingredientId =
                    cursor.getInt(
                            cursor.getColumnIndexOrThrow(
                                    INGREDIENT_ID
                            )
                    );

            String ingredientName =
                    cursor.getString(
                            cursor.getColumnIndexOrThrow(
                                    INGREDIENT_NAME
                            )
                    );

            double quantity =
                    cursor.getDouble(
                            cursor.getColumnIndexOrThrow(
                                    INGREDIENT_QUANTITY
                            )
                    );

            String unit =
                    cursor.getString(
                            cursor.getColumnIndexOrThrow(
                                    INGREDIENT_UNIT
                            )
                    );

            RecipeIngredient ingredient =
                    new RecipeIngredient(
                            ingredientId,
                            recipeId,
                            ingredientName,
                            quantity,
                            unit
                    );

            ingredientList.add(
                    ingredient
            );
        }

        cursor.close();
        db.close();

        return ingredientList;
    }
}