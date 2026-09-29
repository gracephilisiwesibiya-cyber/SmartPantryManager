package com.example.smartpantrymanager;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.Calendar;

public class AddItemActivity extends AppCompatActivity {

    private EditText etItemName;
    private EditText etQuantity;
    private EditText etUnit;
    private EditText etCategory;
    private EditText etExpiryDate;
    private Button btnSaveItem;

    // Database
    private DatabaseHelper databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_add_item);

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
        etItemName = findViewById(R.id.etItemName);
        etQuantity = findViewById(R.id.etQuantity);
        etUnit = findViewById(R.id.etUnit);
        etCategory = findViewById(R.id.etCategory);
        etExpiryDate = findViewById(R.id.etExpiryDate);
        btnSaveItem = findViewById(R.id.btnSaveItem);

        // Prevent manual typing in the expiry date field
        etExpiryDate.setFocusable(false);
        etExpiryDate.setClickable(true);

        // Open calendar when expiry date is clicked
        etExpiryDate.setOnClickListener(v -> {

            Calendar calendar = Calendar.getInstance();

            int year = calendar.get(Calendar.YEAR);
            int month = calendar.get(Calendar.MONTH);
            int day = calendar.get(Calendar.DAY_OF_MONTH);

            DatePickerDialog datePickerDialog =
                    new DatePickerDialog(
                            AddItemActivity.this,
                            (view, selectedYear,
                             selectedMonth, selectedDay) -> {

                                String selectedDate =
                                        String.format(
                                                "%02d/%02d/%04d",
                                                selectedDay,
                                                selectedMonth + 1,
                                                selectedYear
                                        );

                                etExpiryDate.setText(selectedDate);
                            },
                            year,
                            month,
                            day
                    );

            datePickerDialog.show();
        });

        // Create database helper
        databaseHelper = new DatabaseHelper(this);

        // Save button
        btnSaveItem.setOnClickListener(v -> saveItem());
    }

    private void saveItem() {

        // Read information entered by the user
        String itemName =
                etItemName.getText().toString().trim();

        String quantityText =
                etQuantity.getText().toString().trim();

        String unit =
                etUnit.getText().toString().trim();

        String category =
                etCategory.getText().toString().trim();

        String expiryDate =
                etExpiryDate.getText().toString().trim();


        // Validate item name
        if (itemName.isEmpty()) {

            etItemName.setError(
                    "Please enter item name"
            );

            etItemName.requestFocus();
            return;
        }


        // Validate quantity
        if (quantityText.isEmpty()) {

            etQuantity.setError(
                    "Please enter quantity"
            );

            etQuantity.requestFocus();
            return;
        }


        // Validate unit
        if (unit.isEmpty()) {

            etUnit.setError(
                    "Please enter unit"
            );

            etUnit.requestFocus();
            return;
        }


        // Validate category
        if (category.isEmpty()) {

            etCategory.setError(
                    "Please enter category"
            );

            etCategory.requestFocus();
            return;
        }


        // Validate expiry date
        if (expiryDate.isEmpty()) {

            etExpiryDate.setError(
                    "Please enter expiry date"
            );

            return;
        }


        // Convert quantity from text to number
        int quantity;

        try {

            quantity =
                    Integer.parseInt(quantityText);

        } catch (NumberFormatException e) {

            etQuantity.setError(
                    "Please enter a valid quantity"
            );

            etQuantity.requestFocus();
            return;
        }


        // Quantity cannot be zero or negative
        if (quantity <= 0) {

            etQuantity.setError(
                    "Quantity must be at least 1"
            );

            etQuantity.requestFocus();
            return;
        }


        // Save item to SQLite database
        boolean isInserted =
                databaseHelper.addPantryItem(
                        itemName,
                        quantity,
                        unit,
                        category,
                        expiryDate
                );


        if (isInserted) {

            Toast.makeText(
                    AddItemActivity.this,
                    itemName + " saved successfully!",
                    Toast.LENGTH_SHORT
            ).show();


            // Clear fields after successful saving
            etItemName.setText("");
            etQuantity.setText("");
            etUnit.setText("");
            etCategory.setText("");
            etExpiryDate.setText("");

            etItemName.requestFocus();

        } else {

            Toast.makeText(
                    AddItemActivity.this,
                    "Failed to save item",
                    Toast.LENGTH_SHORT
            ).show();
        }
    }
}