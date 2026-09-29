package com.example.smartpantrymanager;

import android.database.Cursor;
import android.os.Bundle;
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

public class ShoppingListActivity extends AppCompatActivity {

    private EditText etShoppingItem;
    private EditText etShoppingQuantity;
    private Button btnAddShoppingItem;
    private LinearLayout shoppingListContainer;

    private DatabaseHelper databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_shopping_list);

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

        // Connect Java variables to XML
        etShoppingItem =
                findViewById(R.id.etShoppingItem);

        etShoppingQuantity =
                findViewById(R.id.etShoppingQuantity);

        btnAddShoppingItem =
                findViewById(R.id.btnAddShoppingItem);

        shoppingListContainer =
                findViewById(R.id.shoppingListContainer);

        // Connect to database
        databaseHelper = new DatabaseHelper(this);

        // Display saved shopping items
        displayShoppingItems();

        // Add button
        btnAddShoppingItem.setOnClickListener(v ->
                addShoppingItem()
        );
    }

    private void addShoppingItem() {

        String itemName =
                etShoppingItem.getText().toString().trim();

        String quantityText =
                etShoppingQuantity.getText().toString().trim();

        // Validate item name
        if (itemName.isEmpty()) {

            etShoppingItem.setError(
                    "Please enter an item"
            );

            etShoppingItem.requestFocus();
            return;
        }

        // Validate quantity
        if (quantityText.isEmpty()) {

            etShoppingQuantity.setError(
                    "Please enter quantity"
            );

            etShoppingQuantity.requestFocus();
            return;
        }

        int quantity;

        try {

            quantity = Integer.parseInt(quantityText);

        } catch (NumberFormatException e) {

            etShoppingQuantity.setError(
                    "Please enter a valid quantity"
            );

            etShoppingQuantity.requestFocus();
            return;
        }
        // Quantity must be greater than zero
        if (quantity <= 0) {

            etShoppingQuantity.setError(
                    "Quantity must be at least 1"
            );

            etShoppingQuantity.requestFocus();
            return;
        }
// Check if item is already in the shopping list
        if (databaseHelper.isShoppingItemExists(itemName)) {

            Toast.makeText(
                    ShoppingListActivity.this,
                    itemName + " is already in the shopping list!",
                    Toast.LENGTH_SHORT
            ).show();

            etShoppingItem.requestFocus();
            return;
        }
        // Save to SQLite
        boolean inserted =
                databaseHelper.addShoppingItem(
                        itemName,
                        quantity
                );

        if (inserted) {

            Toast.makeText(
                    ShoppingListActivity.this,
                    itemName + " added to shopping list!",
                    Toast.LENGTH_SHORT
            ).show();

            // Clear fields
            etShoppingItem.setText("");
            etShoppingQuantity.setText("");

            etShoppingItem.requestFocus();

            // Refresh list
            displayShoppingItems();

        } else {

            Toast.makeText(
                    ShoppingListActivity.this,
                    "Failed to add item",
                    Toast.LENGTH_SHORT
            ).show();
        }
    }

    private void displayShoppingItems() {

        // Clear old views before refreshing
        shoppingListContainer.removeAllViews();

        Cursor cursor =
                databaseHelper.getAllShoppingItems();

        // Empty list message
        if (cursor.getCount() == 0) {

            TextView emptyMessage =
                    new TextView(this);

            emptyMessage.setText(
                    "Your shopping list is empty."
            );

            emptyMessage.setTextSize(18);

            shoppingListContainer.addView(
                    emptyMessage
            );

            cursor.close();
            return;
        }

        while (cursor.moveToNext()) {

            int id =
                    cursor.getInt(
                            cursor.getColumnIndexOrThrow(
                                    DatabaseHelper.SHOPPING_ID
                            )
                    );

            String itemName =
                    cursor.getString(
                            cursor.getColumnIndexOrThrow(
                                    DatabaseHelper.SHOPPING_NAME
                            )
                    );

            int quantity =
                    cursor.getInt(
                            cursor.getColumnIndexOrThrow(
                                    DatabaseHelper.SHOPPING_QUANTITY
                            )
                    );

            // Container for each item
            LinearLayout itemLayout =
                    new LinearLayout(this);

            itemLayout.setOrientation(
                    LinearLayout.VERTICAL
            );

            itemLayout.setPadding(
                    16,
                    16,
                    16,
                    24
            );

            // Display item information
            TextView itemText =
                    new TextView(this);

            itemText.setText(
                    "Item: " + itemName +
                            "\nQuantity: " + quantity
            );

            itemText.setTextSize(18);

            // Remove button
            Button removeButton =
                    new Button(this);

            removeButton.setText(
                    "Remove"
            );

            removeButton.setOnClickListener(v ->
                    confirmDelete(
                            id,
                            itemName
                    )
            );

            itemLayout.addView(itemText);
            itemLayout.addView(removeButton);

            shoppingListContainer.addView(
                    itemLayout
            );
        }

        cursor.close();
    }

    private void confirmDelete(
            int id,
            String itemName
    ) {

        new AlertDialog.Builder(this)

                .setTitle(
                        "Remove Shopping Item"
                )

                .setMessage(
                        "Remove " +
                                itemName +
                                " from your shopping list?"
                )

                .setPositiveButton(
                        "Remove",
                        (dialog, which) -> {

                            boolean deleted =
                                    databaseHelper
                                            .deleteShoppingItem(id);

                            if (deleted) {

                                Toast.makeText(
                                        ShoppingListActivity.this,
                                        itemName +
                                                " removed!",
                                        Toast.LENGTH_SHORT
                                ).show();

                                displayShoppingItems();

                            } else {

                                Toast.makeText(
                                        ShoppingListActivity.this,
                                        "Failed to remove item",
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
}