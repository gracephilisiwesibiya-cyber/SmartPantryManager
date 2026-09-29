package com.example.smartpantrymanager;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

public class PantryAdapter
        extends RecyclerView.Adapter<PantryAdapter.PantryViewHolder> {

    private final List<PantryItem> pantryItems;
    private final OnPantryItemClickListener listener;


    // Sends button clicks back to ViewPantryActivity
    public interface OnPantryItemClickListener {

        void onEditClick(PantryItem item);

        void onDeleteClick(PantryItem item);

        void onShoppingClick(PantryItem item);
    }


    // Constructor
    public PantryAdapter(
            List<PantryItem> pantryItems,
            OnPantryItemClickListener listener
    ) {

        this.pantryItems = pantryItems;
        this.listener = listener;
    }


    // Creates the layout for one pantry item
    @NonNull
    @Override
    public PantryViewHolder onCreateViewHolder(
            @NonNull ViewGroup parent,
            int viewType
    ) {

        View view =
                LayoutInflater
                        .from(parent.getContext())
                        .inflate(
                                R.layout.item_pantry,
                                parent,
                                false
                        );

        return new PantryViewHolder(view);
    }


    // Places pantry data inside each RecyclerView row
    @Override
    public void onBindViewHolder(
            @NonNull PantryViewHolder holder,
            int position
    ) {

        PantryItem item =
                pantryItems.get(position);


        // Display pantry information
        holder.tvItemName.setText(
                item.getName()
        );

        holder.tvQuantity.setText(
                "Quantity: "
                        + item.getQuantity()
                        + " "
                        + item.getUnit()
        );

        holder.tvCategory.setText(
                "Category: "
                        + item.getCategory()
        );

        holder.tvExpiryDate.setText(
                "Expiry Date: "
                        + item.getExpiryDate()
        );


        // -----------------------------------------
        // LOAD EXPIRY WARNING SETTING
        // -----------------------------------------

        Context context =
                holder.itemView.getContext();

        SharedPreferences sharedPreferences =
                context.getSharedPreferences(
                        "SmartPantrySettings",
                        Context.MODE_PRIVATE
                );

        boolean showExpiryWarnings =
                sharedPreferences.getBoolean(
                        "expiry_warnings",
                        true
                );


        // -----------------------------------------
        // BUILD WARNING MESSAGE
        // -----------------------------------------

        String warningMessage = "";


        // Check low stock
        // This is independent from expiry settings.
        if (item.getQuantity() <= 2) {

            warningMessage =
                    "⚠ LOW STOCK";

            holder.btnAddToShopping.setVisibility(
                    View.VISIBLE
            );

        } else {

            holder.btnAddToShopping.setVisibility(
                    View.GONE
            );
        }


        // -----------------------------------------
        // CHECK EXPIRY ONLY IF SETTING IS ON
        // -----------------------------------------

        if (showExpiryWarnings) {

            String expiryWarning =
                    getExpiryWarning(
                            item.getExpiryDate()
                    );


            if (!expiryWarning.isEmpty()) {

                if (!warningMessage.isEmpty()) {

                    warningMessage += "\n";
                }

                warningMessage +=
                        expiryWarning;
            }
        }


        // -----------------------------------------
        // SHOW OR HIDE WARNING TEXT
        // -----------------------------------------

        if (!warningMessage.isEmpty()) {

            holder.tvWarning.setText(
                    warningMessage
            );

            holder.tvWarning.setVisibility(
                    View.VISIBLE
            );

        } else {

            holder.tvWarning.setText("");

            holder.tvWarning.setVisibility(
                    View.GONE
            );
        }


        // Edit button
        holder.btnEdit.setOnClickListener(v ->
                listener.onEditClick(item)
        );


        // Delete button
        holder.btnDelete.setOnClickListener(v ->
                listener.onDeleteClick(item)
        );


        // Add to Shopping List button
        holder.btnAddToShopping.setOnClickListener(v ->
                listener.onShoppingClick(item)
        );
    }


    // Check whether an item is expired
    // or will expire within 7 days
    private String getExpiryWarning(
            String expiryDate
    ) {

        SimpleDateFormat dateFormat =
                new SimpleDateFormat(
                        "dd/MM/yyyy",
                        Locale.getDefault()
                );

        dateFormat.setLenient(false);


        try {

            Date expiry =
                    dateFormat.parse(
                            expiryDate
                    );

            Date today =
                    dateFormat.parse(
                            dateFormat.format(
                                    new Date()
                            )
                    );


            if (expiry == null
                    || today == null) {

                return "";
            }


            long difference =
                    expiry.getTime()
                            - today.getTime();


            long daysUntilExpiry =
                    TimeUnit.MILLISECONDS
                            .toDays(difference);


            if (daysUntilExpiry < 0) {

                return "❌ EXPIRED";

            } else if (daysUntilExpiry <= 7) {

                return "⚠ EXPIRING SOON";

            } else {

                return "";
            }


        } catch (ParseException e) {

            return "";
        }
    }


    // Number of pantry items
    @Override
    public int getItemCount() {

        return pantryItems.size();
    }


    // Holds the views from item_pantry.xml
    public static class PantryViewHolder
            extends RecyclerView.ViewHolder {

        TextView tvItemName;
        TextView tvQuantity;
        TextView tvCategory;
        TextView tvExpiryDate;
        TextView tvWarning;

        Button btnEdit;
        Button btnDelete;
        Button btnAddToShopping;


        public PantryViewHolder(
                @NonNull View itemView
        ) {

            super(itemView);


            tvItemName =
                    itemView.findViewById(
                            R.id.tvItemName
                    );

            tvQuantity =
                    itemView.findViewById(
                            R.id.tvQuantity
                    );

            tvCategory =
                    itemView.findViewById(
                            R.id.tvCategory
                    );

            tvExpiryDate =
                    itemView.findViewById(
                            R.id.tvExpiryDate
                    );

            tvWarning =
                    itemView.findViewById(
                            R.id.tvWarning
                    );

            btnEdit =
                    itemView.findViewById(
                            R.id.btnEdit
                    );

            btnDelete =
                    itemView.findViewById(
                            R.id.btnDelete
                    );

            btnAddToShopping =
                    itemView.findViewById(
                            R.id.btnAddToShopping
                    );
        }
    }
}