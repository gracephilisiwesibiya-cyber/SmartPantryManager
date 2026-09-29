package com.example.smartpantrymanager;

import android.app.DatePickerDialog;
import android.database.Cursor;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Calendar;

public class ViewPantryActivity extends AppCompatActivity {

    private RecyclerView recyclerViewPantry;
    private TextView tvEmptyPantry;

    private DatabaseHelper databaseHelper;

    private ArrayList<PantryItem> pantryItems;
    private PantryAdapter pantryAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        EdgeToEdge.enable(this);

        setContentView(
                R.layout.activity_view_pantry
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
        recyclerViewPantry =
                findViewById(
                        R.id.recyclerViewPantry
                );

        tvEmptyPantry =
                findViewById(
                        R.id.tvEmptyPantry
                );


        // Create database helper
        databaseHelper =
                new DatabaseHelper(this);


        // Create list that will hold PantryItem objects
        pantryItems =
                new ArrayList<>();


        // RecyclerView displays items vertically
        recyclerViewPantry.setLayoutManager(
                new LinearLayoutManager(this)
        );


        // Create custom Adapter
        pantryAdapter =
                new PantryAdapter(
                        pantryItems,
                        new PantryAdapter
                                .OnPantryItemClickListener() {

                            @Override
                            public void onEditClick(
                                    PantryItem item
                            ) {

                                showEditDialog(item);
                            }


                            @Override
                            public void onDeleteClick(
                                    PantryItem item
                            ) {

                                confirmDelete(item);
                            }


                            @Override
                            public void onShoppingClick(
                                    PantryItem item
                            ) {

                                addToShoppingList(item);
                            }
                        }
                );


        // Connect Adapter to RecyclerView
        recyclerViewPantry.setAdapter(
                pantryAdapter
        );


        // Load SQLite data
        loadPantryItems();
    }


    // Read pantry records from SQLite
    private void loadPantryItems() {

        pantryItems.clear();

        Cursor cursor =
                databaseHelper.getAllPantryItems();


        while (cursor.moveToNext()) {

            int id =
                    cursor.getInt(
                            cursor.getColumnIndexOrThrow(
                                    DatabaseHelper.COLUMN_ID
                            )
                    );


            String name =
                    cursor.getString(
                            cursor.getColumnIndexOrThrow(
                                    DatabaseHelper.COLUMN_NAME
                            )
                    );


            int quantity =
                    cursor.getInt(
                            cursor.getColumnIndexOrThrow(
                                    DatabaseHelper.COLUMN_QUANTITY
                            )
                    );


            String unit =
                    cursor.getString(
                            cursor.getColumnIndexOrThrow(
                                    DatabaseHelper.COLUMN_UNIT
                            )
                    );


            String category =
                    cursor.getString(
                            cursor.getColumnIndexOrThrow(
                                    DatabaseHelper.COLUMN_CATEGORY
                            )
                    );


            String expiryDate =
                    cursor.getString(
                            cursor.getColumnIndexOrThrow(
                                    DatabaseHelper.COLUMN_EXPIRY_DATE
                            )
                    );


            // Convert database row into Java object
            PantryItem item =
                    new PantryItem(
                            id,
                            name,
                            quantity,
                            unit,
                            category,
                            expiryDate
                    );


            pantryItems.add(item);
        }


        cursor.close();


        // Tell Adapter that the data changed
        pantryAdapter.notifyDataSetChanged();


        // Show empty message when there are no items
        if (pantryItems.isEmpty()) {

            tvEmptyPantry.setVisibility(
                    View.VISIBLE
            );

            recyclerViewPantry.setVisibility(
                    View.GONE
            );

        } else {

            tvEmptyPantry.setVisibility(
                    View.GONE
            );

            recyclerViewPantry.setVisibility(
                    View.VISIBLE
            );
        }
    }


    // Add low-stock item to Shopping List
    private void addToShoppingList(
            PantryItem item
    ) {

        if (databaseHelper
                .isShoppingItemExists(
                        item.getName()
                )) {

            Toast.makeText(
                    this,
                    item.getName()
                            + " is already in the shopping list!",
                    Toast.LENGTH_SHORT
            ).show();

            return;
        }


        boolean added =
                databaseHelper.addShoppingItem(
                        item.getName(),
                        1
                );


        if (added) {

            Toast.makeText(
                    this,
                    item.getName()
                            + " added to shopping list!",
                    Toast.LENGTH_SHORT
            ).show();

        } else {

            Toast.makeText(
                    this,
                    "Failed to add item",
                    Toast.LENGTH_SHORT
            ).show();
        }
    }


    // Open Edit dialog
    private void showEditDialog(
            PantryItem item
    ) {

        LinearLayout editLayout =
                new LinearLayout(this);

        editLayout.setOrientation(
                LinearLayout.VERTICAL
        );

        editLayout.setPadding(
                50,
                20,
                50,
                10
        );


        // Item name
        EditText editName =
                new EditText(this);

        editName.setHint(
                "Item Name"
        );

        editName.setText(
                item.getName()
        );


        // Quantity
        EditText editQuantity =
                new EditText(this);

        editQuantity.setHint(
                "Quantity"
        );

        editQuantity.setInputType(
                InputType.TYPE_CLASS_NUMBER
        );

        editQuantity.setText(
                String.valueOf(
                        item.getQuantity()
                )
        );


        // Unit
        EditText editUnit =
                new EditText(this);

        editUnit.setHint(
                "Unit"
        );

        editUnit.setText(
                item.getUnit()
        );


        // Category
        EditText editCategory =
                new EditText(this);

        editCategory.setHint(
                "Category"
        );

        editCategory.setText(
                item.getCategory()
        );


        // Expiry date
        EditText editExpiry =
                new EditText(this);

        editExpiry.setHint(
                "Expiry Date"
        );

        editExpiry.setText(
                item.getExpiryDate()
        );

        editExpiry.setFocusable(false);
        editExpiry.setClickable(true);


        // Open DatePicker
        editExpiry.setOnClickListener(v -> {

            Calendar calendar =
                    Calendar.getInstance();

            int year =
                    calendar.get(
                            Calendar.YEAR
                    );

            int month =
                    calendar.get(
                            Calendar.MONTH
                    );

            int day =
                    calendar.get(
                            Calendar.DAY_OF_MONTH
                    );


            DatePickerDialog datePickerDialog =
                    new DatePickerDialog(
                            ViewPantryActivity.this,
                            (view,
                             selectedYear,
                             selectedMonth,
                             selectedDay) -> {

                                String selectedDate =
                                        String.format(
                                                "%02d/%02d/%04d",
                                                selectedDay,
                                                selectedMonth + 1,
                                                selectedYear
                                        );

                                editExpiry.setText(
                                        selectedDate
                                );
                            },
                            year,
                            month,
                            day
                    );


            datePickerDialog.show();
        });


        // Add fields to dialog
        editLayout.addView(
                editName
        );

        editLayout.addView(
                editQuantity
        );

        editLayout.addView(
                editUnit
        );

        editLayout.addView(
                editCategory
        );

        editLayout.addView(
                editExpiry
        );


        AlertDialog dialog =
                new AlertDialog.Builder(this)
                        .setTitle(
                                "Edit Pantry Item"
                        )
                        .setView(
                                editLayout
                        )
                        .setPositiveButton(
                                "Update",
                                null
                        )
                        .setNegativeButton(
                                "Cancel",
                                null
                        )
                        .create();


        dialog.setOnShowListener(
                dialogInterface -> {

                    Button updateButton =
                            dialog.getButton(
                                    AlertDialog.BUTTON_POSITIVE
                            );


                    updateButton.setOnClickListener(v -> {

                        String newName =
                                editName
                                        .getText()
                                        .toString()
                                        .trim();


                        String quantityText =
                                editQuantity
                                        .getText()
                                        .toString()
                                        .trim();


                        String newUnit =
                                editUnit
                                        .getText()
                                        .toString()
                                        .trim();


                        String newCategory =
                                editCategory
                                        .getText()
                                        .toString()
                                        .trim();


                        String newExpiry =
                                editExpiry
                                        .getText()
                                        .toString()
                                        .trim();


                        // Validation
                        if (newName.isEmpty()) {

                            editName.setError(
                                    "Please enter item name"
                            );

                            editName.requestFocus();
                            return;
                        }


                        if (quantityText.isEmpty()) {

                            editQuantity.setError(
                                    "Please enter quantity"
                            );

                            editQuantity.requestFocus();
                            return;
                        }


                        if (newUnit.isEmpty()) {

                            editUnit.setError(
                                    "Please enter unit"
                            );

                            editUnit.requestFocus();
                            return;
                        }


                        if (newCategory.isEmpty()) {

                            editCategory.setError(
                                    "Please enter category"
                            );

                            editCategory.requestFocus();
                            return;
                        }


                        if (newExpiry.isEmpty()) {

                            editExpiry.setError(
                                    "Please enter expiry date"
                            );

                            return;
                        }


                        int newQuantity;

                        try {

                            newQuantity =
                                    Integer.parseInt(
                                            quantityText
                                    );

                        } catch (
                                NumberFormatException e
                        ) {

                            editQuantity.setError(
                                    "Please enter a valid quantity"
                            );

                            editQuantity.requestFocus();
                            return;
                        }


                        if (newQuantity <= 0) {

                            editQuantity.setError(
                                    "Quantity must be at least 1"
                            );

                            editQuantity.requestFocus();
                            return;
                        }


                        // Update SQLite record
                        boolean updated =
                                databaseHelper
                                        .updatePantryItem(
                                                item.getId(),
                                                newName,
                                                newQuantity,
                                                newUnit,
                                                newCategory,
                                                newExpiry
                                        );


                        if (updated) {

                            Toast.makeText(
                                    ViewPantryActivity.this,
                                    newName
                                            + " updated successfully!",
                                    Toast.LENGTH_SHORT
                            ).show();


                            dialog.dismiss();

                            // Reload RecyclerView
                            loadPantryItems();

                        } else {

                            Toast.makeText(
                                    ViewPantryActivity.this,
                                    "Failed to update item",
                                    Toast.LENGTH_SHORT
                            ).show();
                        }
                    });
                }
        );


        dialog.show();
    }


    // Confirm before deleting item
    private void confirmDelete(
            PantryItem item
    ) {

        new AlertDialog.Builder(this)
                .setTitle(
                        "Delete Pantry Item"
                )
                .setMessage(
                        "Are you sure you want to delete "
                                + item.getName()
                                + "?"
                )
                .setPositiveButton(
                        "Delete",
                        (dialog, which) -> {

                            boolean deleted =
                                    databaseHelper
                                            .deletePantryItem(
                                                    item.getId()
                                            );


                            if (deleted) {

                                Toast.makeText(
                                        ViewPantryActivity.this,
                                        item.getName()
                                                + " deleted successfully!",
                                        Toast.LENGTH_SHORT
                                ).show();


                                // Reload RecyclerView
                                loadPantryItems();

                            } else {

                                Toast.makeText(
                                        ViewPantryActivity.this,
                                        "Failed to delete item",
                                        Toast.LENGTH_SHORT
                                ).show();
                            }
                        }
                )
                .setNegativeButton(
                        "Cancel",
                        null
                )
                .show();
    }


    // Refresh data when returning to screen
    @Override
    protected void onResume() {

        super.onResume();

        if (databaseHelper != null
                && pantryAdapter != null) {

            loadPantryItems();
        }
    }
}